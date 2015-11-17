package muhlenberg.edu.bergdining;


import muhlenberg.edu.bergdining.retro.WeeklyMenu;
import retrofit.Call;
import retrofit.Response;
import retrofit.http.GET;

/**
 * Created by Jalal on 11/15/2015.
 */
public interface BergServer {

    @GET("/menu.xml")
    Call<WeeklyMenu> getMenu();
}
