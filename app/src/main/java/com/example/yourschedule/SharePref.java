package com.example.yourschedule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SharePref {
    //getStringArrayPref
    String PREFERENCE = "com.example.yourschedule.FRAGMENT";

    public ArrayList<String> get(Activity activity, String key) {
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        String json = pref.getString(key, null);

        Log.d("json ", json+"");

        ArrayList<String> urls = new ArrayList<String>();
        JSONObject jsonObject = null;
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                }
                Iterator iterator = jsonObject.keys();
                while(iterator.hasNext()){
                    String url = (String)iterator.next();
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    public void set(Activity activity, String key, ArrayList<String[]> values){
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(int i=0;i<values.size();i++){
            for(int j=1;j<values.get(i).length;j++){
                try{
                    jsonObject.put(values.get(i)[0],values.get(i)[j]);
                }catch (JSONException e){}
            }
        }
        jsonArray.put(jsonObject);
        if(!values.isEmpty()){
            editor.putString(key,jsonArray.toString());
        }else{
            editor.putString(key,null);
        }
        editor.apply();
    }
}
