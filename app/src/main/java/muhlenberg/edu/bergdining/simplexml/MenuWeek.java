package muhlenberg.edu.bergdining.simplexml;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="BergMenu")
public class MenuWeek  implements Serializable{

	public MenuWeek() {
		days = new ArrayList<MenuDay>();
		days.add(new MenuDay("monday"));
		days.add(new MenuDay("tuesday"));
		days.add(new MenuDay("wednesday"));
		days.add(new MenuDay("thursday"));
		days.add(new MenuDay("friday"));
		days.add(new MenuDay("saturday"));
		days.add(new MenuDay("sunday"));
	}
	@ElementList
	public ArrayList<MenuDay> days;
}
