package com.devstock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devstock.handlers.ApiHandler;
import com.devstock.models.BaseModel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Iterator;

public class Helpers {
    private static JSONArray flattenJsonArray(JSONArray array) throws JSONException {
        JSONArray result = new JSONArray();

        for (int i = 0; i < array.length(); i++) {
            Object arrayItem = array.get(i);

            if (arrayItem instanceof JSONArray) {
                result.put(flattenJsonArray((JSONArray) arrayItem));
            } else if (arrayItem instanceof JSONObject) {
                result.put(flattenJsonObject((JSONObject) arrayItem, null, null));
            } else {
                result.put(arrayItem);
            }
        }

        return result;
    }

    public static JSONObject flattenJsonObject(JSONObject obj, String refKey, JSONObject refObj) throws JSONException {
        if (refKey == null) {
            refKey = "";
        }

        if (refObj == null) {
            refObj = new JSONObject();
        }

        Iterator<String> keys = obj.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = obj.get(key);
            key = (!refKey.isEmpty() ? refKey + '.' + key : key);

            if (value instanceof JSONObject) {
                flattenJsonObject((JSONObject) value, key, refObj);
            } else if (value instanceof JSONArray) {
                refObj.put(key, flattenJsonArray((JSONArray) value));
            } else {
                refObj.put(key, value);
            }
        }

        return refObj;
    }

    public static JSONObject createJsonObject(Object ...args) throws Exception {
        JSONObject result = new JSONObject();
        String key = null;

        for (Object obj : args) {
            if (key == null) {
                if (!(obj instanceof String)) {
                    throw new Exception("Key must be a string");
                } else {
                    key = (String) obj;
                    continue;
                }
            } else {
                result.put(key, obj);
                key = null;
            }
        }

        if (key != null) {
            throw new Exception("Incomplete key/value pair for key " + key);
        }

        return result;
    }

    public static String getPrefs(Context ctx, String name) {
        SharedPreferences pref = ctx.getSharedPreferences("devstock_prefs", Context.MODE_PRIVATE);

        if (pref.contains(name)) {
            return pref.getString(name, null);
        } else {
            return null;
        }
    }

    public static void setPrefs(Context ctx, String name, String value) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences("devstock_prefs", Context.MODE_PRIVATE).edit();

        editor.putString(name, value);
        editor.commit();
    }

    public static void removePrefs(Context ctx, String name) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences("devstock_prefs", Context.MODE_PRIVATE).edit();

        editor.remove(name);
        editor.commit();
    }

    public static void verificarSessao(final Activity act, Response.Listener success, final Response.ErrorListener onError) throws Exception {
        ApiHandler handler = ApiHandler.getInstance(act);
        String token = getPrefs(act, "AUTH_TOKEN");

        handler.validateToken(token, success, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiHandler.setToken(null);
                ApiHandler.setUser(null);

                if (onError != null) {
                    onError.onErrorResponse(error);
                }
            }
        });
    }

    public static ProgressDialog showLoading(Context ctx, String text) {
        ProgressDialog dialog = new ProgressDialog(ctx);
        dialog.setTitle("Por favor, aguarde");
        dialog.setMessage(text);
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    public static <T> T deserialize(String data, Class<T> target) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        JsonElement element = new JsonParser().parse(data);

        if (element.isJsonObject()) {
            JsonObject obj = element.getAsJsonObject();

            if (obj.has("data")) {
                element = obj.get("data");
            }
        }

        return gson.fromJson(element, target);
    }

    public static JSONObject serialize(BaseModel model, Class source) throws Exception {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .create();

        return new JSONObject(gson.toJson(model, source));
    }

    public static String dateFromString(String date) throws Exception {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                formatter = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");

        return formatter.format(parser.parse(date));
    }

    public static String formatString(String mask, String value) {
        try {
            String result = "";
            int index = 0;

            for (char c : mask.toCharArray()) {
                if (c == '#') {
                    result += value.charAt(index);
                    index++;
                } else {
                    result += c;
                }
            }

            return result;
        } catch (Exception ex) {
            return value;
        }
    }
}
