package com.example.yourschedule.FRAGMENT;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
//import com.example.yourschedule.RetrofitClient;
import com.example.yourschedule.RetrofitClient;
import com.example.yourschedule.RetrofitService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import kotlin.jvm.internal.Intrinsics;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyList extends Fragment {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    ImageButton settingBt,closeSettingBt;
    TextView dayOfWeek,dateText,logoutBt,shareBt;
    DrawerLayout settingViewLayout;
    View settingView;
    String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    logoutListener logoutListener;
    MyList child;
    TextView t1;
    ImageView weatherIcon;

    public MyList newInstance() {
        return new MyList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        t1 = rootView.findViewById(R.id.test);
        weatherIcon = rootView.findViewById(R.id.weatherIcon);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dateText = rootView.findViewById(R.id.date);
//        settingBt = rootView.findViewById(R.id.settingBt);
        settingViewLayout = rootView.findViewById(R.id.settingLayout);
        settingView = rootView.findViewById(R.id.settingDetail);
        closeSettingBt = rootView.findViewById(R.id.closeSettingBt);
        logoutBt = rootView.findViewById(R.id.logoutBt);
        shareBt = rootView.findViewById(R.id.shareBt);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);
        getCurrentWeather("37.57","126.98","7d25a27ec2361e69dcbb04d90feb6b23");
//        getDailyForecast("37.57","126.98","metric","hourly","7","kr","7d25a27ec2361e69dcbb04d90feb6b23");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = transFormat.format(calendar.getTime());
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];
        dayOfWeek.setText(day);
        dateText.setText(today);
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        try{
            mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
//                .child(today.replace(".","-"))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                                scheduleDTOS.add(scheduleDTO);
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }catch (Exception e){

        }

//        settingBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                settingViewLayout.openDrawer(settingView);
//            }
//        });
//        closeSettingBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                settingViewLayout.closeDrawer(settingView);
//            }
//        });

        //여기서 일단 오늘 일정 공유테스트 ㄱㄱ
//        shareBt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FeedTemplate params = FeedTemplate
//                        .newBuilder(ContentObject.newBuilder("일정이 도착했습니다.",
//                                "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
//                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
//                                        .setMobileWebUrl("https://developers.kakao.com").build())
//                                .build())
//                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
//                                .setWebUrl("https://developers.kakao.com")
//                                .setMobileWebUrl("https://developers.kakao.com")
//                                .setAndroidExecutionParams("user="+auth.getCurrentUser().getDisplayName())
//                                .setIosExecutionParams("key1=value1")
//                                .build()))
//                        .build();
//                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
//                serverCallbackArgs.put("user_id", auth.getCurrentUser().getDisplayName());
//
//                KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
//                    @Override
//                    public void onFailure(ErrorResult errorResult) {
//                        Log.d("TEST","FAILED");
//                    }
//
//                    @Override
//                    public void onSuccess(KakaoLinkResponse result) {
//                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
//                        Log.d("TEST","SUCCESS");
//                    }
//                });
//            }
//        });
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
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), scheduleDTOS,today);
        recyclerView.setAdapter(recyclerViewAdapter);

    }
    public void getCurrentWeather(String latitude, String longitude, String OPEN_WEATHER_MAP_KEY){
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();
        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getCurrentWeather(latitude,longitude,OPEN_WEATHER_MAP_KEY);
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
                    String iconUrl = "http://openweathermap.org/img/wn/" + icon+ ".png";
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

    public void getDailyForecast(String latitude, String longitude,String units,String exclude,String cnt,String lang,String OPEN_WEATHER_MAP_KEY){
        RetrofitClient retrofitClient = new RetrofitClient();
        retrofitClient.buildRetrofit();
        Call<JsonObject> response = retrofitClient.getInstance()
                .buildRetrofit()
                .getDailyForecast(latitude,longitude,units,exclude,cnt,lang,OPEN_WEATHER_MAP_KEY);
        response.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JSONArray jsonArray;
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    Log.d("JSONDAILY",jsonObject+"");
                    jsonArray = jsonObject.getJSONArray("daily");
                    Log.d("daily",jsonArray+"");
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
    public interface logoutListener {
        void finish(Fragment child);
    }

}
