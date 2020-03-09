package com.ravel.puzzle;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Utils {


    public static void savePuzzle(Context mContext, JSONObject mJSONObject) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(mContext.getPackageName()
                , Context.MODE_PRIVATE);
        JSONArray savedPuzzles = getSavedPuzzles(mContext);
        savedPuzzles.put(mJSONObject);
        SharedPreferences.Editor edit = sharedpreferences.edit();
        edit.putString("data", savedPuzzles.toString());
        edit.commit();
    }

    public static JSONArray getSavedPuzzles(Context mContext) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(mContext.getPackageName()
                , Context.MODE_PRIVATE);
        String data = sharedpreferences.getString("data", "");
        if (data.length() != 0) {
            try {
                return new JSONArray(data);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new JSONArray();
        }
    }
    public static void deletePuzzle(Context mContext) {
        SharedPreferences mySharedPreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.remove("data");
        editor.commit();
        }
}
