package advenstudios.prostamapav2;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

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
import java.util.List;

import static advenstudios.prostamapav2.LoginActivity.em;
import static advenstudios.prostamapav2.LoginActivity.ps;
import static advenstudios.prostamapav2.RegisterActivity.email;
import static advenstudios.prostamapav2.RegisterActivity.pss;


public class RequestsActivity extends AppCompatActivity {

    private ListView userListaa;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    UserAdapter adapterx;

    List<String> firstNamex;
    List<String> lastNameex;
    List<String> friendMailx;
    List<String> friendPasswx;

    User user;
    ArrayList<User> userArrayListaa;
    TextView result;
    static String friendsEmail ="nikt@nikt.pl";
    int countFriends;

    ArrayAdapter<String> adapterek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        connectionClass = new ConnectionClass();
        userListaa=(ListView)findViewById(R.id.userListIddd);
        //result = (TextView) findViewById(R.id.rezultattt);
        user=new User();
        progressDialog = new ProgressDialog(this);

        firstNamex = new ArrayList<String>();
        lastNameex= new ArrayList<String>();
        friendMailx= new ArrayList<String>();
        friendPasswx= new ArrayList<String>();


        userArrayListaa = new ArrayList<User>();

        //  userArrayList.add(userTest);

        adapterx = new UserAdapter(this, userArrayListaa);

        adapterek = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,firstNamex);

        userListaa.setAdapter(adapterx);


        DoQuery selectUsers = new DoQuery();
        selectUsers.execute("");

        Thread t = new Thread();
        try {
            t.sleep(3500);
            // progressDialog.hide();
            try {
                for (int i = 0; i < countFriends; i++) {
                    userArrayListaa.add(new User(firstNamex.get(i), lastNameex.get(i), friendMailx.get(i), friendPasswx.get(i)));
                    adapterx.notifyDataSetChanged();
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.setText(" c huj xd " + e.getMessage());
            }
            t.interrupt();
        } catch (InterruptedException e) {}


        MapsActivity.progressDialogfromMap.hide();

        userListaa.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {

                    try {
                        Connection conon = connectionClass.CONN();
                        if (conon == null) {

                            Toast.makeText(getBaseContext(), "nie udalo sie  połaczyć niestety xD", Toast.LENGTH_LONG).show();
                        } else {
                            String query = "";
                            if (ps == "log") {
                                //  query = " select * from friendss where my_email='" + em + "'";
                                query = "update friendss set accepted='true' where friends_email='"+em+"' and my_email = '" + userArrayListaa.get(i).getEmail() + "'";
                            } else if (pss == "reg") {
                                //  query = " select * from friendss where my_email='" + email + "'";
                                query = "update friendss set accepted='true' where friends_email='"+email+"' and my_email = '" + userArrayListaa.get(i).getEmail() + "'";
                            }

                            Statement stmt = conon.createStatement();
                            stmt.executeUpdate(query);

                            Toast.makeText(getBaseContext(), "Dodano znajomego do bazy ", Toast.LENGTH_LONG).show();
                            //SearchFriendActivity.userArrayList.add(new User(firstNamex.get(i), lastNameex.get(i), friendMailx.get(i), friendPasswx.get(i)));

                        }
                    } catch (Exception ex) {
                        Toast.makeText(getBaseContext(), "Exception wyjebalo: " + ex, Toast.LENGTH_LONG).show();
                    }
                }
        });

    }




    public class DoQuery extends AsyncTask<String,String,String>
    {

        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {

            String query="";

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "nie udalo sie  połaczyć niestety xD";
                }
                else {
                    if(ps=="log") {
                        query = "select * from usrs2 where email in( select my_email from friendss where friends_email='" + em + "' and accepted='false')";
                    }
                    else if(pss=="reg") {
                        query = "select * from usrs2 where email in( select my_email from friendss where friends_email='" + email + "' and accepted='false')";
                    }


                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    countFriends=0;
                    while (rs.next())
                    {
                        firstNamex.add(rs.getString(2));
                        lastNameex.add(rs.getString(3));
                        friendMailx.add(rs.getString(4));
                        friendPasswx.add(rs.getString(6));
                        countFriends++;
                    }

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = "Exception wyjebalo: "+ex;
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {}

    }

}
