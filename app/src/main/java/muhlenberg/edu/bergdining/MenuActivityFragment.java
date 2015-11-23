package muhlenberg.edu.bergdining;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;


import muhlenberg.edu.bergdining.simplexml.MenuItem;
import muhlenberg.edu.bergdining.simplexml.MenuMeal;
import muhlenberg.edu.bergdining.simplexml.MenuWeek;

/**
 * A placeholder fragment containing a simple view.
 */
public class MenuActivityFragment extends Fragment  {

    View view;
    int mealIndex =-1;
    MenuWeek menu;

    public static final MenuActivityFragment newInstance(int day, MenuWeek menu) {
        MenuActivityFragment f = new MenuActivityFragment();
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
            int j=0;
            for (MenuItem i : meal.items) {
                try {
                    while(fields[j % fields.length].getName().contains("big")) {
                       j++;
                    }

                    i.id = (fields[j % fields.length].getInt(fields[j++ % fields.length]));
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
