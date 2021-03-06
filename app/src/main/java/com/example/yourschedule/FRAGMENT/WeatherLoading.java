package com.example.yourschedule.FRAGMENT;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.GpsTracker;
import com.example.yourschedule.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WeatherLoading extends Fragment {

    public WeatherLoading newInstance() {
        return new WeatherLoading();
    }
    private ImageView runningImage;

    private JsonObject jsonObject;
    private double longitude;
    private double latitude;
    private Fragment fragment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.weather_loading, container, false);
        runningImage = rootView.findViewById(R.id.running_image);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final AnimationDrawable drawable =
                (AnimationDrawable) runningImage.getBackground();
        drawable.start();

            GpsTracker gpsTracker;
            gpsTracker = new GpsTracker(getActivity());
            latitude = gpsTracker.getLatitude(); // 위도
            longitude = gpsTracker.getLongitude(); //경도
            Log.d("위치", latitude + " : " + longitude + "");

            getWeekWeather(String.valueOf(latitude), String.valueOf(longitude), "minutely,current", "metric", "650b8470989fceb2f4a95b3241a76d65");

    }

    public void getWeekWeather(String mlatitude, String mlongitude, String exclude, String units, String OPEN_WEATHER_MAP_KEY) {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();

        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getWeekWeather(mlatitude, mlongitude, exclude, units, OPEN_WEATHER_MAP_KEY);

        final AnimationDrawable drawable =
                (AnimationDrawable) runningImage.getBackground();
        drawable.start();
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObject = response.body();
                drawable.stop();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragment = new WeatherForm().newInstance();
                Bundle bundle = new Bundle();
                bundle.putDouble("longitude", longitude);
                bundle.putDouble("latitude", latitude);
                Gson gson = new Gson();
                String bundleJson = gson.toJson(jsonObject);
                bundle.putString("jsonObject", bundleJson);
                fragment.setArguments(bundle);
                if(checkGPSPermission()){
                    fragmentTransaction.replace(R.id.form_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commitAllowingStateLoss();
                    runningImage.setVisibility(View.INVISIBLE);
                }

            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
            }
        });
    }

    public boolean checkGPSPermission(){
        boolean permissionCheck = false;
        int locationPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(locationPermissionCheck == PackageManager.PERMISSION_DENIED && coarsePermissionCheck == PackageManager.PERMISSION_DENIED){
            permissionCheck = false;
        }
        else if(locationPermissionCheck == PackageManager.PERMISSION_GRANTED){
            permissionCheck = true;
        }
        return permissionCheck;
    }
}
