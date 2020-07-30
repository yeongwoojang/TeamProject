package com.example.yourschedule;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("weather?")
    Call<JsonObject> getCurrentWeather(
             @Query("lat") String lat,
            @Query("lon")String lon,
            @Query("APPID")String APPID
    );
    @GET("forecast")
    Call<JsonObject> getDailyForecast(
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("units") String units,
            @Query("exclude") String exclude,
            @Query("cnt") String cnt,
            @Query("lang") String lang,
            @Query("APPID") String APPID
    );
}
