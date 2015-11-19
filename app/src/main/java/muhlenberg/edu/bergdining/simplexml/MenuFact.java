package muhlenberg.edu.bergdining.simplexml;

import org.simpleframework.xml.Attribute;

public class MenuFact {

	public MenuFact() {

	}
	@Attribute(name = "day")
	public String day;

	@Attribute(name = "meal")
	public String meal;

	@Attribute(name = "station")
	public String station;

	@Attribute(name = "item_name")
	public String item_name;

	@Attribute(name = "item_desc")
	public String item_desc;

	@Attribute(name = "serv_size")
	public String serv_size;

//	@Attribute(name = "veg_type")
//	public String veg_type;

	@Attribute(name = "allergens")
	public String allergens;

	@Attribute(name = "calories")
	public String calories;

	@Attribute(name = "fat")
	public String fat;

	@Attribute(name = "fat_pct_dv")
	public String fat_pct_dv;

	@Attribute(name = "calfat")
	public String calfat;

	@Attribute(name = "satfat")
	public String satfat;

	@Attribute(name = "satfat_pct_dv")
	public String satfat_pct_dv;

	@Attribute(name = "transfat")
	public String transfat;

	@Attribute(name = "chol")
	public String chol;

	@Attribute(name = "chol_pct_dv")
	public String chol_pct_dv;

	@Attribute(name = "sodium")
	public String sodium;

	@Attribute(name = "sodium_pct_dv")
	public String sodium_pct_dv;

	@Attribute(name = "carbo")
	public String carbo;

	@Attribute(name = "carbo_pct_dv")
	public String carbo_pct_dv;

	@Attribute(name = "dfib")
	public String dfib;

	@Attribute(name = "dfib_pct_dv")
	public String dfib_pct_dv;

	@Attribute(name = "sugars")
	public String sugars;

	@Attribute(name = "protein")
	public String protein;

	@Attribute(name = "vita_pct_dv")
	public String vita_pct_dv;

	@Attribute(name = "vitc_pct_dv")
	public String vitc_pct_dv;

	@Attribute(name = "calcium_pct_dv")
	public String calcium_pct_dv;

	@Attribute(name = "iron_pct_dv")
	public String iron_pct_dv;

	public String id;
}
