package advenstudios.prostamapav2;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    Button  rButton, lButton, mButton;
    Context context;
    Intent intent1;
    TextView textview;
    LocationManager locationManager ;
    boolean GpsStatus, statusChanged ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act);

        textview = (TextView)findViewById(R.id.textView);
        context = getApplicationContext();

        mButton = findViewById(R.id.mybutton);
        rButton = findViewById(R.id.registerButton);
        lButton = findViewById(R.id.loginButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                GPSStatus();
                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                if (GpsStatus == true) {

                    if (statusChanged) {
                       // textview.setText("WAIT");
                        Thread t = new Thread();
                        try {
                            t.sleep(9000);
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                            t.interrupt();
                        } catch (InterruptedException e) {}


                    }
                    else {
                        textview.setText("Welcome to (M)App ! ");
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    }
                }

                else {
                      textview.setText("Location Services Is Disabled, Please turn it on ");
                      startActivity(intent1);
                      statusChanged=true;
                    }

                }

        });

        rButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        lButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

        public void GPSStatus(){
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

}
