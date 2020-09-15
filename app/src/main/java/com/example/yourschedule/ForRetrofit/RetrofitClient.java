package com.example.yourschedule.ForRetrofit;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

    private RetrofitClient retrofitClient;
    private JSONObject jsonObject;
    public RetrofitClient getInstance( ){
        retrofitClient = new RetrofitClient();
        return retrofitClient;
    }

    public RetrofitService buildRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }

//    public void getCurrentWeather(String latitude, String longitude, String OPEN_WEATHER_MAP_KEY){
//        Call<JsonObject> response = getInstance()
//                .buildRetrofit()
//                .getCurrentWeather(latitude,longitude,OPEN_WEATHER_MAP_KEY);
//        response.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                    Log.d("JSON",jsonObject+"");
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
//    }

}


