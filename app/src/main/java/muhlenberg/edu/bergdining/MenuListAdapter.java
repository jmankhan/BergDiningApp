package muhlenberg.edu.bergdining;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import muhlenberg.edu.bergdining.simplexml.MenuItem;
import muhlenberg.edu.bergdining.simplexml.MenuMeal;

/**
 * Created by BCrossLap on 11/17/2015.
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.CustomViewHolder> {

    Context context;
    MenuMeal menu;

    public MenuListAdapter(Context context, MenuMeal menu) {
        this.context = context;
        this.menu = menu;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;
        LinearLayout layout;
        int index = -1;

        public CustomViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.menu_item_text);
            imageView = (ImageView) itemView.findViewById(R.id.menu_item_image);
            layout = (LinearLayout) itemView.findViewById(R.id.menu_item_layout);
            layout.setOnClickListener(this);
         }

        @Override
        public void onClick(View v) {
            MenuItem item = menu.items.get(index);
            String message = "Calories:\t" + item.facts.calories + "\n"
                    + "Carbs:\t" + item.facts.carbo + "\n"
                    + "Fat:\t" + item.facts.fat;
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Details - " + item.name)
                    .setMessage(message)
                    .create();

            dialog.show();
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.menu_item_layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        holder.index = position;

        String name = menu.items.get(position).name;
        holder.textView.setText(name);

        Picasso.with(context)
                .load(menu.items.get(position).id)
                .resize(144, 144)
                .placeholder(R.drawable.ic_menu)
                .into(holder.imageView);

        if(menu.items.get(position).facts.station.contains("Wildfire"))
            holder.imageView.setBackgroundResource(R.drawable.corner);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.imageView.setBackground(null);
        }

    }

    @Override
    public int getItemCount() {
        return menu.items.size();
    }
}
