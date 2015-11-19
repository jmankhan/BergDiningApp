package muhlenberg.edu.bergdining;


import muhlenberg.edu.bergdining.simplexml.MenuWeek;
import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Jalal on 11/15/2015.
 */
public interface BergServer {

    @GET("/berg")
    Call<MenuWeek> getMenu();
}
