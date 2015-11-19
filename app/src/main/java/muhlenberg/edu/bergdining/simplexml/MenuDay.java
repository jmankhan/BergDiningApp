package muhlenberg.edu.bergdining.simplexml;

import android.util.Log;

import java.util.ArrayList;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class MenuDay {

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
	
	@ElementList
	public ArrayList<MenuMeal> meal;
}
