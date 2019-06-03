//package advenstudios.prostamapav2;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.ArrayList;
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
//import java.util.List;
//
//import static advenstudios.prostamapav2.LoginActivity.em;
//import static advenstudios.prostamapav2.LoginActivity.ps;
//import static advenstudios.prostamapav2.RegisterActivity.email;
//import static advenstudios.prostamapav2.RegisterActivity.pss;
//
//
//public class SearchActivity extends AppCompatActivity {
//
//    private ListView userList;
//    ConnectionClass connectionClass;
//    ProgressDialog progressDialog;
//    UserAdapter adapter;
//
//    List<String> firstName;
//    List<String> lastNamee;
//    List<String> friendMail;
//    List<String> friendPassw;
//
//    User user;
//    User userTest;
//    ArrayList<User> userArrayList;
//    TextView result;
//    static String friendsEmail ="nikt@nikt.pl";
//    int countFriends;
//
//    ArrayAdapter<String> adapterek;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//        connectionClass = new ConnectionClass();
//        userList=(ListView)findViewById(R.id.userListIddd);
//        //result = (TextView) findViewById(R.id.rezultattt);
//        user=new User();
//        progressDialog = new ProgressDialog(this);
//
//        firstName = new ArrayList<String>();
//        lastNamee= new ArrayList<String>();
//        friendMail= new ArrayList<String>();
//        friendPassw= new ArrayList<String>();
//
//
//
////            already2=true;
//
//
//        userArrayList = new ArrayList<User>();
//        userTest=new User("Andrzej", "Duda","prezydent@xd.pl", "janek123");
//        //  userArrayList.add(userTest);
//
//        adapter = new UserAdapter(this, userArrayList);
//
//        adapterek = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,firstName);
//
//        userList.setAdapter(adapter);
//
//
//        DoQuery selectUsers = new DoQuery();
//        selectUsers.execute("");
//
//        Thread t = new Thread();
//        try {
//            t.sleep(8000);
//            // progressDialog.hide();
//            try {
//                for (int i = 0; i < countFriends; i++) {
//                    userArrayList.add(new User(firstName.get(i), lastNamee.get(i), friendMail.get(i), friendPassw.get(i)));
//                    adapter.notifyDataSetChanged();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                result.setText("cos sie popsulo " + e.getMessage());
//            }
//            t.interrupt();
//        } catch (InterruptedException e) {}
//
//
//
//        MapsActivity.progressDialogfromMap.hide();
//        userList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
//
//                if (!checkIfFriend(userArrayList.get(i).getEmail())) {
//                    Toast.makeText(getBaseContext(), "Masz już w znajomych tego usera", Toast.LENGTH_LONG).show();
//                }
//
//                else {
//                    try {
//                        Connection conon = connectionClass.CONN();
//                        if (conon == null) {
//
//                            Toast.makeText(getBaseContext(), "nie udalo sie  połaczyć niestety xD", Toast.LENGTH_LONG).show();
//                        } else {
//                            String query = "";
//                            if (ps == "log") {
//                                //  query = " select * from friendss where my_email='" + em + "'";
//                                query = "insert into friendss(my_email, friends_email,accepted) values('" + em + "', '" + userArrayList.get(i).getEmail() + "','false')";
//                            } else if (pss == "reg") {
//                                //  query = " select * from friendss where my_email='" + email + "'";
//                                query = "insert into friendss(my_email, friends_email, accepted) values('" + email + "', '" + userArrayList.get(i).getEmail() + "','false')";
//                            }
//
//                            Statement stmt = conon.createStatement();
//                            stmt.executeUpdate(query);
//
//                            Toast.makeText(getBaseContext(), "Prośba została wysłana ", Toast.LENGTH_LONG).show();
//
//                        }
//                    } catch (Exception ex) {
//                        Toast.makeText(getBaseContext(), "Exception wyjebalo: " + ex, Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//
//    }
//
//    boolean checkIfFriend(String danyUser){
//
//        String query="";
//        boolean b=true;
//
//        try {
//            Connection con = connectionClass.CONN();
//            if (con == null) {
//                Toast.makeText(getBaseContext(),"nie udalo sie  połaczyć niestety xD",Toast.LENGTH_LONG).show();
//            }
//            else {
//                if(ps=="log") {
//                    query = "select friends_email from friendss where my_email='" + em + "'";
//                }
//                else if(pss=="reg") {
//                    query = "select friends_email from friendss where my_email='" + email + "'";
//                }
//                //  query = "select * from usrs2 where email in (select friends_email from friendss where my_email='atomczak30@gmail.com')";
//                Statement stmt = con.createStatement();
//                ResultSet rs = stmt.executeQuery(query);
//
//                countFriends=0;
//                while (rs.next())
//                {
//                    friendMail.add(rs.getString(1));
//                    countFriends++;
//                }
//
//                for(int i=0; i < countFriends;i++) {
//                    if(friendMail.get(i) == danyUser){
//                        b=false;
//                    }
//                }
//
//            }
//        }
//        catch (Exception ex)
//        {
//            Toast.makeText(getBaseContext(),"Exception : "+ex,Toast.LENGTH_LONG).show();
//        }
//
//        return b;
//    }
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_menu, menu);
//
//        MenuItem itemm = menu.findItem(R.id.search_menu);
//        SearchView searchView = (SearchView)itemm.getActionView();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                adapterek.getFilter().filter(s);
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
//
//
//    public class DoQuery extends AsyncTask<String,String,String>
//    {
//
//        String z="";
//        boolean isSuccess=false;
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            String query="";
//
//            try {
//                Connection con = connectionClass.CONN();
//                if (con == null) {
//                    z = "nie udalo sie  połaczyć niestety xD";
//                }
//                else {
//                    if(ps=="log") {
//                        query = "select * from usrs2 where email !='"+em+"'";
//                    }
//                    if(ps=="reg") {
//                        query = "select * from usrs2 where email !='"+email+"'";
//                    }
//
//                    Statement stmt = con.createStatement();
//                    ResultSet rs = stmt.executeQuery(query);
//
//                    countFriends=0;
//                    while (rs.next())
//                    {
//                        firstName.add(rs.getString(2));
//                        lastNamee.add(rs.getString(3));
//                        friendMail.add(rs.getString(4));
//                        friendPassw.add(rs.getString(5));
//                        countFriends++;
//                    }
//
//                }
//            }
//            catch (Exception ex)
//            {
//                isSuccess = false;
//                z = "Exception wyjebalo: "+ex;
//            }
//
//            return z;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//            //  Toast.makeText(getBaseContext(),"bum "+z,Toast.LENGTH_LONG).show();
//        }
//    }
//
//}
