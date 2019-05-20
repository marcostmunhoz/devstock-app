package com.devstock;

import android.app.Activity;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static void verificarSessao(Activity act, Response.Listener success, final Response.ErrorListener onError) throws Exception {
        Context ctx = act.getApplicationContext();
        final SharedPreferences pref = act.getSharedPreferences("devstock_prefs", Context.MODE_PRIVATE);
        ApiHandler handler = ApiHandler.getInstance(act);

        if (pref.contains("token")) {
            String token = pref.getString("token", "");
            handler.validateToken(token, success, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("token");
                    editor.apply();

                    if (onError != null) {
                        onError.onErrorResponse(error);
                    }
                }
            });
        }
    }
}
