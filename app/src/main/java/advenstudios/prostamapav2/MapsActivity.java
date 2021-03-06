package advenstudios.prostamapav2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static advenstudios.prostamapav2.LoginActivity.em;
import static advenstudios.prostamapav2.RegisterActivity.email;
import static advenstudios.prostamapav2.LoginActivity.ps;
import static advenstudios.prostamapav2.RegisterActivity.pss;
import static advenstudios.prostamapav2.SearchFriendActivity.friendsEmail;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    double geoLong, geoLat;
    static ProgressDialog progressDialogfromMap;

    Thread tMyLoc, tFriendLoc;
    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng bauty = new LatLng(51.788974, 19.457503);
    private static final int DEFAULT_ZOOM = 18;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    TextView textView, ttt;
    int markerNumber=0;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    Marker marker;
    Marker friendMarker;

    static DrawerLayout mDrawerLayout;

    String firstName="Please";
    String lastName="wait";
    double friendLong =61.2324;
    double friendLat =29.9752;

    ConnectionClass connectionClass;
    Button toggleButton;
   static boolean activeUsr;

//    static boolean already1;
//    static boolean already2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectionClass = new ConnectionClass();
        progressDialogfromMap = new ProgressDialog(this);
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView) findViewById(R.id.textView);

       // ttt = (TextView) findViewById(R.id.tttt);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mGoogleApiClient.connect();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.burger);

