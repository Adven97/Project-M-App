package advenstudios.prostamapav2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button mButton, cancelButton;
    Context context;
    Intent intent1;
    LocationManager locationManager ;
    boolean GpsStatus, statusChanged ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        mButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

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
