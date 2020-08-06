package com.example.yourschedule;

import android.util.Log;

import com.example.yourschedule.FRAGMENT.MyList;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient{

    private RetrofitClient retrofitClient;
    private String jsonObject;
    public RetrofitClient getInstance(){
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

    public void getCurrentWeather(String latitude, String longitude, String OPEN_WEATHER_MAP_KEY){
        Call<JsonObject> response = getInstance()
                .buildRetrofit()
                .getCurrentWeather(latitude,longitude,OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObject = response.body().toString();
                    Log.d("JSON C",jsonObject+"");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("JSON C","실패");
            }
        });
    }

    public void getWeekWeather(String latitude, String longitude, String exclude, String OPEN_WEATHER_MAP_KEY){
        Call<JsonObject> response = getInstance()
                .buildRetrofit()
                .getWeekWeather(latitude, longitude, exclude, OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObject = response.body().toString();
                Log.d("JSON W",jsonObject+"");
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("JSON W","실패");
            }
        });
    }

}


