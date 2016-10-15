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
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sushant on 15-10-2016.
 */

public class NearbyLocator {
    Context c;
    RequestQueue queue;

    private static final String TAG = "NearbyLocator";

    public NearbyLocator(Context c) {
        this.c = c;
        queue = Volley.newRequestQueue(c);
    }

    public void findNearbyPlacesByType(LatLng latLng, String type) {

        queue.add(placeSearchRequestBuilder(latLng, type));
    }

    private StringRequest placeSearchRequestBuilder(LatLng latLng, String type) {
        String beg = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
        String rankbyAndType = "&rankby=distance&type=";
        String key = "&type=police&key=AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI";

        String queuery = beg + latLng.latitude + "," + latLng.longitude + rankbyAndType + type + key;
        String dummy = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&rankby=distance&type=police&key=AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI";

        StringRequest sr = new StringRequest(Request.Method.GET, queuery, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                placeSearchResultHandler(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });

        return sr;
    }
    OnPlaceSearchResultReceivedListener opsrrl=null;

    public void setOnPlaceSearchResultReceivedListener(OnPlaceSearchResultReceivedListener o){
        opsrrl=o;
    }
    private void placeSearchResultHandler(String response) {
        try {
            JSONObject jObjBase = new JSONObject(response);
            JSONArray jArrayResults = jObjBase.getJSONArray("results");
            JSONObject jObjResult = jArrayResults.getJSONObject(0);
            JSONObject jObjLocation = jObjResult.getJSONObject("geometry").getJSONObject("location");
            LatLng ll = new LatLng(jObjLocation.getDouble("lat"), jObjLocation.getDouble("lng"));
            String placeID = jObjResult.getString("place_id");
            String name=jObjResult.getString("name");
            Log.d(TAG, "placeSearchResultHandler: " + placeID);
            //findPlaceDetails(placeID);
            opsrrl.useLatLngAndName(ll,name);
            opsrrl.usePlaceID(placeID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface OnPlaceSearchResultReceivedListener{
        void useLatLngAndName(LatLng placeLL,String name);
        void usePlaceID(String placeID);
    }

    public void findPlaceDetails(String placeID) {
        queue.add(placeDetailStringBuilder(placeID));
    }

    private StringRequest placeDetailStringBuilder(String placeID) {
        String beg = "https://maps.googleapis.com/maps/api/place/details/json?placeid=";
        String key = "&key=AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI";

        String query = beg + placeID + key;
        Log.d(TAG, "placeDetailStringBuilder: query=" + query);
        StringRequest sr = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                placeDetailResultHandler(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        });
        return sr;
    }

    OnPlaceDetailResultReceivedListener opdrrl;


    public void setOnPlaceDetailResultReceivedListener(OnPlaceDetailResultReceivedListener o){
        opdrrl=o;
    }
    private void placeDetailResultHandler(String response) {
        try {
            JSONObject jObjPlace = new JSONObject(response);
            JSONObject jObjResult = jObjPlace.getJSONObject("result");
            if (jObjResult.has("formatted_address")) {
                String address = jObjResult.getString("formatted_address");
                Log.d(TAG, "placeDetailResultHandler: address=" + address);
                opdrrl.useAddress(address);
            }

            if (jObjResult.has("formatted_phone_number")) {
                String phone = jObjResult.getString("formatted_phone_number");
                Log.d(TAG, "placeDetailResultHandler: phone=" + phone);
                opdrrl.usePhone(phone);
            }

            if (jObjResult.has("name")) {
                String name = jObjResult.getString("name");
                Log.d(TAG, "placeDetailResultHandler: name=" + name);
                opdrrl.useName(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public interface OnPlaceDetailResultReceivedListener{
        void useAddress(String address);
        void usePhone(String phone);
        void useName(String name);
    }


}
//https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&rankby=distance&type=police&key=AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI
// AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI places api web service
//https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&key=AIzaSyA9QBKcYiu3LbbAzmKuu09Xret_-hqF9wI