package com.pinkcabs.pinkcabs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    TextView tvGeoCode;
    String driverFirebaseId;
    RequestQueue rq;
    GoogleApiClient mGoogleApiClient = null;
    FancyButton searchButton;
    ServerRequests serverRequestsGetAllDrivers,serverRequestBookDriver;
    Location myLocation;
    String minDriverId;
    GeoCoder geoCoder;
    private static final String TAG = "MapsActivity";
    public static final Integer PLACE_AUTOCOMPLETE_REQUEST_CODE = 2209;
    public FloatingActionButton bookCab;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geoCoder=new GeoCoder(this);
        geoCoder.setReverseGeoCodeListener(new GeoCoder.ReverseGeoCodeResponseListener() {
            @Override
            public void useAddress(String address) {
                tvGeoCode.setText(address);
            }
        });
        bookCab = (FloatingActionButton) findViewById(R.id.book_cab);        // susi use this to book cab
        bookCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: minDriverId"+minDriverId);
                if(minDriverId==null){
                    Toast.makeText(MapsActivity.this, "Error! Try Again", Toast.LENGTH_SHORT).show();
                }else {
                    driverFirebaseId=minDriverId;
                    serverRequestBookDriver.selectDriver(MapsActivity.this, minDriverId, "sample_id"/*user.getUid()*/);
                }
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        serverRequestsGetAllDrivers =new ServerRequests();
        serverRequestsGetAllDrivers.setCallback(new ServerRequests.RequestCallback() {
            @Override
            public void response(Object data) {
                //JSONObject jObjResponse= (JSONObject) data;
                if (data==null) return;
                try {
                    //JSONArray jArrayDriverList=jObjResponse.getJSONArray("driver_list");
                    JSONArray jArrayDriverList= (JSONArray) data;
                    JSONObject jObjDriver;
                    Double distance,minDistance=10000000000000000000000000000000000000000.0;
                    for(int i=0;i<jArrayDriverList.length();i++){
                        jObjDriver=jArrayDriverList.getJSONObject(i);
                        Log.d(TAG, "response: Driverid"+jObjDriver.getInt("dID"));
                        Log.d(TAG, "response: lat"+jObjDriver.getDouble("latitude"));
                        mMap.addMarker(new MarkerOptions().position(new LatLng(jObjDriver.getDouble("latitude"),jObjDriver.getDouble("longitude"))).title("Taxi "+i));
                        if((distance=SphericalUtil.computeDistanceBetween(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),
                                new LatLng(jObjDriver.getDouble("latitude"),jObjDriver.getDouble("longitude"))))<minDistance){
                                    minDistance=distance;
                                    minDriverId=jObjDriver.getString("drv_fireb_id");
                            Log.d(TAG, "response: +minDriverIdChanged"+minDriverId);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        serverRequestBookDriver=new ServerRequests();
        serverRequestBookDriver.setCallback(new ServerRequests.RequestCallback() {
            @Override
            public void response(Object data) {

            }
        });

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        rq = Volley.newRequestQueue(this);
        tvGeoCode = (TextView) findViewById(R.id.tv_geocode);
        searchButton = (FancyButton) findViewById(R.id.btn_search);

    }

     RouteMaker rm;
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

        rm = new RouteMaker(this);

//        NearbyLocator nl=new NearbyLocator(this);
//        nl.findNearbyPlacesByType(new LatLng(28.644800,77.216721),"police");  //just some dummy testing code

        try {
            final Intent placeAutoCompleteIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(placeAutoCompleteIntent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                }
            });
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

        rm.setOnDirectionsReceivedListener(new RouteMaker.OnDirectionsReceivedListener() {
            @Override
            public void displayPolyline(List<LatLng> latLngList) {
                mMap.addPolyline(new PolylineOptions().addAll(latLngList));
            }

            @Override
            public void getDistanceString(String distance) {
                Log.d(TAG, "getDistanceString: distance=" + distance);
            }

            @Override
            public void getDistanceValue(Double distance) {
                Log.d(TAG, "getDistanceValue: distance=" + distance);
            }

            @Override
            public void getTimeString(String time) {
                Log.d(TAG, "getTimeString: time=" + time);
            }

            @Override
            public void getTimeValue(Double time) {
                Log.d(TAG, "getTimeValue: time=" + time);
            }
        });
        // Add a marker in Sydney and move the camera
        final LatLng sydney = new LatLng(-34, 151);
        LatLng xyz = new LatLng(-33, 150);
        rm.findPath(sydney, xyz);
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
        final Marker marker = mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney").draggable(true));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mMap.getMyLocation().getLatitude(),mMap.getMyLocation().getLongitude())));
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                                         @Override
                                         public void onCameraMove() {
                                             LatLng center = mMap.getCameraPosition().target;
                                             marker.setPosition(center);
                                             // rq.add(reverseGeoCodeRequestBuilder(center));
                                             //rm.findPath(sydney,center);

                                         }
                                     }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getAddress().toString()));
                Log.d(TAG, "Place:" + place.toString());
                if(myLocation!=null){
                    rm.findPath(new LatLng(myLocation.getLatitude(),myLocation.getLongitude()),place.getLatLng());
                }
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.d(TAG, status.getStatusMessage());
            } else if (requestCode == RESULT_CANCELED) {

            }
        }
    }

    LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLocationRequest(), this);

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLastLocation!=null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude())));
            //mMap.moveCamera(CameraUpdateFactory.zoomBy(7));
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

    @Override
    protected void onPause() {
        super.onPause();
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location mahLocation) {
        if(mMap!=null){
            myLocation=mahLocation;
            Log.d(TAG, "onLocationChanged: "+mahLocation.getLatitude()+mahLocation.getLongitude());
            serverRequestsGetAllDrivers.getCabsWithin6(this,mahLocation.getLatitude(),mahLocation.getLongitude());
            //serverRequestsGetAllDrivers.getAllCabs(this);
        }
    }
}

//AIzaSyDIVZ-j79nYVjEW0B99YiUG5zb5Jf_JVWc   geocodind api key
//https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=YOUR_API_KEY

//    AIzaSyAa2CKARbz6bU6Nx6UJNVFG_2hwR3lVgkQ   directions api key


//AIzaSyDxdGZkc176riyJN8KfZENp5kNxHU1-Lw4  places api key

// AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI places api web service


//198.199.120.41/pinkcabs/v1/drivers