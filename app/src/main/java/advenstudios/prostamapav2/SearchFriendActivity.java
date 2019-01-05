package advenstudios.prostamapav2;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ListView;
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


public class SearchFriendActivity extends AppCompatActivity {

    private ListView userList;
    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    UserAdapter adapter;

//    String[] ;
//    String[] lastNamee;
//    String[] friendMail;
//    String[] friendPassw;

    List<String> firstName;
    List<String> lastNamee;
    List<String> friendMail;
    List<String> friendPassw;

    User user;
    User userTest;
    ArrayList<User> userArrayList;
    TextView result;
    Button mmmButton;
    static String friendsEmail ="nikt@nikt.pl";
    int countFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        mmmButton = findViewById(R.id.newFriendsButton);
        connectionClass = new ConnectionClass();
        userList=(ListView)findViewById(R.id.userListId);
        result = (TextView) findViewById(R.id.rezultat);
        user=new User();
        progressDialog = new ProgressDialog(this);

        firstName = new ArrayList<String>();
        lastNamee= new ArrayList<String>();
        friendMail= new ArrayList<String>();
        friendPassw= new ArrayList<String>();

        DoQuery selectUsers = new DoQuery();
        selectUsers.execute("");

        userArrayList = new ArrayList<User>();
        userTest=new User("Andrzej", "Duda","prezydent@xd.pl", "janek123");
       //  userArrayList.add(userTest);

        adapter = new UserAdapter(this, userArrayList);
        userList.setAdapter(adapter);

      //  progressDialog.setMessage("Loading...");
       // progressDialog.show();

        Thread t = new Thread();
        try {
            t.sleep(3000);
           // progressDialog.hide();
            try{
                for(int i=0; i < countFriends;i++) {
                    userArrayList.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendPassw.get(i)));
                    adapter.notifyDataSetChanged();
                }

            }
            catch (Exception e){
                e.printStackTrace();
                result.setText(" c huj xd "+e.getMessage());
            }
            t.interrupt();
        } catch (InterruptedException e) {
        }
        User userTest2=new User("Swiety", "mikolaj","mikolaj303@gmail.com", "jp100");
      //   userArrayList.add(userTest2);
         userArrayList.add(new User("adam","tomczak","atomczak30@gmail.com","pppp"));



        userList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                friendsEmail = userArrayList.get(i).getEmail();
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));

            }
        });

    }


    public void dodajKogos(View view) {
        try{

            for(int i=0; i < countFriends;i++) {
                userArrayList.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendPassw.get(i)));
                adapter.notifyDataSetChanged();
            }

        }
        catch (Exception e){
            e.printStackTrace();
            result.setText(" c huj xd "+e.getMessage());
        }

    }

    /* public class SelectUsers extends AsyncTask<String,String,String>
    {
        String z="komunikat";

        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            //result.setText("Loading...");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                userArrayList.add(userTest);
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "Connection failed";
                    result.append("Connection failed");
                } else {

                    String query = " select * from user_db";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    user.setFirstName(rs.getString(2));
                    user.setLastName(rs.getString(3));
                    user.setEmail(rs.getString(4));
                    userArrayList.add(user);

                }

            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions: " + ex;
                result.setText(z);
            }

            return z;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }*/


    public class DoQuery extends AsyncTask<String,String,String>
    {

        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {

            String query="";
            //String emai = "kubica@blyskawica.com";

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "nie udalo sie  połaczyć niestety xD";
                }
                else {
//                    if(ps=="log") {
//                      //  query = " select * from friendss where my_email='" + em + "'";
//                        query = "select * from usrs2 where email in (select friends_email from friendss where my_email='" + em + "')";
//                    }
//                    else if(pss=="reg") {
//                      //  query = " select * from friendss where my_email='" + email + "'";
//                        query = "select * from usrs2 where email in (select friends_email from friendss where my_email='" + email + "')";
//                    }
                    query = "select * from usrs2 where email in (select friends_email from friendss where my_email='atomczak30@gmail.com')";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    countFriends=0;
                    while (rs.next())
                    {
                        firstName.add(rs.getString(2));
                        lastNamee.add(rs.getString(3));
                        friendMail.add(rs.getString(4));
                        friendPassw.add(rs.getString(5));
                        countFriends++;
                    }

                   // userArrayList.add(new User("Swiety", "mikolaj","mikolaj303@gmail.com", "jp100"));
                  //  adapter.notifyDataSetChanged();

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
        protected void onPostExecute(String s) {

            //  Toast.makeText(getBaseContext(),"bum "+z,Toast.LENGTH_LONG).show();


        }
    }


/*

    public class DoIt extends AsyncTask<String,String,String>
    {

        String z="komunikat";

        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                       z = "nie udalo sie  płaczyć niestety xD";
                } else {

                    String query = " select * from user_db where lastname='kubica'";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        firstName = rs.getString(2);
                        lastNamee = rs.getString(3);

                    }

                }
            }
            catch (Exception ex)
            {
                isSuccess = false;
                z = "Exceptions: "+ex;
            }
            result.setText(z);
            return z;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }*/

}
