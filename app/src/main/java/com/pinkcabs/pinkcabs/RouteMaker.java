package com.pinkcabs.pinkcabs;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushant on 14-10-2016.
 */

public class RouteMaker {
    Context context;
    //GoogleMap googleMap;
    RequestQueue requestQueue;
    OnDirectionsReceivedListener odrl;

    private static final String TAG = "RouteMaker";

    public RouteMaker(Context c) {
//        this.start = start;
//        this.end = end;
        this.context=c;
       // this.googleMap=googleMap;
    }

    public void setOnDirectionsReceivedListener(OnDirectionsReceivedListener o){
        odrl=o;
    }

    void findPath(LatLng start,LatLng end){
        requestQueue=Volley.newRequestQueue(context);
        requestQueue.add(directionRequestBuilder(start,end));
    }
    private StringRequest directionRequestBuilder(LatLng start,LatLng end){
        String origin="https://maps.googleapis.com/maps/api/directions/json?origin=";
        String destination="&destination=";
        String apiKey="&key=AIzaSyAa2CKARbz6bU6Nx6UJNVFG_2hwR3lVgkQ";
        String request=origin+start.latitude+","+start.longitude+destination+end.latitude+","+end.longitude+apiKey;
        String dummy="https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyAa2CKARbz6bU6Nx6UJNVFG_2hwR3lVgkQ";
        StringRequest request1=new StringRequest(Request.Method.GET, request, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "onResponse: "+response);
                //System.out.println(response);
                directionResultHandler(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        });
        return request1;
    }
    private void directionResultHandler(String response){
        try {
            JSONObject jresponse=new JSONObject(response);

            JSONArray jarrayRoutes=jresponse.getJSONArray("routes");

            JSONObject jroute=jarrayRoutes.getJSONObject(0);

            JSONObject jleg=jroute.getJSONArray("legs").getJSONObject(0);
            JSONObject jlegDistance=jleg.getJSONObject("distance");
            String distanceString=jlegDistance.getString("text");
            Double distanceValue=jlegDistance.getDouble("value");
            JSONObject jlegTime=jleg.getJSONObject("duration");
            String timeString=jlegTime.getString("text");
            Double timeValue=jlegTime.getDouble("value");
            odrl.getDistanceString(distanceString);
            odrl.getDistanceValue(distanceValue);
            odrl.getTimeString(timeString);
            odrl.getTimeValue(timeValue);

            JSONObject jOverviewPolyline=jroute.getJSONObject("overview_polyline");
            String polyline=jOverviewPolyline.getString("points");
            List<LatLng> pointLatLngArrayList = PolyUtil.decode(polyline);
            //googleMap.addPolyline(new PolylineOptions().addAll(pointLatLngArrayList));
            odrl.displayPolyline(pointLatLngArrayList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } 

    public interface OnDirectionsReceivedListener{
        void displayPolyline(List<LatLng> latLngList);
        void getDistanceString(String distance);
        void getDistanceValue(Double distance);
        void getTimeString(String time);
        void getTimeValue(Double time);
    }
}
//https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyAa2CKARbz6bU6Nx6UJNVFG_2hwR3lVgkQ