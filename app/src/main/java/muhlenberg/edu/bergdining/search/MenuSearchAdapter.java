package muhlenberg.edu.bergdining.search;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.method.TextKeyListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import muhlenberg.edu.bergdining.R;
import muhlenberg.edu.bergdining.simplexml.MenuItem;

/**
 * Created by Jalal on 12/2/2015.
 */
public class MenuSearchAdapter extends RecyclerView.Adapter<MenuSearchAdapter.CustomSearchViewHolder>{

    ArrayList<MenuItem> menu;
    Context context;

    public MenuSearchAdapter(Context context, ArrayList<MenuItem> menu) {
        this.context = context;
        this.menu = menu;
    }

    @Override
    public CustomSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.menu_search_item, parent, false);

        return new CustomSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomSearchViewHolder holder, int position) {
        MenuItem item = menu.get(position);
        String day = item.facts.day.replaceFirst(item.facts.day.substring(0, 1),
                item.facts.day.substring(0, 1).toUpperCase());
        String meal = "";
        if (item.facts.meal.equalsIgnoreCase("brk"))
            meal = "Breakfast";
        else if (item.facts.meal.equalsIgnoreCase("lun"))
            meal = "Lunch";
        else if (item.facts.meal.equalsIgnoreCase("din"))
            meal = "Dinner";
        String details = "" + day + " " + meal + " at " + item.facts.station;

        holder.titleView.setText(item.name);
        holder.detailView.setText(details);
    }

    @Override
    public int getItemCount() {
        return menu.size();
    }

    public MenuItem remove(int position) {
        final MenuItem item = menu.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void add(int position, MenuItem item) {
        menu.add(position, item);
        notifyItemInserted(position);
    }

    public void move(int fromPosition, int toPosition) {
        final MenuItem item = menu.remove(fromPosition);
        menu.add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(ArrayList<MenuItem> menu) {
        applyAndAnimateRemovals(menu);
        applyAndAnimateAdditions(menu);
        applyAndAnimateMovedItems(menu);

    }

    private void applyAndAnimateRemovals(ArrayList<MenuItem> newMenu) {
        for (int i = menu.size() - 1; i >= 0; i--) {
            final MenuItem item = menu.get(i);
            if (!newMenu.contains(item)) {
                remove(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<MenuItem> newMenu) {
        for (int i = 0, count = newMenu.size(); i < count; i++) {
            final MenuItem item = newMenu.get(i);
            if (!menu.contains(item)) {
                add(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<MenuItem> newMenu) {
        for (int toPosition = newMenu.size() - 1; toPosition >= 0; toPosition--) {
            final MenuItem item = newMenu.get(toPosition);
            final int fromPosition = menu.indexOf(item);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                move(fromPosition, toPosition);
            }
        }
    }


    public class CustomSearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleView, detailView;

        public CustomSearchViewHolder(View itemView) {
            super(itemView);

            int itemsOnScreen = 10;
            DisplayMetrics dm = new DisplayMetrics();
            ((MenuSearchActivity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);

            CardView cardView = (CardView) itemView.findViewById(R.id.menu_search_item_layout);
            cardView.setMinimumHeight(dm.heightPixels / itemsOnScreen);
            cardView.setOnClickListener(this);

            titleView = (TextView) itemView.findViewById(R.id.menu_search_item_title);
            detailView = (TextView) itemView.findViewById(R.id.menu_search_item_detail);




        }

        @Override
        public void onClick(View v) {
            //navigate to menu item in real menu
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Clicked search item!")
                    .create().show();

        }
    }
}
