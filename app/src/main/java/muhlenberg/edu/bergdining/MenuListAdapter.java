package muhlenberg.edu.bergdining;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.menu_item_text);
            imageView = (ImageView) itemView.findViewById(R.id.menu_item_image);
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
        String name = menu.items.get(position).name;
        holder.textView.setText(name);

        Picasso.with(context)
                .load(menu.items.get(position).id)
                .resize(250, 250)
                .placeholder(R.drawable.ic_menu)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return menu.items.size();
    }
}
