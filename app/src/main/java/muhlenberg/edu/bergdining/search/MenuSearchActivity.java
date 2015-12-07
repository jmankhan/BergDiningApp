package muhlenberg.edu.bergdining.search;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

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
    ArrayList<MenuItem> masterItems;
    MenuSearchAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_search);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_menu_search);
        setSupportActionBar(toolbar);

        MenuWeek menu = (MenuWeek) getIntent().getSerializableExtra("menu");
        filterView = (RecyclerView) findViewById(R.id.menu_search_recycler);
        filterView.setLayoutManager(new LinearLayoutManager(this));

        masterItems = setupMasterItems(menu);
        ArrayList<MenuItem> copy = (ArrayList) masterItems.clone();

        adapter = new MenuSearchAdapter(this, copy);
        filterView.setAdapter(adapter);
    }

    /**
     * Add a master list of all the menu items available
     */
    public ArrayList<MenuItem> setupMasterItems(MenuWeek menu) {

        ArrayList<MenuItem> filterItems = new ArrayList<>();

        for (MenuDay d : menu.days) {
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
     *
     * @param query
     * @return
     */
    private ArrayList<MenuItem> filter(String query) {
        query = query.toLowerCase();

        //return all items if empty string is searched
        if (query.equals("")) {
            return masterItems;
        }

        ArrayList<MenuItem> filteredList = new ArrayList<>();
        for (MenuItem item : masterItems) {

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
        ArrayList<MenuItem> newFilterItems = filter(newText);
        adapter.animateTo(newFilterItems);
        filterView.scrollToPosition(0);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu, menu);

        menu.findItem(R.id.action_search).setVisible(true);
        final android.view.MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(false);


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        android.view.MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView sView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        sView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)sView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        View searchplate = sView.findViewById(android.support.v7.appcompat.R.id.search_plate);
        searchplate.setBackgroundResource(android.support.design.R.drawable.abc_textfield_search_default_mtrl_alpha);

        return true;
    }
}
