package muhlenberg.edu.bergdining;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import muhlenberg.edu.bergdining.retro.WeeklyMenu;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements Callback<WeeklyMenu> {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://berg-dining.herokuapp.com/")
                .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                .build();

        BergServer bs = retrofit.create(BergServer.class);
        Call<WeeklyMenu> call = bs.getMenu();
        call.enqueue(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, true);
        return view;
    }


    @Override
    public void onResponse(Response<WeeklyMenu> response, Retrofit retrofit) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        WeeklyMenu menu = response.body();
        Field[] fields=R.raw.class.getFields();
        for(int i=0; i<50; i++) {
            try {
                menu.getItems().get(i).setImgId(fields[i%9].getInt(fields[i%9]));
            } catch (IllegalAccessException e) {e.printStackTrace();}
        }

        GridView grid = (GridView) view.findViewById(R.id.menu_list);
        MenuListAdapter adapter = new MenuListAdapter(menu, getActivity(), metrics);
        grid.setAdapter(adapter);

        Log.d("diningserver", "Response receieved");
    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
        Log.d("diningserver", "failure: " + t.getMessage());
    }
}
