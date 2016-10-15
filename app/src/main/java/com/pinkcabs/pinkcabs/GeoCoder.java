package com.pinkcabs.pinkcabs;

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

public class GeoCoder {

    private static final String TAG = "GeoCoder";

    RequestQueue rq;
    Context c;
    ReverseGeoCodeResponseListener rgcl;

    public GeoCoder(Context c) {
        this.c = c;
        rq= Volley.newRequestQueue(c);
    }

    public void setReverseGeoCodeListener(ReverseGeoCodeResponseListener r){
        rgcl=r;
    }

    public void reverseGeoCode(LatLng ll){
        rq.add(reverseGeoCodeRequestBuilder(ll));
    }
    private StringRequest reverseGeoCodeRequestBuilder(LatLng ll){
        String basic="https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        String key="&key=AIzaSyDIVZ-j79nYVjEW0B99YiUG5zb5Jf_JVWc";
        final String requestString=basic+ll.latitude+","+ll.longitude+key;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, requestString, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d(TAG, "onResponse: "+response);
                try {
                    JSONObject responseJSon =new JSONObject(response);
                    JSONArray results=responseJSon.getJSONArray("results");
                    JSONObject jobj=results.getJSONObject(0);
                    //tvGeoCode.setText(jobj.getString("formatted_address"));
                    rgcl.useAddress(jobj.getString("formatted_address"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        });
        return  stringRequest;
    }

    public interface ReverseGeoCodeResponseListener{
        void useAddress(String address);
    }
}
