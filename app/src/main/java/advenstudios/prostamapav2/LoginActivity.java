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
import java.sql.ResultSet;
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
    ConnectionClass connectionClass;

   static String ps;
   static String em=" ";

    boolean openedLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        mButton = findViewById(R.id.confirmButton);
        cancelButton = findViewById(R.id.cancelButton);
        textView = (TextView) findViewById(R.id.textView2);

        connectionClass = new ConnectionClass();
       // openedLogin=false;

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

                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

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

        String z="komunikat";

        boolean isSuccess=false;

        @SuppressLint("NewApi")
        public Connection konik() {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Connection conn = null;
            String ConnURL;
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnURL =String.format("jdbc:jtds:sqlserver://adamserver2137.database.windows.net:1433/Users;" +
                        "user=adamserver2137@adamserver2137;password=#zbigniewstonoga1;encrypt=true;" +
                        "trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

                conn = DriverManager.getConnection(ConnURL);

            } catch (SQLException e) {
                e.printStackTrace();
                     z="chuj "+e.getMessage().toString();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                 z="chuj 2  "+e.getMessage();

            } catch (Exception e) {
                e.printStackTrace();
                z=" c h uj 434 "+e.getMessage();
            }
            //Toast.makeText(getBaseContext(),""+wiad,Toast.LENGTH_LONG).show();
            return conn;
        }


        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

                try {
                    Connection con = konik();
                    if (con == null) {
                     //   z = "nie udalo sie  płaczyć niestety xD";
                    }
                    else {
                        if (email.length()>0 && password.length()>0) {

                            String query = " select * from usrs2 where email='" + email + "' and PASWORD = '" + password + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            while (rs.next())
                            {
                                em = rs.getString(4);
                                ps = rs.getString(5);

                                if (em.equals(email) && ps.equals(password)) {
                                    isSuccess = true;
                                    ps="log";
                                    z = "Zalogowano pomyślnie";
                                    openedLogin=true;
                                    GPSStatus();
                                    if (GpsStatus == true) {

                                        if (statusChanged) {
                                            Thread t = new Thread();
                                         try {
                                             t.sleep(9000);
                                             startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                             t.interrupt();
                                            }
                                            catch (InterruptedException e) {}

                                        }
                                        else {

                                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                                        }
                                    }
                                    else {
                                        startActivity(intent1);
                                        statusChanged = true;
                                    }
                                }
                                else
                                    z = "Nie udało się zalogować";
                                isSuccess = false;
                            }
                        }
                        else{
                            z = "Pola nie mogą być puste";
                        }
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


