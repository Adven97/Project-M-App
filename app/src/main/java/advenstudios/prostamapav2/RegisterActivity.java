package advenstudios.prostamapav2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.microsoft.windowsazure.mobileservices.*;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.table.TableOperationCallback;

public class RegisterActivity extends AppCompatActivity {

    Button mButton, cancelButton;
    TextView textView;
    Context context;
    Intent intent1;
    LocationManager locationManager ;
    boolean GpsStatus, statusChanged ;
    private MobileServiceClient mmClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context = getApplicationContext();
        mButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        textView = (TextView) findViewById(R.id.textView);

        /*Retrofit retrofit = new Retrofit.Builder()
              //  .baseUrl("https://tranquil-hollows-66538.herokuapp.com")
                .baseUrl("https://herku-test-postgres.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final UserService service = retrofit.create(UserService.class);*/

        try {
            mmClient = new MobileServiceClient("https://mylocapp.azurewebsites.net", this);
            User2 usr = new User2();
            usr.firstName = "elo";
            mmClient.getTable(User2.class).insert(usr, new TableOperationCallback<User2>() {
                @Override
                public void onCompleted(User2 entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null) {
                        textView.setText("SZTYWNIUTKO ");
                    } else {
                        textView.setText("CHUJOWIO ");
                    }
                }
            });
        }
        catch (Exception e){}



        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                EditText fName =(EditText) findViewById(R.id.fName);
                EditText name =(EditText)findViewById(R.id.lastName);
                EditText em =(EditText) findViewById(R.id.email);
                EditText pass =(EditText) findViewById(R.id.pass);
                EditText pass2 =(EditText) findViewById(R.id.pass2);

                final String firstName = fName.getText().toString();
                String lastName = name.getText().toString();
                String email = em.getText().toString();
                String password = pass.getText().toString();
                String password2 = pass2.getText().toString();





                //////////////INSERT TO DB /////////////

              /*  Call<ResponseBody> createCall = service.hello();
               // User user = new User(firstName,lastName,email,password);
               // Call<User> createCall = service.createe(user);
              //  Call<User> createCall = service.create(email,password);
                createCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> _, Response<ResponseBody> resp) {

                        try {
                           // User newUser = resp.body();
                            textView.setText("Created user " + resp.body().string());

                        } catch (Exception e) {
                            e.printStackTrace();
                            textView.setText(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> _, Throwable t) {
                        t.printStackTrace();
                        textView.setText("nie wyszlo " );
                    }
                });


              /*   User user = new User(email,password);
                 Call<User> createCall = service.createe(user);
                //  Call<User> createCall = service.create(email,password);
                createCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> _, Response<User> resp) {

                        try {
                             User newUser = resp.body();
                            textView.setText("Created user " + newUser.passwordHash);

                        } catch (Exception e) {
                            e.printStackTrace();
                            textView.setText(e.getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<User> _, Throwable t) {
                        t.printStackTrace();
                        textView.setText("nie wyszlo " );
                    }
                });       */

                ////////////////////////////////////////

                GPSStatus();
                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                if (GpsStatus == true) {

                    if (statusChanged) {
                        Thread t = new Thread();
                        try {
                            t.sleep(9000);
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            t.interrupt();
                        } catch (InterruptedException e) {}
                    }
                    else {

                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    }
                }

                else {

                    startActivity(intent1);
                    statusChanged=true;
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    public void GPSStatus(){
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

}
