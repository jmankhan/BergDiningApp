package muhlenberg.edu.bergdining.retro;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;
import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Created by Jalal on 11/16/2015.
 */
@Root(strict = false)
@Convert(value = MenuItem.MenuItemConverter.class)
public class MenuItem {

    @Element(name = "item_name")
    String name;
    Map<String, String> nuts;
    int imgId;

    public MenuItem() {
        name = "undefined";
        nuts = new HashMap<>();
    }

    public void setImgId(int imgId){this.imgId = imgId;}
    public int getImgId() {return imgId;}
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        String i = "";
        Iterator<String> it = nuts.keySet().iterator();
        while(it.hasNext()) {
            String key = it.next();
            i += key + ":" + nuts.get(key) + " ";
        }

        return i;

    }

    public static class MenuItemConverter implements Converter<MenuItem> {

        @Override
        public MenuItem read(InputNode node) throws Exception {
            MenuItem item = new MenuItem();
            item.name = node.getAttribute("item_name").getValue();
            Iterator<String> it = node.getAttributes().iterator();
            while(it.hasNext()) {
                String key = it.next();
                item.nuts.put(key, node.getAttribute(key).getValue());
            }


            return item;
        }

        @Override
        public void write(OutputNode node, MenuItem value) throws Exception {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
