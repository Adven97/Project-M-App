package advenstudios.prostamapav2;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {

    @SuppressLint("NewApi")
    public Connection CONN() {
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
            //textView.setText("chuj "+e.getMessage().toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
           // textView.setText("chuj 2  "+e.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
          //  textView.setText(" c h uj 434 "+e.getMessage());
        }
        //Toast.makeText(getBaseContext(),""+wiad,Toast.LENGTH_LONG).show();
        return conn;
    }
}
