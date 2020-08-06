package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.RetrofitClient;
import com.example.yourschedule.RetrofitService;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherOfWeek extends Fragment {

    private Retrofit mRetrofit;
    private RetrofitService mRetrofitService;
    private Call<JsonObject> mCallWeekWeather;

    private String jsonObject;

    public WeatherOfWeek newInstance() {
        return new WeatherOfWeek();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetrofitInit();
        callWeekWeather();
        Log.d("json", "WeatherOfWeek onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("json", "WeatherOfWeek onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_weather_of_week, container, false);


        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("json", "WeatherOfWeek onViewCreated");
    }

    private void setRetrofitInit() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofitService = mRetrofit.create(RetrofitService.class);
    }

    private void callWeekWeather() {
        mCallWeekWeather = mRetrofitService.getWeekWeather(
                "37.56826",
                "126.977829",
                "daily",
                "c0f3c4eaaf1be0001005e2a2973e33f4"
        );
        mCallWeekWeather.enqueue(mRetrofitCallback);
    }

    private Callback<JsonObject> mRetrofitCallback = new Callback<JsonObject>(){
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            jsonObject = response.body().toString();
            System.out.println(jsonObject);
            Log.d("JSON W", jsonObject + "");
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("JSON W", "실패");
        }
    };

}
