package advenstudios.prostamapav2;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.Timer;
import java.util.TimerTask;

/**
 * An activity that displays a map showing the place at the device's current location.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    double geoLong, geoLat;

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
    TextView textView;
    int markerNumber=0;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;

    LatLng other = new LatLng(geoLat,geoLong);

    Marker marker;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

       /* Thread t=new Thread(){ @Override public void run(){
            while(!isInterrupted()){
                try { Thread.sleep(1000);
                     runOnUiThread(new Runnable()
                     { @Override public void run() {

                     }
                     }); } catch (InterruptedException e) {} } } };
        t.start(); */

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        textView = (TextView) findViewById(R.id.textView);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();

        mGoogleApiClient.connect();

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
        Thread t=new Thread(){
            @Override public void run(){
                while(!isInterrupted()){
                    try { Thread.sleep(1000); //1000ms = 1 sec
                         runOnUiThread(new Runnable() {
                             @Override public void run() {

                                 getDeviceChangedLocation();

                             } }); } catch (InterruptedException e) {} } } };
                            t.start();

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
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();

                            geoLat = mLastKnownLocation.getLatitude();
                            geoLong = mLastKnownLocation.getLongitude();
                           // String znacznik = "R.drwaable.sak";

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(geoLat,geoLong), DEFAULT_ZOOM));

                            textView.setText("Twoje aktualne położenie: "+geoLat+ " , " +geoLong);

                            dodajMarker(geoLat,geoLong,BitmapDescriptorFactory.fromResource(R.drawable.emoji));

                           // dodajMarker(geoLat,geoLong, BitmapDescriptorFactory.fromResource(R.drawable.emoji));
                          //  dodajMarker(geoLat+0.0032282,geoLong-0.000013, BitmapDescriptorFactory.fromResource(R.drawable.stonog));
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

                            textView.setText("Twoje aktualne położenie: "+geoLat+ " , " +geoLong);

                            dodajNowyMarker(geoLat,geoLong, BitmapDescriptorFactory.fromResource(R.drawable.emoji));
                          //  dodajNowyMarker(geoLat+0.0032282,geoLong-0.000013, BitmapDescriptorFactory.fromResource(R.drawable.stonog));
                            markerNumber++;
                         /*   BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.emoji);
                            LatLng other = new LatLng(geoLat,geoLong);
                            MarkerOptions markerOptions = new MarkerOptions().position(other)
                                    .title("You're here")
                                    .snippet("")
                                    .icon(icon);
                            mMap.addMarker(markerOptions);


                            BitmapDescriptor icon2 = BitmapDescriptorFactory.fromResource(R.drawable.stonog);
                            LatLng other2 = new LatLng(geoLat+0.0032282,geoLong-0.000013);
                            MarkerOptions markerOptions2 = new MarkerOptions().position(other2)
                                    .title("You're here")
                                    .snippet("")
                                    .icon(icon2);
                            mMap.addMarker(markerOptions2);*/

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

    private void dodajMarker( double geoLat, double geoLong, BitmapDescriptor myIcon){

        BitmapDescriptor icon = myIcon;
        LatLng other = new LatLng(geoLat,geoLong);
        marker = mMap.addMarker(new MarkerOptions().position(other)
                .title("You're here")
                .snippet("")
                .icon(icon));

    }

    private void dodajNowyMarker( double geoLat, double geoLong, BitmapDescriptor myIcon){


        if(marker != null && markerNumber > 0){
            marker.remove();

            BitmapDescriptor icon = myIcon;
            LatLng other = new LatLng(geoLat,geoLong);
             marker = mMap.addMarker(new MarkerOptions().position(other)
                    .title("You're here")
                    .snippet("")
                    .icon(icon));

          //  mMap.addMarker(marker);
        }

    }











    private void turnGPSOn(){
        String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")){ //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            sendBroadcast(poke);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    MapsActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                         case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode)
        {
            case REQUEST_LOCATION:
                switch (resultCode)
                {
                    case Activity.RESULT_OK:
                    {
                        // All required changes were successfully made
                        Toast.makeText(MapsActivity.this, "Location enabled by user!", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED:
                    {
                        // The user was asked to change settings, but chose not to
                        Toast.makeText(MapsActivity.this, "Location not enabled, user cancelled.", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default:
                    {
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
