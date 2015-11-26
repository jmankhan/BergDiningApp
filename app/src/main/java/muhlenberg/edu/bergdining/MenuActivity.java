package muhlenberg.edu.bergdining;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import muhlenberg.edu.bergdining.simplexml.MenuWeek;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class MenuActivity extends AppCompatActivity implements Callback<MenuWeek>, ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    MenuWeek menu;
    boolean save;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu);

        //set up the title
        Button title = (Button) findViewById(R.id.toolbar_menu_title);
        setupTitle(title);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                if (xml != null) {
                    Serializer ser = new Persister(new AnnotationStrategy());
                    try {
                        menu = ser.read(MenuWeek.class, xml);
                        for (int i = 0; i < 7; i++)
                            menu.days.remove(0);

                        instantiateViewPager();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
        for (int i = 0; i < 14; i++)
            menu.days.remove(0);

        instantiateViewPager();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(this, "Failed to update menu", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        String[] days = getResources().getStringArray(R.array.days);
        int day = position / 3;
        Button title = (Button) findViewById(R.id.toolbar_menu_title);
        title.setText(days[day]);

        ActionBar toolbar = getSupportActionBar();

        if (toolbar != null) {
            toolbar.setDisplayShowTitleEnabled(false);
            ImageView logo = (ImageView) findViewById(R.id.toolbar_menu_logo);

            switch (menu.days.get(position / 3).meal.get(position % 3).name) {
                case "brk":
                    logo.setImageResource(R.drawable.sunrise);
                    break;
                case "lun":
                    logo.setImageResource(R.drawable.sun_lunch);
                    break;
                case "din":
                    logo.setImageResource(R.drawable.moon);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return MenuActivityFragment.newInstance(position, menu);
        }

        @Override
        public int getCount() {
            return 21;
        }
    }

    public void instantiateViewPager() {
        viewPager = (ViewPager) findViewById(R.id.menu_week_pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(0);
        viewPager.addOnPageChangeListener(this);

        Calendar cal = Calendar.getInstance();
        int time = cal.get(Calendar.HOUR_OF_DAY);
        int hour;
        if (time < 10) //before 10am - breakfast
            hour = 0;
        else if (time < 16) //before 5pm - lunch
            hour = 1;
        else
            hour = 2;

        int day = cal.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY:
                viewPager.setCurrentItem(0 + hour);
                break;
            case Calendar.TUESDAY:
                viewPager.setCurrentItem(3 + hour);
                break;
            case Calendar.WEDNESDAY:
                viewPager.setCurrentItem(6 + hour);
                break;
            case Calendar.THURSDAY:
                viewPager.setCurrentItem(9 + hour);
                break;
            case Calendar.FRIDAY:
                viewPager.setCurrentItem(12 + hour);
                break;
            case Calendar.SATURDAY:
                viewPager.setCurrentItem(15 + hour);
                break;
            case Calendar.SUNDAY:
                viewPager.setCurrentItem(18 + hour);
                break;

            default:
                viewPager.setCurrentItem(0);
        }
    }

    public void setupTitle(Button title) {

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] days = getResources().getStringArray(R.array.days);

                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Select A Day");
                builder.setSingleChoiceItems(days, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        viewPager.setCurrentItem(item * 3);
                        dialog.dismiss();
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }
}