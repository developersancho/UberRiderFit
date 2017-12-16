package developersancho.uberriderfit.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by developersancho on 16.12.2017.
 */

public interface IGoogleAPI {

    @GET
    Call<String> getPath(@Url String url);

}
