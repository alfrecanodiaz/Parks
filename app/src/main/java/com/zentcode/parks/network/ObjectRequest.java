package com.zentcode.parks.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.zentcode.parks.app.Messages;

import org.json.JSONObject;

public class ObjectRequest {

    private Context mContext;

    public ObjectRequest(Context context) {
        this.mContext = context;
    }

    public interface RequestCallback {
        void success(JSONObject response);
        void error();
    }

    public void sendRequest(Integer method, String url, final String body, final RequestCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    callback.success(response);
                } catch (Exception e) {
                    Log.d(Messages.REQUEST_EXCEPTION, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = (error == null ? null : error.getMessage());
                Log.d(Messages.REQUEST_EXCEPTION, message);
                callback.error();
            }
        })
        {
            @Override
            public byte[] getBody() {
                return body.getBytes();
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(
                new DefaultRetryPolicy(
                        30000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        VolleyRequestQueue.getInstance(mContext).getRequestQueue().add(request);
    }
}
