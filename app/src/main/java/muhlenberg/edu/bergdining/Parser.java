package muhlenberg.edu.bergdining;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jalal on 11/15/2015.
 */
public class Parser {

    ArrayList<Item> items;
    public Parser(InputStream is) throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(is, null);
        parser.nextTag();

        items = new ArrayList<>();

        int eventType;
        while((eventType = parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();

            if(name != null && name.equalsIgnoreCase("weeklymenu") && eventType != XmlPullParser.END_TAG) {
                Item i = new Item(parser.getAttributeValue(null, "item_name"));
                items.add(i);
            }

            parser.nextToken();
        }
    }

    static class Item {
        String name, meal;
        Map<String, String> nuts;

        public Item(String name) {
            this.name = name;
            meal = null;
            nuts = new HashMap<>();
        }
    }

    public ArrayList<Item> getItems() {
        return items;
    }
}
