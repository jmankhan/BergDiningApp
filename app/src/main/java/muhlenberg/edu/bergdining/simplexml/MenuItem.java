package muhlenberg.edu.bergdining.simplexml;

import android.util.Log;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="menuItem")
public class MenuItem {

	public MenuItem() {
		facts = new MenuFact();

	}

	@Element
	public String name;
	
	@Element
	public MenuFact facts; 
}
