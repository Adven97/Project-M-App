package advenstudios.prostamapav2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {

    TextView textView;
    Button mButton, cancelButton;
    Context context;
    Intent intent1;
    LocationManager locationManager ;
    boolean GpsStatus, statusChanged ;
    ProgressDialog progressDialog;
    String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        mButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        textView = (TextView) findViewById(R.id.textView2);

        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressDialog=new ProgressDialog(this);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Doregister doregister = new Doregister();
                doregister.execute("");

                EditText mail = (EditText)findViewById(R.id.editText);
                EditText pass = (EditText)findViewById(R.id.editText2);

                email = mail.getText().toString();
                password = pass.getText().toString();

                //////////////INSERT TO DB /////////////

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

    public class Doregister extends AsyncTask<String,String,String>
    {
        String wiad="tem";
        String z="";


        @SuppressLint("NewApi")
        public Connection CONN() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                 ConnURL =String.format("jdbc:jtds:sqlserver://adamserver2137.database.windows.net:1433/Users;user=adamserver2137@adamserver2137;password=#zbigniewstonoga1;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");
               /// ConnURL =String.format("jdbc:jtds:sqlserver://newserver209486.database.windows.net:1433/Users2;user=adven97@newserver209486;password=#adam997;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

                // conn = DriverManager.getConnection(url);
                conn = DriverManager.getConnection(ConnURL);

            } catch (SQLException e) {
                e.printStackTrace();
                textView.setText("cjuj1 "+e.getMessage().toString());
                //   wiad=se.getMessage().toString();
                //  progressDialog.setMessage("chujnia :"+se.getMessage().toString());
                //   progressDialog.show();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                textView.setText("chuj 2  "+e.getMessage());
                //  wiad=e.getMessage().toString();
                //  progressDialog.setMessage("chujnia 2:"+e.getMessage().toString());
                //  progressDialog.show();

            } catch (Exception e) {
                e.printStackTrace();
                textView.setText(" c h uj 434 "+e.getMessage());
                //  wiad=e.getMessage().toString();
                // progressDialog.setMessage("chujnia 3:"+e.getMessage().toString());
                //   progressDialog.show();
            }
            //Toast.makeText(getBaseContext(),""+wiad,Toast.LENGTH_LONG).show();
            return conn;
        }

        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

                try {
                    Connection con = CONN();
                    if (con == null) {
                        z = "Wykurwiaj :D";
                    }
                    else {
                           String query="INSERT INTO TodoItem (firstName, lastName) VALUES ('"+email+"', '"+password+"')";
                          Statement stmt = con.createStatement();
                            stmt.executeUpdate(query);

                        z = "Register successfull";
                        //  isSuccess=true;

                    }
                }
                catch (Exception ex)
                {
                    isSuccess = false;
                    z = "Exceptions: "+ex;
                }
            textView.setText(z);
            return z;
        }

        @Override
        protected void onPostExecute(String s) {

            Toast.makeText(getBaseContext(),""+z,Toast.LENGTH_LONG).show();

            if(isSuccess) {
                //startActivity(new Intent(MainActivity.this,Main2Activity.class));
                Toast.makeText(getBaseContext(),"nooo jest git jest git ksieciu",Toast.LENGTH_LONG).show();
            }

            progressDialog.hide();
        }
    }

}


