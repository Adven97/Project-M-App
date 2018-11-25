package advenstudios.prostamapav2;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
   // @GET("user")
   // Call<List<User>> all();

    @POST("user/register")
    Call create(@Body String login, @Body String pass);

    @POST("user/register")
    Call<User> createe(@Body User user);

    @GET("user/hello")
    Call<ResponseBody> hello();

    @GET("hello")
    Call<ResponseBody> hello2();

    @GET("user/test")
    Call<ResponseBody> qwe();
}
