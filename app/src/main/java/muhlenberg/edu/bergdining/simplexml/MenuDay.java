package muhlenberg.edu.bergdining.simplexml;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class MenuDay implements Serializable {

	public MenuDay() {
	}
	public MenuDay(String n) {
		this.name = n;
		meal = new ArrayList<MenuMeal>();
		meal.add(new MenuMeal("brk"));
		meal.add(new MenuMeal("lun"));
		meal.add(new MenuMeal("din"));
		
	}
	@Element
	public String name;

	@Element(required = false)
	public int count;

	@ElementList
	public ArrayList<MenuMeal> meal;
}
