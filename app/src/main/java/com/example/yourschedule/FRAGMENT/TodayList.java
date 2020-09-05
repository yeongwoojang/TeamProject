package com.example.yourschedule.FRAGMENT;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
//import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.ForRetrofit.RetrofitClient;
import com.example.yourschedule.SharePref;
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
    TextView dayOfWeek, dateText,signOutBt;
    DrawerLayout settingViewLayout;
    View settingView;
    String[] days = new String[]{"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_today_list, container, false);
        t1 = rootView.findViewById(R.id.detailDate);
        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dateText = rootView.findViewById(R.id.date);
        settingBt = rootView.findViewById(R.id.settingBt);
        signOutBt = rootView.findViewById(R.id.signOutBt);
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

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        getCurrentWeather("37.57", "126.98", "7d25a27ec2361e69dcbb04d90feb6b23");
//        getDailyForecast("37.57","126.98","metric","hourly","7","kr","7d25a27ec2361e69dcbb04d90feb6b23");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = transFormat.format(calendar.getTime());
        String day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        dayOfWeek.setText(day);
        dateText.setText(today);
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        Log.d("today", today);
        SharePref sharePref = new SharePref();
        scheduleDTOS.addAll(sharePref.getEntire(getActivity()));
//        Log.d("asdf",scheduleDTOS.get(0).getSchedule()+"");
//        try{
//            mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
//                    .addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                                scheduleDTOS.add(scheduleDTO);
//                            }
//                            recyclerViewAdapter.notifyDataSetChanged();
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//        }catch (Exception e){
//
//        }

//           logoutBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//
//                Toast.makeText(getActivity(),"로그아웃되었습니다.",Toast.LENGTH_SHORT).show();
//                logoutListener.finish(child);
//
//            }
//        });
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
//                Log.d("logout",auth.getCurrentUser().getDisplayName()+"a");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.remove(fragment);
//                ft.addToBackStack(null);
//                ft.commitAllowingStateLoss();
                logoutListener.finish(getParentFragment());
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                startActivity(intent);
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

            }
        });
    }

    public void getDailyForecast(String latitude, String longitude, String units, String exclude, String cnt, String lang, String OPEN_WEATHER_MAP_KEY) {
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();
        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getDailyForecast(latitude, longitude, units, exclude, cnt, lang, OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONArray jsonArray;
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    jsonArray = jsonObject.getJSONArray("daily");
//                    calendar.setTimeInMillis(Integer.parseInt(jsonObject.getString("dt"))*1000L);
//                    Calendar calendar = Calendar.getInstance();
//                    TimeZone tz = TimeZone.getDefault();
//                    calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
//                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//                    java.util.Date currenTimeZone=new java.util.Date((long)Integer.parseInt(jsonObject.getString("dt"))*1000);
//                    Log.d("fdghdgfhdfgh",jsonObject.getString("dt"));
//                    Log.d("dsafsafsadf",sdf.format(currenTimeZone));
//                    Log.d("JSON",jsonObject+"");
//                    jsonArray = jsonObject.getJSONArray("weather");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

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
