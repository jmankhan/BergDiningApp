package muhlenberg.edu.bergdining.retro;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Jalal on 11/15/2015.
 */
@Root(strict = false)
public class WeeklyMenu {

    @ElementList(entry = "weeklymenu", inline = true)
    List<MenuItem> items;

    public List<MenuItem> getItems() {
        return items;
    }
    public String toString() {
        String w = "";
        for(MenuItem m : items)
            w += m.toString() + "\n";
        return w;
    }
}