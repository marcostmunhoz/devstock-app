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

    private RequestQueue queue;

    public static synchronized ApiHandler getInstance(Activity act) {
        Context ctx = act.getApplicationContext();

        if (!instances.containsKey(ctx)) {
            if (!instances.containsKey(ctx)) {
                instances.put(ctx, new ApiHandler(act));
            }
        }

        return instances.get(ctx);
    }

    private ApiHandler(Activity act) {
        Context ctx = act.getApplicationContext();
        this.queue = Volley.newRequestQueue(ctx);
        instances.put(ctx, this);
    }

    public void logInUser(String username, String password, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject body = Helpers.createJsonObject(
                "login", username,
                "password", password
        );

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/login", body, success, error));
    }

    public void validateToken(String token, Response.Listener success, Response.ErrorListener error) throws Exception {
        JSONObject body = Helpers.createJsonObject("token", token);

        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/check-token", body, success, error));
    }

    public void getAllProdutos(final String token, Response.Listener success, Response.ErrorListener error) throws Exception {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produtos/", null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        });
    }

    public void getProdutosLike(String filter, final String token, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produtos/" + Uri.encode(filter), null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        });
    }

    public void getProduto(int idProduto, final String token, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.GET, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        });
    }

    public void newProduto(JSONObject dadosProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.POST, BASE_PATH + "/produto", dadosProduto, success, error));
    }

    public void setProduto(int idProduto, JSONObject dadosProduto, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.PUT, BASE_PATH + "/produto/" + idProduto, dadosProduto, success, error));
    }

    public void deleteProduto(int idProduto, final String token, Response.Listener success, Response.ErrorListener error) {
        this.queue.add(new JsonObjectRequest(Request.Method.DELETE, BASE_PATH + "/produto/" + idProduto, null, success, error) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        });
    }
}
