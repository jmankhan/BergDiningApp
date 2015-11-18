package muhlenberg.edu.bergdining;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

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
        GridView grid = (GridView) view.findViewById(R.id.menu_list);
        MenuListAdapter adapter = new MenuListAdapter(response.body(), getActivity());
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
