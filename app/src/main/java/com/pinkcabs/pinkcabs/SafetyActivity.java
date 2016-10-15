package com.pinkcabs.pinkcabs;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

public class SafetyActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    NearbyLocator nearbyLocator;
    RouteMaker rm;
    TextView tvpolName, tvpolAddress, tvpolContact, tvpolDis, tvpolTime, tvhospName, tvhospAddress, tvhospContact, tvhospDis, tvhospTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety);

        tvpolName = (TextView) findViewById(R.id.tv_pol_name);
        tvpolAddress = (TextView) findViewById(R.id.tv_pol_address);
        tvpolContact = (TextView) findViewById(R.id.tv_pol_contact);
        tvpolDis = (TextView) findViewById(R.id.tv_pol_dis);
        tvpolTime = (TextView) findViewById(R.id.tv_pol_time);

        tvhospName = (TextView) findViewById(R.id.tv_hosp_name);
        tvhospAddress = (TextView) findViewById(R.id.tv_hosp_address);
        tvhospContact = (TextView) findViewById(R.id.tv_hosp_contact);
        tvhospDis = (TextView) findViewById(R.id.tv_hosp_dis);
        tvhospTime = (TextView) findViewById(R.id.tv_hosp_time);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMap=null;

        rm=new RouteMaker(this);
        rm.setOnDirectionsReceivedListener(new RouteMaker.OnDirectionsReceivedListener() {
            @Override
            public void displayPolyline(List<LatLng> latLngList) {
                if(mMap!=null)
                    mMap.addPolyline(new PolylineOptions().addAll(latLngList));
            }

            @Override
            public void getDistanceString(String distance) {

            }

            @Override
            public void getDistanceValue(Double distance) {

            }

            @Override
            public void getTimeString(String time) {

            }

            @Override
            public void getTimeValue(Double time) {

            }
        });


        nearbyLocator=new NearbyLocator(this);
        nearbyLocator.setOnPlaceSearchResultReceivedListener(new NearbyLocator.OnPlaceSearchResultReceivedListener() {
            @Override
            public void useLatLngAndName(LatLng placeLL,String name) {
                if(mMap!=null) {
                    mMap.addMarker(new MarkerOptions().position(placeLL).title(name));
                    }
                if(mLastLocation!=null){
                    rm.findPath(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),placeLL);
                }
            }

            @Override
            public void usePlaceID(String placeID) {
                nearbyLocator.findPlaceDetails(placeID);
            }
        });

        nearbyLocator.setOnPlaceDetailResultReceivedListener(new NearbyLocator.OnPlaceDetailResultReceivedListener() {
            @Override
            public void useAddress(String address) {

            }

            @Override
            public void usePhone(String phone) {

            }

            @Override
            public void useName(String name) {

            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        mMap.setMyLocationEnabled(true);
        //Location myLocation=mMap.getMyLocation();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            if(mMap!=null){
                LatLng mahLocationLL=new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                mMap.addMarker(new MarkerOptions().position(mahLocationLL));
                nearbyLocator.findNearbyPlacesByType(mahLocationLL,"police");
                nearbyLocator.findNearbyPlacesByType(mahLocationLL,"hospital");



            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
