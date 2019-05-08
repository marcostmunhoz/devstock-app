package com.devstock;

import android.app.Activity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Map;

public class ApiHandler {
    final String BASE_PATH = "http://devstock.herokuapp.com/api/";
    RequestQueue queue;

    public ApiHandler(Activity ref) {
        queue = Volley.newRequestQueue(ref);
    }

    public JSONObject sendRequest(String url, Response.Listener success, Response.ErrorListener error, Map<String, String> header) {
        StringRequest request = new StringRequest()
    }
}
