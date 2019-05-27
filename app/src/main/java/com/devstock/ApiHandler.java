package com.devstock;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiHandler {
    private static final String BASE_PATH = "http://devstock.herokuapp.com/api";
    private static HashMap<Context, ApiHandler> instances = new HashMap<Context, ApiHandler>();
    private static String AUTH_TOKEN = null;

    private RequestQueue queue;

    private ApiHandler(Activity act) {
        Context ctx = act.getApplicationContext();
        this.queue = Volley.newRequestQueue(ctx);
        instances.put(ctx, this);
    }

    private static synchronized Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + AUTH_TOKEN);
        return map;
    }

    public static synchronized ApiHandler getInstance(Activity act) {
        Context ctx = act.getApplicationContext();

        if (!instances.containsKey(ctx)) {
            if (!instances.containsKey(ctx)) {
                instances.put(ctx, new ApiHandler(act));
            }
        }

        return instances.get(ctx);
    }

    public static synchronized void setToken(String token) {
        AUTH_TOKEN = token;
    }

    public static synchronized boolean isLoggedIn() {
        return (AUTH_TOKEN != null);
    }

    public void logInUser(String username, String password, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject body = Helpers.createJsonObject(
                "login", username,
                "password", password
        );

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/login", body, success, error));
    }

    public void validateToken(final String token, Response.Listener success, Response.ErrorListener error) throws Exception {

        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/check-token/" + Uri.encode(token), null, success, error));
    }

    public void getProdutosLike(String query, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produtos/" + Uri.encode(query), null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void getProduto(int idProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void newProduto(JSONObject dadosProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/produto", dadosProduto, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void setProduto(int idProduto, JSONObject dadosProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.PUT, BASE_PATH + "/produto/" + idProduto, dadosProduto, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }

    public void deleteProduto(int idProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.DELETE, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                return ApiHandler.getHeaders();
            }
        });
    }
}
