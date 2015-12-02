package muhlenberg.edu.bergdining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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
            View newView = itemView.findViewById(R.id.item_detail_main);
            String message = "Calories:\t" + item.facts.calories + "\n"
                    + "Carbs:\t" + item.facts.carbo + "\n"
                    + "Fat:\t" + item.facts.fat;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setCustomTitle(itemView.findViewById(R.id.menu_item_detail_title))
                    .setView(inflater.inflate(R.layout.item_details, null, false))
                    .create();


            dialog.show();
            dialog.findViewById(R.id.item_detail_main).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
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
            holder.imageView.getDrawable().setColorFilter(Color.rgb(237, 231, 159), PorterDuff.Mode.MULTIPLY);

        else if(menu.items.get(position).facts.station.contains("Crouton"))
            holder.imageView.getDrawable().setColorFilter(Color.rgb(0, 231, 0), PorterDuff.Mode.MULTIPLY);

        else if(menu.items.get(position).facts.station.contains("Mangia"))
            holder.imageView.getDrawable().setColorFilter(Color.rgb(237, 131, 0), PorterDuff.Mode.MULTIPLY);

        else if(menu.items.get(position).facts.station.contains("Magellan"))
            holder.imageView.getDrawable().setColorFilter(Color.rgb(237, 0, 237), PorterDuff.Mode.MULTIPLY);

        else if(menu.items.get(position).facts.station.contains("Chef"))
            holder.imageView.getDrawable().setColorFilter(Color.rgb(237, 237, 237), PorterDuff.Mode.MULTIPLY);

        else
            holder.imageView.getDrawable().setColorFilter(Color.rgb(0, 0, 0), PorterDuff.Mode.MULTIPLY);
    }

    @Override
    public int getItemCount() {
        return menu.items.size();
    }
}
