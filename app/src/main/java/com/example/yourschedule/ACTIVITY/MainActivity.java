package com.example.yourschedule.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourschedule.FRAGMENT.ScheduleList;
import com.example.yourschedule.FRAGMENT.TodayList;
import com.example.yourschedule.FRAGMENT.WeatherOfWeek;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TodayList.LogoutListener {


    private final int FRAGMENT1 = 0;
    private final int FRAGMENT2 = 1;
    private final int FRAGMENT3 = 2;
    private final String[] bottomTab = {"일정", "날씨", "추가예정"};
    private TabLayout bottom_tabs;

    private Context mContext = null;
    private List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    private long backKeyPressed = 0;
    private Toast backBtClickToast;

    private ImageView titleImage, appLogoImage,runningImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("oncreate","onCreate");
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mContext = this;
        auth = FirebaseAuth.getInstance();
        titleImage = (ImageView)findViewById(R.id.title_image);
        appLogoImage = (ImageView)findViewById(R.id.app_logo_image);
        runningImage = (ImageView)findViewById(R.id.running_image);
        bottom_tabs = (TabLayout) findViewById(R.id.bottom_tabs);




        for (int i = 0; i < bottomTab.length; i++) {
            bottom_tabs.addTab(bottom_tabs.newTab());
            TextView view = new TextView(this);
            view.setGravity(bottom_tabs.GRAVITY_CENTER);
            view.setTextColor(getResources().getColor(R.color.white));
            view.setTypeface(Typeface.createFromAsset(getAssets(), "font/baemin.ttf"));
            view.setText(bottomTab[i]);
            bottom_tabs.getTabAt(i).setCustomView(view);
        }

        bottom_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        bottom_tabs.getTabAt(FRAGMENT1).setTag(FRAGMENT1);
        bottom_tabs.getTabAt(FRAGMENT2).setTag(FRAGMENT2);
        bottom_tabs.getTabAt(FRAGMENT3).setTag(FRAGMENT3);


//        SharePref sharePref = new SharePref();
//        sharePref.deletaAll(getApplicationContext());


//        readDatabase(new DataLoadCallBack() {
//            @Override
//            public void onCallback(List<ScheduleDTO> value) {
//                scheduleDTOS.clear();
//                if (value.size() != 0) {
//                    scheduleDTOS = value;
//                    sharePref.FireBaseToSharedPref(mContext, scheduleDTOS);
//                }
//            }
//        });

        bottom_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                titleImage.setVisibility(View.INVISIBLE);
                appLogoImage.setVisibility(View.INVISIBLE);
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
                    case FRAGMENT1:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        callFragment(FRAGMENT1);
                        break;

                    case FRAGMENT2:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        callFragment(FRAGMENT2);
                        break;

                    case FRAGMENT3:
                        callFragment(FRAGMENT3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                titleImage.setVisibility(View.INVISIBLE);
                appLogoImage.setVisibility(View.INVISIBLE);
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
                    case FRAGMENT1:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        callFragment(FRAGMENT1);
                        break;

                    case FRAGMENT2:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        callFragment(FRAGMENT2);
                        break;

                    case FRAGMENT3:
                        callFragment(FRAGMENT3);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (auth.getCurrentUser() != null) {
            if (System.currentTimeMillis() > backKeyPressed + 2000) {
                backKeyPressed = System.currentTimeMillis();
                backBtClickToast = Toast.makeText(this, "\'뒤로가기\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                backBtClickToast.show();
                return;
            }
            if (System.currentTimeMillis() <= backKeyPressed + 2000) {
                finishAffinity();
                backBtClickToast.cancel();
            }
        } else {
            Log.d("logout",auth.getCurrentUser().getDisplayName()+" is Out");


            super.onBackPressed();
        }
    }

    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            default:

            case FRAGMENT1:
                // '프래그먼트1' 호출
                ScheduleList scheduleList = new ScheduleList();
                transaction.replace(R.id.main_fragment_container, scheduleList);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case FRAGMENT2:
                // '프래그먼트2' 호출
                WeatherOfWeek weatherOfWeek = new WeatherOfWeek();
                transaction.replace(R.id.main_fragment_container, weatherOfWeek);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

//    public void readDatabase(DataLoadCallBack dataLoadCallBack) {
//        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();
//        mReference = mDatabase.getReference("일정");
//        auth = FirebaseAuth.getInstance();
//        Log.d("readUserName",auth.getCurrentUser().getDisplayName());
//        mReference.child(auth.getCurrentUser().getDisplayName())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        scheduleDTOSTemp.clear();
//
//
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                                scheduleDTOSTemp.add(scheduleDTO);
//                        }
//
//                        dataLoadCallBack.onCallback(scheduleDTOSTemp);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//    }

    @Override
    public void finish(Fragment child) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.remove(child).commit();
        Intent goToLogInActivity = new Intent(getApplicationContext(),LoginActivity.class);

        startActivity(goToLogInActivity);
        Intent intent = getIntent();
        setResult(RESULT_OK,intent);
        finish();
    }


    //    public interface DataLoadCallBack {
//        void onCallback(List<ScheduleDTO> value);
//    }

}