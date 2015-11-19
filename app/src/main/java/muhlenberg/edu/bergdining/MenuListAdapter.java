package muhlenberg.edu.bergdining;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import muhlenberg.edu.bergdining.simplexml.MenuMeal;

/**
 * Created by BCrossLap on 11/17/2015.
 */
public class MenuListAdapter extends BaseAdapter {

    MenuMeal menu;
    Context context;
    DisplayMetrics dm;

    public MenuListAdapter(MenuMeal menu, Context context, DisplayMetrics dm) {
        this.menu = menu;
        this.context = context;
        this.dm = dm;
    }

    @Override
    public int getCount() {
        return menu.items.size();
    }

    @Override
    public Object getItem(int position) {
        return menu.items.get(position);
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

//        Picasso.with(context)
//                .load(menu.getItems().get(position).getImgId())
//                .placeholder(R.drawable.ic_menu)
//                .resize(250,250)
//                .into(img);


        TextView txt = (TextView) convertView.findViewById(R.id.menu_item_text);
        txt.setText(menu.items.get(position).name);

        return convertView;
    }
}
