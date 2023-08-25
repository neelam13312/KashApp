package in.games.Gameskash.Retrofit;



import static in.games.Gameskash.Config.BaseUrls.BASE_URL;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import in.games.Gameskash.Util.SessionMangement;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient retrofitClient;
    private static Retrofit retrofit;
    static SessionMangement sessionManagement;

    private OkHttpClient.Builder builder=new OkHttpClient.Builder();
//    private HttpLoggingInterceptor interceptor=new HttpLoggingInterceptor();


    public RetrofitClient() {
        Gson gson=new GsonBuilder()
                .setLenient()
                .create();

        builder.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().
                        addHeader ("Token",sessionManagement.getToken ().toString ()).build();
                Log.d ("dad7Dd", "intercept: "+sessionManagement.getToken ().toString ());
                return chain.proceed(request);
            }
        });
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        builder.addInterceptor(interceptor);
                 retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();




    }
    //eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJtb2JpbGUiOiI5OTk5OTk5OTk5IiwicGFzc3dvcmQiOiJNVEl6TkRVMiJ9.ZbrpfZusxQfFPWwynP0bKbl-QpUfWaF989EtUUa6Z5o
    public static  synchronized RetrofitClient getInstance(Context context){
        if (retrofitClient==null){
            retrofitClient=new RetrofitClient();
        }
        sessionManagement=new SessionMangement (context);
        return  retrofitClient;
    }

    public  Api getApi(){
        return  retrofit.create(Api.class);
    }
}
