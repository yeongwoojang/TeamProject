package com.example.yourschedule;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SharePref {
    String PREFERENCE = "Schedule_List_For_Widget";
    List<ScheduleDTO> scheduleDTOS;
    ScheduleDTO scheduleDTO;

//    public SharePref(Context context) {
//        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        ObjectMapper mapper = new ObjectMapper();
//
//        try {
//            String jsonStr = mapper.writeValueAsString(scheduleDTO);
//            JSONObject jsonObject = new JSONObject(jsonStr);
//            editor.putString(jsonObject.get("date")+"Key",jsonStr);
//            Log.d("dghdgh", jsonObject + "");
//            editor.apply();
//        } catch (Exception e) {}
//    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public ScheduleDTO get(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        String json = pref.getString(key+"Key", null);
        Log.d("key",key);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        if(json!=null) {
            try {

                scheduleDTO = mapper.readValue(json, ScheduleDTO.class);
                Log.d("DTO", scheduleDTO.getSchedule().get(0) + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return scheduleDTO;
        }else{
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public List<ScheduleDTO> getEntire(Context context) {

        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);

        Map<String,?> json = pref.getAll();
        Log.d("json",json+"");
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        for(Map.Entry<String,?> entry : json.entrySet()){
            Log.d("adsfadsf",entry.getKey()+" : "+ entry.getValue());
            try {
                scheduleDTOS.add(mapper.readValue((String) entry.getValue(),ScheduleDTO.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return scheduleDTOS;
    }

    public void FireBaseToSharedPref(Context context, List<ScheduleDTO> scheduleDTOS) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(scheduleDTOS);
            JSONArray jsonArray = new JSONArray(jsonStr);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                editor.putString(jsonArray.getJSONObject(i).get("date")+"Key", jsonArray.getJSONObject(i)+"");
                Log.d("jsonObject", jsonObject + "");
                editor.apply();
            }
        } catch (Exception e) {}
    }


    public void addToShardPref(Context context, ScheduleDTO scheduleDTO) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonStr = mapper.writeValueAsString(scheduleDTO);
            JSONObject jsonObject = new JSONObject(jsonStr);
            editor.putString(jsonObject.get("date")+"Key",jsonStr);
            Log.d("dghdgh", jsonObject + "");
            editor.apply();
        } catch (Exception e) {}
    }

    public void deleteShardPref(Context context,String dateKey) {
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();



        try {
            Log.d("delete",pref.getString(dateKey,"No"));
            editor.remove(dateKey);
            editor.apply();
        } catch (Exception e) {}
    }

    public void deletaAll(Context context){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    public void updateChk(Context context,int position){
        SharedPreferences pref = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
    }
}
