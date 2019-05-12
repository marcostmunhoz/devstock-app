package com.devstock;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;

public class ApiHandler {
    private static final String BASE_PATH = "http://devstock.herokuapp.com/api";
    private static HashMap<Context, ApiHandler> instances = new HashMap<Context, ApiHandler>();

    private RequestQueue queue;


    public static synchronized ApiHandler getInstance(Context context) {
        if (!instances.containsKey(context)) {
            if (!instances.containsKey(context)) {
                instances.put(context, new ApiHandler(context));
            }
        }

        return instances.get(context);
    }

    private ApiHandler(Context context) {
        this.queue = Volley.newRequestQueue(context);
        instances.put(context, this);
    }

    public void logInUser(String username, String password, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject body = new JSONObject();
        body.put("login", username);
        body.put("password", password);

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/login", body, success, error));
    }
}
