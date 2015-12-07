package muhlenberg.edu.bergdining;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class Splash extends Activity implements Callback<MenuWeek> {

    MenuWeek menu;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 5000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        getSharedPreferences("bergmenu", MODE_PRIVATE);

        if (shouldUpdate()) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://berg-dining.herokuapp.com/")
                    .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                    .build();

            BergServer bs = retrofit.create(BergServer.class);
            Call<MenuWeek> call = bs.getMenu();
            call.enqueue(this);
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Home-Activity. */
                Intent mainIntent = new Intent(Splash.this,HomeActivity.class);
                mainIntent.putExtra("menu", menu);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public boolean shouldUpdate() {
        boolean update;

        //read the menu from memory
        SharedPreferences prefs = getSharedPreferences("bergmenu", MODE_PRIVATE);

        //get the date it was added to memory
        long latest = prefs.getLong("latest", -1);

        //nothing in storage, download a new update and store it on phone
        if (latest != -1) {
            Calendar last = Calendar.getInstance();
            last.setTime(new Date(latest));

            //use this calendar to check if 7 days has passed since the last update
            GregorianCalendar week = new GregorianCalendar();
            week.setTime(new Date(latest));
            week.add(Calendar.DATE, 7);

            //use this calendar to store most recent monday's date
            Calendar monday = new GregorianCalendar();
            monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            Calendar now = Calendar.getInstance();

            //if the last update occurred before the most recent Monday,
            //or one week has passed since the last update
            //we need to download a new update
            if (last.before(monday) && monday.get(Calendar.MONTH) == last.get(Calendar.MONTH)
                    || now.after(week)) {
                Log.d("parser", "already have menu in memory, but need to update it");
                Log.d("parser", "last monday date: " + monday.get(Calendar.DATE));
                update = true;
            } else {
                //otherwise, we can just use the menu we have on file
                update = false;

                String xml = prefs.getString("latestmenu", null);
                if (xml != null) {
                    Serializer ser = new Persister(new AnnotationStrategy());
                    try {


                        //because of some strange serializing, the first few entries are blank
                        //so remove them
                        menu = ser.read(MenuWeek.class, xml);
                        while(menu.days.size() > 7)
                            menu.days.remove(0);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            //there is nothing in the storage, so we need to update
            Log.d("parser", "nothing in storage... making network call");
            update = true;
        }

        return update;
    }

    @Override
    public void onResponse(Response<MenuWeek> response, Retrofit retrofit) {

        //due to some strange serialization we get some extra, blank days, so remove them
        menu = response.body();
        while(menu.days.size() > 7)
            menu.days.remove(0);

        //store the downloaded menu into sharedpreferences
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
}