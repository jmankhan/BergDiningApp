package muhlenberg.edu.bergdining;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import muhlenberg.edu.bergdining.retro.WeeklyMenu;

/**
 * Created by BCrossLap on 11/17/2015.
 */
public class MenuListAdapter extends BaseAdapter {

    WeeklyMenu menu;
    Context context;
    DisplayMetrics dm;

    public MenuListAdapter(WeeklyMenu menu, Context context, DisplayMetrics dm) {
        this.menu = menu;
        this.context = context;
        this.dm = dm;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int position) {
        return menu.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item_layout, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.menu_item_image);
        ViewGroup.LayoutParams lp = img.getLayoutParams();
        lp.width = dm.widthPixels / 3;

        Picasso.with(context)
                .load(menu.getItems().get(position).getImgId())
                .placeholder(R.drawable.ic_menu)
                .resize(150,150)
                .into(img);


        TextView txt = (TextView) convertView.findViewById(R.id.menu_item_text);
        txt.setText(menu.getItems().get(position + position % 3).getName());

        return convertView;
    }
}
