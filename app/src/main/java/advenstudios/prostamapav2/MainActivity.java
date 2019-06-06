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

    Button mButton;
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
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                GPSStatus();
                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                if (GpsStatus == true) {

                    startActivity(new Intent(context, MapsActivity.class));
                }
                else {
                      textview.setText("Location Services Is Disabled, Please turn it on ");
                      startActivity(intent1);
                    }
                }
        });
    }

        public void GPSStatus(){
            locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
}
