package com.pinkcabs.pinkcabs;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vishal on 15-Oct-16.
 */

public class ServerRequests {

    private RequestCallback callback;
//    private static ServerRequests requests=null;
    private String ALL_CABS="http://198.199.120.41/pinkcabs/v1/drivers";
    private String CABS_WITHIN_6="http://198.199.120.41/pinkcabs/v1/driver/lat=_LAT_/long=_LONG_";
    private String SELECT_A_DRIVER="http://198.199.120.41/pinkcabs/v1/driver/_DRV_FIREID_/allot";
    private String RELEASE_A_DRIVER="http://198.199.120.41/pinkcabs/v1/driver/_DRV_FIREID_/release";
    private String UPDATE_MY_LOCATION="http://198.199.120.41/pinkcabs/v1/user/location/_USER_FIRE_ID_";
    private String NEW_USER="http://198.199.120.41/pinkcabs/v1/register/user";
    private static final String TAG = "ServerRequests";
//
//    public static ServerRequests getInstance() {
//        if (requests==null) requests=new ServerRequests();
//        return requests;
//    }

    public void setCallback(RequestCallback callback) {
        this.callback = callback;
    }

     void getAllCabs(Context ctx) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                ALL_CABS,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray driverList = response.getJSONArray("driver_list");
                            if (callback!=null) callback.response(driverList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                });
        Volley.newRequestQueue(ctx).add(req);
    }

    void getCabsWithin6(Context ctx,double latitude, double longitude) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                CABS_WITHIN_6.replace("_LAT_",String.valueOf(latitude))
                        .replace("_LONG_",String.valueOf(longitude)),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray driverList = response.getJSONArray("driver_list");
                            if (callback!=null) callback.response(driverList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                });
        Volley.newRequestQueue(ctx).add(req);
    }

    void selectDriver(Context ctx, String driverFirebaseId, final String userFirebaseId) {
        StringRequest req=new StringRequest(
                Request.Method.PUT,
                SELECT_A_DRIVER.replace("_DRV_FIREID_", driverFirebaseId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "onResponse: "+response);
                            String res = new JSONObject(response).getString("result");
                            if (callback!=null) callback.response(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                        Log.d(TAG, "onErrorResponse: "+error.getMessage());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("user_fireb_id",userFirebaseId);
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        Volley.newRequestQueue(ctx).add(req);
    }

    void releaseDriver(Context ctx, String driverFirebaseId) {
        StringRequest req=new StringRequest(
                Request.Method.PUT,
                RELEASE_A_DRIVER.replace("_DRV_FIREID_", driverFirebaseId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String res = new JSONObject(response).getString("result");
                            if (callback!=null) callback.response(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        );
        Volley.newRequestQueue(ctx).add(req);
    }

    void newUser(Context ctx, final String userFirebaseId) {
        Log.d(TAG, "newUser: "+userFirebaseId);
        StringRequest req=new StringRequest(
                Request.Method.POST,
                NEW_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "onResponse: "+response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (callback!=null) callback.response(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: "+error.getMessage());
                        if (callback!=null) callback.response(null);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("user_fireb_id",userFirebaseId);
                return map;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
        Volley.newRequestQueue(ctx).add(req);
    }

    void updateMyLocation(Context ctx, String userFirebaseId, final double latitude, final double longitude) {
        StringRequest req=new StringRequest(
                Request.Method.PUT,
                UPDATE_MY_LOCATION.replace("_USER_FIRE_ID_", userFirebaseId),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String res = new JSONObject(response).getString("result");
                            if (callback!=null) callback.response(res);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (callback!=null) callback.response(null);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map=new HashMap<>();
                map.put("latitude", String.valueOf(latitude));
                map.put("longitude", String.valueOf(longitude));
                return map;
            }
        };
        Volley.newRequestQueue(ctx).add(req);
    }

    interface RequestCallback{
        void response(Object data);
    }
}
