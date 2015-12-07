package muhlenberg.edu.bergdining;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = inflater.inflate(R.layout.item_details, null, false);

            final AlertDialog dialog = new AlertDialog.Builder(context)
                    .setCustomTitle(itemView.findViewById(R.id.menu_item_detail_title))
                    .setView(customView)
                    .create();

            TextView name = (TextView) customView.findViewById(R.id.menu_item_detail_title);
            name.setText(item.facts.item_name);

            ImageView img = (ImageView) customView.findViewById(R.id.menu_item_detail_image);
            String image = item.facts.imageID;
            if (image != null && context.getResources().getIdentifier(image, "raw", context.getPackageName()) != 0) {
                InputStream is = context.getResources().openRawResource(context.getResources().getIdentifier(image,
                        "raw", context.getPackageName()));
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                img.setImageBitmap(bitmap);
            }

            TextView desc = (TextView) customView.findViewById(R.id.menu_item_description);
            desc.setText(item.facts.item_desc);

            TextView cal = (TextView) customView.findViewById(R.id.menu_item_cal_text);
            cal.setText(item.facts.calories);

            TextView fat = (TextView) customView.findViewById(R.id.menu_item_fat_text);
            fat.setText(item.facts.fat);

            TextView satfat = (TextView) customView.findViewById(R.id.menu_item_satfat_text);
            satfat.setText(item.facts.satfat);

            TextView transfat = (TextView) customView.findViewById(R.id.menu_item_transfat_text);
            transfat.setText(item.facts.transfat);

            TextView chol = (TextView) customView.findViewById(R.id.menu_item_chol_text);
            chol.setText(item.facts.chol);

            TextView carbs = (TextView) customView.findViewById(R.id.menu_item_carbs_text);
            carbs.setText(item.facts.carbo);

            TextView sugar = (TextView) customView.findViewById(R.id.menu_item_sugar_text);
            sugar.setText(item.facts.sugars);

            TextView protein = (TextView) customView.findViewById(R.id.menu_item_protein_text);
            protein.setText(item.facts.protein);

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
        View v = LayoutInflater.from(context).inflate(R.layout.menu_item, parent, false);
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
