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
public class MainActivityFragment extends Fragment  {

    View view;
    int mealIndex =-1;
    MenuWeek menu;

    public static final MainActivityFragment newInstance(int day, MenuWeek menu) {
        MainActivityFragment f = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mealIndex", day);
        bundle.putSerializable("menu", menu);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);
        mealIndex = getArguments().getInt("mealIndex");
        menu = (MenuWeek) getArguments().getSerializable("menu");

        if(menu != null) {
            MenuMeal meal = menu.days.get(mealIndex % 7).meal.get(mealIndex % 3);
            Field[] fields = R.raw.class.getFields();
            for (MenuItem i : meal.items) {
                try {
                    i.id = (fields[0].getInt(fields[0]));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.menu_recycler);

            GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
            recyclerView.setLayoutManager(manager);

            MenuListAdapter adapter = new MenuListAdapter(getActivity(), meal);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
