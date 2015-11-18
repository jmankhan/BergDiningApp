package muhlenberg.edu.bergdining;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import muhlenberg.edu.bergdining.retro.MenuItem;
import muhlenberg.edu.bergdining.retro.WeeklyMenu;

/**
 * Created by BCrossLap on 11/17/2015.
 */
public class MenuListAdapter extends BaseAdapter {

    WeeklyMenu menu;
    Context context;

    public MenuListAdapter(WeeklyMenu menu, Context context) {
        this.menu = menu;
        this.context = context;
    }

    @Override
    public int getCount() {
        return menu.getItems().size();
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
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.menu_item_layout, parent, false);
        }

        ImageView img = (ImageView) convertView.findViewById(R.id.menu_item_image);
        img.setImageResource(R.drawable.ic_menu);

        TextView txt = (TextView) convertView.findViewById(R.id.menu_item_text);
        txt.setText(menu.getItems().get(position+position%3).getName());

        return convertView;
    }
}
