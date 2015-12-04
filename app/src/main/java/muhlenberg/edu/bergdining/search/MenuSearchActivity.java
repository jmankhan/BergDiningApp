package muhlenberg.edu.bergdining.search;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;

import muhlenberg.edu.bergdining.R;
import muhlenberg.edu.bergdining.simplexml.MenuDay;
import muhlenberg.edu.bergdining.simplexml.MenuItem;
import muhlenberg.edu.bergdining.simplexml.MenuMeal;
import muhlenberg.edu.bergdining.simplexml.MenuWeek;

/**
 * Created by Jalal on 12/4/2015.
 */
public class MenuSearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView filterView;
    ArrayList<MenuItem> filterItems;
    MenuSearchAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu_search);
        setSupportActionBar(toolbar);

        MenuWeek menu = (MenuWeek) getIntent().getSerializableExtra("menu");
        filterView = (RecyclerView) findViewById(R.id.menu_search_recycler);
        filterView.setLayoutManager(new LinearLayoutManager(this));

        filterItems = setupFilterItems(menu);

        adapter = new MenuSearchAdapter(this, filterItems);
        filterView.setAdapter(adapter);

    }

    /**
     * Add a master list of all the menu items available
     */
    public ArrayList<MenuItem> setupFilterItems(MenuWeek menu) {

        ArrayList<MenuItem> filterItems = new ArrayList<>();

        for(MenuDay d : menu.days) {
            for (MenuMeal m : d.meal) {
                for (MenuItem i : m.items) {
                    filterItems.add(i);
                }
            }
        }

        return filterItems;
    }

    /**
     * Matches query text to menu item name. Station, attribute, and other forms of filtering
     * will be added later
     * @param items
     * @param query
     * @return
     */
    private ArrayList<MenuItem> filter(ArrayList<MenuItem> items, String query) {
        query = query.toLowerCase();

        //return all items if empty string is searched
        if(query.equals("")) {
            return filterItems;
        }

        final ArrayList<MenuItem> filteredList = new ArrayList<>();

        for (MenuItem item : items) {
            final String text = item.name.toLowerCase();
            if (text.contains(query)) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d("parser", "searching " + newText);
        ArrayList<MenuItem> newFilterItems = filter(filterItems, newText);
        adapter.animateTo(newFilterItems);
        filterView.scrollToPosition(0);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        final android.view.MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);

        return true;
    }
}
