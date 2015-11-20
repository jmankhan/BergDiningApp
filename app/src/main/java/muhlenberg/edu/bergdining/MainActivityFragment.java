package muhlenberg.edu.bergdining;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.lang.reflect.Field;


import muhlenberg.edu.bergdining.simplexml.MenuDay;
import muhlenberg.edu.bergdining.simplexml.MenuItem;
import muhlenberg.edu.bergdining.simplexml.MenuMeal;
import muhlenberg.edu.bergdining.simplexml.MenuWeek;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import retrofit.SimpleXmlConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements Callback<MenuWeek> {

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://berg-dining.herokuapp.com/")
                .addConverterFactory(SimpleXmlConverterFactory.create(new Persister(new AnnotationStrategy())))
                .build();

        BergServer bs = retrofit.create(BergServer.class);
        Call<MenuWeek> call = bs.getMenu();
        call.enqueue(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, true);
        return view;
    }


    @Override
    public void onResponse(Response<MenuWeek> response, Retrofit retrofit) {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        MenuWeek menu = response.body();
        //magic!
        for(int i=0; i<7; i++)
            menu.days.remove(0);

        Field[] fields=R.raw.class.getFields();

        MenuMeal mondayBrk = menu.days.get(0).meal.get(1);

        for(MenuItem i : mondayBrk.items) {
            try {
                i.id = (fields[0].getInt(fields[0]));
            } catch (IllegalAccessException e) {e.printStackTrace();}
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler);

        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        recyclerView.setLayoutManager(manager);

        MenuListAdapter adapter = new MenuListAdapter(getActivity(), mondayBrk);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onFailure(Throwable t) {
        Toast.makeText(getContext(), "failure", Toast.LENGTH_SHORT).show();
        t.printStackTrace();
        Log.d("diningserver", "failure: " + t.getMessage());
    }
}
