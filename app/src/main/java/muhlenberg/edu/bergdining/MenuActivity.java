package muhlenberg.edu.bergdining;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
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

import muhlenberg.edu.bergdining.search.MenuSearchActivity;
import muhlenberg.edu.bergdining.simplexml.MenuWeek;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class MenuActivity extends AppCompatActivity implements OnPageChangeListener {
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    MenuWeek menu;
    AlertDialog titleDialog;

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

        menu = (MenuWeek) getIntent().getSerializableExtra("menu");
        instantiateViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
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

    public void onSearch(View view) {
        Intent intent = new Intent(MenuActivity.this, MenuSearchActivity.class);
        intent.putExtra("menu", MenuActivity.this.menu);
        startActivity(intent);
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
        else if (time < 16) //before 4pm - lunch
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
                Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_WEEK);

                String[] days = getResources().getStringArray(R.array.days);
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this, R.style.MyAlertDialogStyle);
                builder.setTitle("Select A Day");
                builder.setSingleChoiceItems(days, (day - 2) % 7, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        viewPager.setCurrentItem(item * 3);
                        dialog.dismiss();
                    }
                });
                titleDialog = builder.create();
                titleDialog.show();
            }
        });
    }

}