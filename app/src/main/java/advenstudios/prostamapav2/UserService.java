package advenstudios.prostamapav2;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserService {
    @GET("users")
    Call<List<User>> all();

    @POST("users/new")
    Call<User> create(@Body User user);
}
