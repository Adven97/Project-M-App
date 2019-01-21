package advenstudios.prostamapav2;

import android.content.Intent;
import android.graphics.Color;
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
//import static advenstudios.prostamapav2.MapsActivity.already1;
import static advenstudios.prostamapav2.MapsActivity.activeUsr;
import static advenstudios.prostamapav2.RegisterActivity.email;
import static advenstudios.prostamapav2.RegisterActivity.pss;
import static advenstudios.prostamapav2.UserAdapter.kolorek;


public class SearchFriendActivity extends AppCompatActivity {

   // private ListView userList;
    private ListView userList2;

    ConnectionClass connectionClass;
    ProgressDialog progressDialog;
    UserAdapter adapter;

    private ListView userList3;
    UserAdapter adapter2;
    ArrayList<User> userArrayList2;

    List<String> firstName, firstName2;
    List<String> lastNamee, lastNamee2;
    List<String> friendMail, friendMail2;
    List<String> friendStatus, friendStatus2;

    User user;
    static ArrayList<User> userArrayList;

    TextView result;
    //Button mmmButton;
    static String friendsEmail ="nikt@nikt.pl";
    int countFriends;
    int countFriends2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
      //  mmmButton = findViewById(R.id.newFriendsButton);
        connectionClass = new ConnectionClass();
      //  userList=(ListView)findViewById(R.id.userListId);
        userList2=(ListView)findViewById(R.id.userListId);
        userList3=(ListView)findViewById(R.id.userListId2);
        //result = (TextView) findViewById(R.id.rezultat);
        user=new User();
        progressDialog = new ProgressDialog(this);

        firstName = new ArrayList<String>();
        lastNamee= new ArrayList<String>();
        friendMail= new ArrayList<String>();
        friendStatus= new ArrayList<String>();

        firstName2 = new ArrayList<String>();
        lastNamee2= new ArrayList<String>();
        friendMail2= new ArrayList<String>();
        friendStatus2= new ArrayList<String>();

        DoQuery selectUsers = new DoQuery();
        selectUsers.execute("");


        userArrayList = new ArrayList<User>();
        adapter = new UserAdapter(this, userArrayList);
        userList2.setAdapter(adapter);

        userArrayList2 = new ArrayList<User>();
        adapter2 = new UserAdapter(this, userArrayList2);
        userList3.setAdapter(adapter2);

        Thread t = new Thread();
        try {
            t.sleep(5000);
           // progressDialog.hide();
            try{
                for(int i=0; i < countFriends;i++) {
                    if(friendStatus.get(i).equals("true")) {
                        userArrayList.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendStatus.get(i)));
                        adapter.notifyDataSetChanged();
                        userList2.setBackgroundColor(Color.parseColor("#ADFF2F"));
                    }
                    else {
                        userArrayList2.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendStatus.get(i)));
                        adapter2.notifyDataSetChanged();
                        userList3.setBackgroundColor(Color.parseColor("#FF0000"));
                    }
                }
//                for(int i=0; i < countFriends;i++) {
//                    userArrayList2.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendStatus.get(i)));
//                    adapter2.notifyDataSetChanged();
//                    userList3.setBackgroundColor(Color.parseColor("#ff4400"));
//                }
                //userList.getChildAt(0).setBackgroundColor(Color.parseColor("#FF4500"));
            }
            catch (Exception e){
                e.printStackTrace();
                result.setText("eror "+e.getMessage());
            }
            t.interrupt();
        } catch (InterruptedException e) {
        }
        MapsActivity.progressDialogfromMap.hide();

        userList2.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                if(activeUsr ) {
                   // if (userArrayList.get(i).getStatus().equals("true")) {
                        friendsEmail = userArrayList.get(i).getEmail();
                        onBackPressed();
                        MapsActivity.mDrawerLayout.closeDrawers();

                }
                else {
                    Toast.makeText(getBaseContext(), "Musisz być aktywny by śledzić innych!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        userList3.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                    Toast.makeText(getBaseContext(), "Użytkownik nie jest dostępny", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void dodajKogos(View view) {
        try{
            userArrayList.clear();
            for(int i=0; i < countFriends;i++) {
                if(friendStatus.get(i) == "true") {
                    userArrayList.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendStatus.get(i)));
                    adapter.notifyDataSetChanged();
                }
//                else {
//                    userArrayList.remove(i);
//                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
            result.setText(" c huj xd "+e.getMessage());
        }

    }

public class DoQuery extends AsyncTask<String,String,String>{

        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {

            String query="";
            String query2="";

            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "nie udalo sie  połaczyć niestety xD";
                }
                else {
                    if(ps=="log") {
                      //  query = " select * from friendss where my_email='" + em + "'";
                        query = "select * from usrs2 where email in (select friends_email from friendss where my_email='" + em + "' and accepted='true')";
                        query2 = "select * from usrs2 where usrstatus='false' and email in (select friends_email from friendss where my_email='" + em + "' and accepted='true')";
                    }
                    else if(pss=="reg") {
                      //  query = " select * from friendss where my_email='" + email + "'";
                        query = "select * from usrs2 where email in (select friends_email from friendss where my_email='" + email + "' and accepted='true')";
                        query2 = "select * from usrs2 where usrstatus='false' and email in (select friends_email from friendss where my_email='" + email + "' and accepted='true')";
                    }
                  //  query = "select * from usrs2 where email in (select friends_email from friendss where my_email='atomczak30@gmail.com')";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    countFriends=0;
                    while (rs.next())
                    {
                        firstName.add(rs.getString(2));
                        lastNamee.add(rs.getString(3));
                        friendMail.add(rs.getString(4));
                        friendStatus.add(rs.getString(6));
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
        protected void onPostExecute(String s) {

            //  Toast.makeText(getBaseContext(),"bum "+z,Toast.LENGTH_LONG).show();
        }
    }


}
