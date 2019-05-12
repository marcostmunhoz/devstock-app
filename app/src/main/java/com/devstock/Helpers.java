package com.devstock;

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
}
