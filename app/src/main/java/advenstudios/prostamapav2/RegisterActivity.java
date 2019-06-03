//package advenstudios.prostamapav2;
//
//import android.annotation.SuppressLint;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.location.LocationManager;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.StrictMode;
//import android.provider.Settings;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//
//public class RegisterActivity extends AppCompatActivity {
//
//   // TextView textView;
//    Button mButton, cancelButton;
//    Context context;
//    Intent intent1;
//    LocationManager locationManager;
//    boolean GpsStatus, statusChanged;
//    ProgressDialog progressDialog;
//    static String email, password, finame, lastNme, passw2;
//    ConnectionClass connectionClass;
//    boolean openedReg;
//    static String pss;
//    //static String pss;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        context = getApplicationContext();
//        mButton = findViewById(R.id.confirmButton);
//        cancelButton = findViewById(R.id.cancelButton);
//        //textView = (TextView) findViewById(R.id.textView);
//
//        connectionClass = new ConnectionClass();
//        //  getSupportActionBar().hide();
//        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//        //      WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        progressDialog = new ProgressDialog(this);
//        openedReg=false;
//
//        mButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//
//                Doregister doregister = new Doregister();
//                doregister.execute("");
//
//                EditText fname = (EditText) findViewById(R.id.fName);
//                EditText lastname = (EditText) findViewById(R.id.lastName);
//                EditText mail = (EditText) findViewById(R.id.email);
//                EditText pass = (EditText) findViewById(R.id.pass);
//                EditText pass2 = (EditText) findViewById(R.id.pass2);
//
//                finame = fname.getText().toString();
//                lastNme = lastname.getText().toString();
//                email = mail.getText().toString();
//                password = pass.getText().toString();
//                passw2 = pass2.getText().toString();
//
//                //////////////INSERT TO DB /////////////
//
//
//                intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });
//
//    }
//
//    public void GPSStatus() {
//        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//
//    public class Doregister extends AsyncTask<String, String, String> {
//
//        String z = "";
//        boolean isSuccess = false;
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            try {
//                Connection con = connectionClass.CONN();
//                if (con == null) {
//                    z = "Nie udało się połączyć";
//                } else {
//                    if (finame.length() > 0 && lastNme.length() > 0 && email.length() > 0 && password.length() > 0 && passw2.length() > 0) {
//                        if (password.equals(passw2)) {
//                            if (password.length() >= 8) {
//                                 if(isAvailable()){
//                                String query = "INSERT INTO usrs2 VALUES ('" + finame + "', '" + lastNme + "','" + email + "', '" + password + "', 'false', 66.788974, 9.457503)";
//                                //String query="INSERT INTO TodoItem (firstName, lastName) VALUES ('email', 'password+')";
//                                 GPSStatus();
//                                if (GpsStatus == true) {
//
//                                    Statement stmt = con.createStatement();
//                                    stmt.executeUpdate(query);
//                                    openedReg=true;
//                                    pss="reg";
//                                    z = "Rejestracja pomyślna";
//
//
//                                    progressDialog.setMessage("Łąduję Mapę, proszę czekać!");
//                                    progressDialog.show();
//
//                                    if (statusChanged) {
//                                        progressDialog.setMessage("Łąduję Mapę, proszę czekać!");
//                                        progressDialog.show();
//                                        Thread t = new Thread();
//                                        try {
//                                            t.sleep(10000);
//                                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//                                            t.interrupt();
//                                        } catch (InterruptedException e) {
//                                        }
//                                    } else {
//
//                                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
//                                    }
//                                } else {
//
//                                    startActivity(intent1);
//                                    statusChanged = true;
//                                }
//                            }
//                                 else {
//                                     z = "Ten email jest juz zajety";
//                                 }
//                            }
//                            else {
//                                z = "Hasło musi mieć minimum 8 znaków";
//                            }
//                        } else {
//                            z = "Hasła są nie zgodne";
//                        }
//                    } else {
//                        z = "Pola nie mogą być puste";
//                    }
//
//                }
//            } catch (Exception ex) {
//                isSuccess = false;
//                z = "Exceptions: " + ex;
//            }
//            // textView.setText(z);
//            return z;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            Toast.makeText(getBaseContext(), "" + z, Toast.LENGTH_LONG).show();
//
//            if (isSuccess) {
//                //startActivity(new Intent(MainActivity.this,Main2Activity.class));
//                Toast.makeText(getBaseContext(), "Witamy w MApp", Toast.LENGTH_LONG).show();
//            }
//
//            progressDialog.hide();
//        }
//
//        boolean isAvailable() {
//
//            String em="nic";
//            boolean av=true;
//            try {
//                Connection con = connectionClass.CONN();
//                if (con == null) {
//                   // z = "Nie udało się połączyć";
//                } else {
//                    String query = " select * from usrs2 where email='" + email + "'";
//                    Statement stmt = con.createStatement();
//                    ResultSet rs = stmt.executeQuery(query);
//
//                    if(rs==null){
//                        av=true;
//                    }
//                    else{
//                       while (rs.next()) {
//                            em = rs.getString(4);
//                            //    ps = rs.getString(5);
//
//                            if (em.equals(email)) {
//                                av=false;
//                            }
//                            else
//                                av=true;
//                        }
//                    }
//                }
//
//            }
//            catch (Exception ex){}
//
//            return av;
//        }
//    }
//}
//
//