//        already1=false;
//        already2=false;

        DoQuery dq = new DoQuery();
        dq.execute("");

        mDrawerLayout = findViewById(R.id.drawer_layout);

        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setTextColor(Color.parseColor("#DC143C"));

        activeUsr=false;

        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        TextView myNameView = (TextView)findViewById(R.id.tttt);
                     //   myNameView.setText("Hello there! "+em);

                        myNameView.setText("Hey there! "+firstName+" "+ lastName);

                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
        mDrawerLayout.closeDrawers();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;
                    }
                });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        // Get the current location of the device and set the position of the map.

        tMyLoc=new Thread(){
            @Override public void run(){
                while(!isInterrupted()){
                    try { Thread.sleep(1000); //1000ms = 1 sec
                         runOnUiThread(new Runnable() {
                             @Override public void run() {

                                 getDeviceChangedLocation();
                                // getFriendLocation();

                             } }); } catch (InterruptedException e) {} } } };
        tMyLoc.start();

        tFriendLoc =new Thread(){
            @Override public void run(){
                while(!isInterrupted()){
                    try { Thread.sleep(1000); //1000ms = 1 sec
                        runOnUiThread(new Runnable() {
                            @Override public void run() {

                                 getFriendLocation();

                            } }); } catch (InterruptedException e) {} } } };
        tFriendLoc.start();

    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()==true) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            progressDialogfromMap.setMessage("Ładuję mapę, proszę czekać");
                            progressDialogfromMap.show();
                            Thread t = new Thread();
                            try {
                                t.sleep(3000);
                                // progressDialog.hide();
                                try {
                                    geoLat = mLastKnownLocation.getLatitude();
                                    geoLong = mLastKnownLocation.getLongitude();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                t.interrupt();
                            } catch (InterruptedException e) {}
                            progressDialogfromMap.hide();
                            InsertPosToDb insertPosToDb = new InsertPosToDb();
                            insertPosToDb.execute("");

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(geoLat,geoLong), DEFAULT_ZOOM));

                            textView.setText("Aktualne położenie: "+geoLat+ " , " +geoLong);

                            dodajNowyMarker(geoLat,geoLong,BitmapDescriptorFactory.fromResource(R.drawable.emoji));

                            markerNumber++;

                            //////ZAPISAC GDZIES TE WSP GEOGRAFICZNE

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(bauty, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceChangedLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            geoLat = mLastKnownLocation.getLatitude();
                            geoLong = mLastKnownLocation.getLongitude();
                            InsertPosToDb insertPosToDb = new InsertPosToDb();
                            insertPosToDb.execute("");

                            textView.setText("Aktualne położenie: "+geoLat+ " , " +geoLong);

                            dodajNowyMarker(geoLat,geoLong, BitmapDescriptorFactory.fromResource(R.drawable.emoji));

                            markerNumber++;

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(bauty, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getFriendLocation() {

        try {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            FriendPos fpos = new FriendPos();
                            fpos.execute("");

                            dodajFriendMarker(friendLat,friendLong, BitmapDescriptorFactory.fromResource(R.drawable.alien_emoji));

                            markerNumber++;

                        }
                        else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(bauty, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });

        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            mLocationPermissionGranted = true;

        }
        else {
            Log.d(TAG, "Current location is null");
            Log.e(TAG, "Exception: s");

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this,
                            "permission was granted  :)",
                            Toast.LENGTH_LONG).show();
                    //getMyLocation();

                }
                updateLocationUI();
            }
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);     ////// TA LINIA ODPOWIADA ZA NIEBIESKĄ KROPKE !!!
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }

        } catch (SecurityException e)  {
            Log.e("wyjonteg: %s", e.getMessage());
        }
    }

    private void dodajNowyMarker( double geoLat, double geoLong, BitmapDescriptor myIcon){

        if(marker != null && markerNumber > 0){
            marker.remove();}

        BitmapDescriptor icon = myIcon;
        LatLng other = new LatLng(geoLat,geoLong);
        marker = mMap.addMarker(new MarkerOptions().position(other)
                .title("You're here")
                .snippet("")
                .icon(icon));
    }

    private void dodajFriendMarker( double geoLat, double geoLong, BitmapDescriptor myIcon){

        if(friendMarker != null && markerNumber > 0){
            friendMarker.remove();}

        BitmapDescriptor icon = myIcon;
        LatLng other = new LatLng(geoLat,geoLong);
        friendMarker = mMap.addMarker(new MarkerOptions().position(other)
                .title("Your friend is here")
                .snippet("")
                .icon(icon));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void connectFriends(MenuItem item) {
        //already1=false;

        try {
            try{
                progressDialogfromMap.setMessage("Szukam ci przyjaciół, poczekaj");
                progressDialogfromMap.show();
                    startActivity(new Intent(getApplicationContext(), SearchFriendActivity.class));
                    //already1=true;
              //  }
            }
            catch (Exception e){
                e.printStackTrace();
                textView.setText("Cos sie sie cos popsulo: "+e.getMessage().toString());
            }

        }
        catch (Exception e){

            e.printStackTrace();
            textView.setText("Erorrr: "+e.getMessage().toString());
        }

    }

    public void searchForFriends(MenuItem item) {
        //already2=false;
        try {
            try{
                progressDialogfromMap.setMessage("Prosze czekac");
                progressDialogfromMap.show();
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));

                //  }
            }
            catch (Exception e){
                e.printStackTrace();
                textView.setText("Cos sie sie cos popsulo: "+e.getMessage().toString());
            }

        }
        catch (Exception e){

            e.printStackTrace();
            textView.setText("Errrorrr: "+e.getMessage().toString());
        }

    }
    public void requests(MenuItem item) {
        //already2=false;
        try {
            try{
                progressDialogfromMap.setMessage("Prosze czekac");
                progressDialogfromMap.show();
                startActivity(new Intent(getApplicationContext(), RequestsActivity.class));
            }
            catch (Exception e){
                e.printStackTrace();
                textView.setText("cos sie sie cos popsulo: "+e.getMessage().toString());
            }
        }
        catch (Exception e){
            e.printStackTrace();
            textView.setText("Mamy problem: "+e.getMessage().toString());
        }
    }

    public void onTButtonClick(View view) {

        activeUsr = !activeUsr;
        if(!activeUsr){
            toggleButton.setTextColor(Color.parseColor("#DC143C"));
            toggleButton.setText("Not Active");
        }
        else {
            toggleButton.setTextColor(Color.parseColor("#00cc00"));
            toggleButton.setText("Active");
        }
        SetActive setac = new SetActive();
        setac.execute("");

    }

    public class DoQuery extends AsyncTask<String,String,String>{

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
                        query = " select * from usrs2 where email='" + em + "'";
                    }
                    else if(pss=="reg") {
                        query = " select * from usrs2 where email='" + email + "'";
                    }
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next())
                    {
                        firstName = rs.getString(2);
                        lastName = rs.getString(3);

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
        }
    }

    public class InsertPosToDb extends AsyncTask<String,String,String>{

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
                        query ="update usrs2 set mylong ="+geoLong+", mylat ="+geoLat+" where email='" + em + "'";
                    }
                    else if(pss=="reg") {
                        query ="update usrs2 set mylong ="+geoLong+", mylat ="+geoLat+" where email='" + email + "'";
                    }


                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
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

        }
    }


    public class FriendPos extends AsyncTask<String,String,String>{

        String z="";
        boolean isSuccess=false;

        @Override
        protected void onPreExecute() {}

        @Override
        protected String doInBackground(String... params) {

            String query="";
            String emailOfFriend =friendsEmail;
            try {
                Connection con = connectionClass.CONN();
                if (con == null) {
                    z = "nie udalo sie  połaczyć niestety xD";
                }
                else {
                        query = " select mylat,mylong from usrs2 where email='"+emailOfFriend+"'";

                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next())
                    {
                        friendLat = rs.getDouble(1);
                        friendLong = rs.getDouble(2);
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

        }
    }

    public class SetActive extends AsyncTask<String,String,String>{

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
                        query = "update usrs2 set usrstatus='"+String.valueOf(activeUsr)+"' where email='" + em + "'";
                    }
                    else if(pss=="reg") {
                        query = "update usrs2 set usrstatus='"+String.valueOf(activeUsr)+"' where email='" + email + "'";
                    }
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(query);
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
        }
    }

}
