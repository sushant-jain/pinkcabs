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
    GoogleMap googleMap;
    RequestQueue requestQueue;

    private static final String TAG = "RouteMaker";

    public RouteMaker(Context c,GoogleMap googleMap) {
//        this.start = start;
//        this.end = end;
        this.context=c;
        this.googleMap=googleMap;
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
        StringRequest request1=new StringRequest(Request.Method.GET, dummy, new Response.Listener<String>() {
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
            JSONObject jOverviewPolyline=jroute.getJSONObject("overview_polyline");
            String polyline=jOverviewPolyline.getString("points");
            List<LatLng> pointLatLngArrayList = PolyUtil.decode(polyline);
            googleMap.addPolyline(new PolylineOptions().addAll(pointLatLngArrayList));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    } 


}
//https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&key=AIzaSyAa2CKARbz6bU6Nx6UJNVFG_2hwR3lVgkQ