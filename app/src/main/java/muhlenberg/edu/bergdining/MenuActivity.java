package muhlenberg.edu.bergdining;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import muhlenberg.edu.bergdining.simplexml.MenuWeek;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class MenuActivity extends AppCompatActivity implements Callback<MenuWeek> {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    MenuWeek menu;
    boolean save;

    static final String saveLocation = "weekly_menus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        save = false;

        SharedPreferences prefs = getSharedPreferences("bergmenu", MODE_PRIVATE);
        long latest = prefs.getLong("latest", -1);

        //nothing in storage, make sure to store next download
        if (latest != -1) {
            GregorianCalendar cal = new GregorianCalendar();

            Date last = new Date(latest);
            cal.setTime(last);
            cal.add(Calendar.DATE, 7);

            Calendar now = Calendar.getInstance();

            //if the last update was 1 or more weeks ago, make a new update
            //otherwise, just read the menu from the file
            if (cal.getTime().before(now.getTime())) {
                Log.d("parser", "already have menu in memory, but need to update it");
                save = true;
            } else {
                save = false;
                String xml = prefs.getString("latestmenu", null);
                if(xml != null) {
                    Serializer ser = new Persister(new AnnotationStrategy());
                    try {
                        menu = ser.read(MenuWeek.class, xml);
                        for(int i=0; i<7; i++)
                            menu.days.remove(0);

                        viewPager  = (ViewPager) findViewById(R.id.menu_week_pager);
                        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
                        viewPager.setAdapter(pagerAdapter);
                        Log.d("parser", "reading from memory..");
                        Log.d("parser", "size: " + menu.days.size());

                    } catch (Exception e) {e.printStackTrace();}
                }
            }
        } else {
            Log.d("parser", "nothing in storage... making network call");
            save = true;
        }

        if (save) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://berg-dining.herokuapp.com/")
                    .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                    .build();

            BergServer bs = retrofit.create(BergServer.class);
            Call<MenuWeek> call = bs.getMenu();
            call.enqueue(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResponse(Response<MenuWeek> response, Retrofit retrofit) {
        menu = response.body();
        for(int i=0; i<14;i++)
            menu.days.remove(0);


        viewPager  = (ViewPager) findViewById(R.id.menu_week_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        SharedPreferences prefs = getSharedPreferences("bergmenu", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Serializer ser = new Persister(new AnnotationStrategy());
        try {
            ser.write(menu, baos);
            editor.putString("latestmenu", new String(baos.toByteArray(), "UTF-8"));
            editor.putLong("latest", Calendar.getInstance().getTime().getTime());
            editor.apply();

            Log.d("parser", "adding new menu to storage");
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, "Failed to update menu", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MenuActivityFragment f = MenuActivityFragment.newInstance(position, menu);
            return f;
        }

        @Override
        public int getCount() {
            return 21;
        }
    }
}
