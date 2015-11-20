package muhlenberg.edu.bergdining.simplexml;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

public class MenuMeal implements Serializable{

	public MenuMeal() {
	}
	public MenuMeal(String n) {
		this.name = n;
		items = new ArrayList<MenuItem>();
	}
	
	@Attribute
	public String name;
	
	@ElementList(type=MenuItem.class)
	public ArrayList<MenuItem> items;
}
