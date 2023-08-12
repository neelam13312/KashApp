package in.games.GamesSattaBets.Retrofit;

import static in.games.GamesSattaBets.Config.BaseUrls.URL_NEWLOGIN;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Api {

    //--login and profile section api call---------------//

    @FormUrlEncoded
    @POST(URL_NEWLOGIN)
    Call<ResponseBody> getnewLogin(@Field("phone") String mobile,
                                   @Field("password") String otp);


}
