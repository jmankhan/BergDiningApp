package muhlenberg.edu.bergdining;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import muhlenberg.edu.bergdining.retro.WeeklyMenu;
import okio.Buffer;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<WeeklyMenu> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            String[] list = getAssets().list("menus");
            String menuName = "menus/" + list[list.length-1];

            Parser parser = new Parser(getAssets().open(menuName));
            ArrayList<Parser.Item> items = parser.getItems();
            for(Parser.Item i : items)
                Log.d("parser", i.name);

        } catch (IOException | XmlPullParserException e) {e.printStackTrace();}

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());

        muhlenberg.edu.bergdining.retro.MenuItem.MenuItemConverter converter = new muhlenberg.edu.bergdining.retro.MenuItem.MenuItemConverter();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://berg-dining.herokuapp.com/")
//                .client(client)
                .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                .build();

        BergServer bs = retrofit.create(BergServer.class);
        Call<WeeklyMenu> call = bs.getMenu();
        call.enqueue(this);
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
    public void onResponse(Response<WeeklyMenu> response, Retrofit retrofit) {
        Toast.makeText(MainActivity.this, "Received response!", Toast.LENGTH_LONG).show();
        Log.d("server", response.body().toString());
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(MainActivity.this, "failure", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
        Log.d("server", "failure: " + t.getMessage());
    }

    /*
    * Obtained from this stackoverflow question: http://stackoverflow.com/questions/32965790/retrofit-2-0-how-to-print-the-full-json-response
     */
    public static class LoggingInterceptor implements Interceptor {
        @Override
        public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
            Log.i("LoggingInterceptor", "inside intercept callback");
            Request request = chain.request();
            long t1 = System.nanoTime();
            String requestLog = String.format("Sending request %s on %s%n%s",
                    request.url(), chain.connection(), request.headers());
            if (request.method().compareToIgnoreCase("post") == 0) {
                requestLog = "\n" + requestLog + "\n" + bodyToString(request);
            }
            Log.d("TAG", "request" + "\n" + requestLog);
            com.squareup.okhttp.Response response = chain.proceed(request);
            long t2 = System.nanoTime();

            String responseLog = String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers());

            String bodyString = response.body().string();

            Log.d("TAG", "response only" + "\n" + bodyString);

            Log.d("TAG", "response" + "\n" + responseLog + "\n" + bodyString);

            return response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();

        }


        public static String bodyToString(final Request request) {
            try {
                final Request copy = request.newBuilder().build();
                final Buffer buffer = new Buffer();
                copy.body().writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                return "did not work";
            }
        }
    }
}
