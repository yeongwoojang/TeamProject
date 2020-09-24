package com.example.yourschedule.FRAGMENT;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.yourschedule.ListWidgetProvider;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
//import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.SharePref;
import com.example.yourschedule.WidgetRemoteViewsService;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TodayList extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    ImageButton settingBt, closeSettingBt;
    TextView dayOfWeek, dateText,signOutBt,appUnlinkBt;
    DrawerLayout settingViewLayout;
    View settingView;
    String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    TextView t1;

    ImageView weatherIcon;
    LogoutListener logoutListener;




    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        logoutListener = (LogoutListener) context;
    }

    private Fragment fragment;

    public TodayList newInstance() {
        return new TodayList();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onWhat","onResume");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = transFormat.format(calendar.getTime());
        SharePref sharePref = new SharePref();
        scheduleDTOS.clear();
        scheduleDTOS.addAll(sharePref.getEntire(getActivity()));
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), scheduleDTOS, today);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onWhat","onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_today_list, container, false);
        t1 = rootView.findViewById(R.id.detailDate);
        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dateText = rootView.findViewById(R.id.date);
        settingBt = rootView.findViewById(R.id.settingBt);
        signOutBt = rootView.findViewById(R.id.signOutBt);
        appUnlinkBt = rootView.findViewById(R.id.appUnlinkBt);
        settingViewLayout = rootView.findViewById(R.id.settingLayout);
        settingView = rootView.findViewById(R.id.settingDetail);
        closeSettingBt = rootView.findViewById(R.id.closeSettingBt);
//        logoutBt = rootView.findViewById(R.id.logoutBt);
//        shareBt = rootView.findViewById(R.id.shareBt);
        fragment = this;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("onWhat","onViewCreated");
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        getCurrentWeather("37.57", "126.98", "7d25a27ec2361e69dcbb04d90feb6b23");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = transFormat.format(calendar.getTime());
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayOfWeek.setText(day);
        dateText.setText(today);

//        SharePref sharePref = new SharePref();
//        scheduleDTOS.addAll(sharePref.getEntire(getActivity()));
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), scheduleDTOS, today);
        recyclerView.setAdapter(recyclerViewAdapter);

        settingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingViewLayout.openDrawer(settingView);
            }
        });
        closeSettingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingViewLayout.closeDrawer(settingView);
            }
        });

        signOutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharePref sharePref = new SharePref();
                sharePref.deletaAll(getActivity());
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getActivity());
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(getActivity(), ListWidgetProvider.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
                logoutListener.finish(getParentFragment());
            }
        });
        appUnlinkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }

    public void getCurrentWeather(String latitude, String longitude, String OPEN_WEATHER_MAP_KEY) {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();
        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getCurrentWeather(latitude, longitude, OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONArray jsonArray;
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    jsonArray = jsonObject.getJSONArray("weather");
                    Log.d("sfdgsdgsdfg",jsonArray+"");
                    jsonObject = jsonArray.getJSONObject(0);
//                    String weather = jsonObject.getString("description");
//                    t1.setText(weather);
                    String icon = jsonObject.getString("icon");
                    String iconUrl = "http://openweathermap.org/img/wn/" + icon + ".png";
                    Glide.with(getActivity()).load(iconUrl).into(weatherIcon);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("Ddfgsdg","실패");
            }
        });
    }


    public interface LogoutListener {
        void finish(Fragment child);
    }

    public void LogoutResult(LogoutListener logoutListener) {
        this.logoutListener = logoutListener;
    }


}
