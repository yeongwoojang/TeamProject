package com.example.yourschedule.ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yourschedule.FRAGMENT.ScheduleList;
import com.example.yourschedule.FRAGMENT.TodayList;
import com.example.yourschedule.FRAGMENT.WeatherLoading;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements TodayList.LogoutListener {


    private final int FRAGMENT1 = 0;
    private final int FRAGMENT2 = 1;
    private final String[] bottomTab = {"일정", "날씨"};


    private int[] tabIcons = {
            R.drawable.baseline_date_range_white_18,
            R.drawable.baseline_cloud_queue_white_18
    };
    private TabLayout bottomTabs;
    private RelativeLayout appTitle;

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
        runningImage = (ImageView)findViewById(R.id.running_image);
        bottomTabs = (TabLayout) findViewById(R.id.bottom_tabs);
        appTitle = (RelativeLayout)findViewById(R.id.app_title);



        for (int i = 0; i < bottomTab.length; i++) {
            bottomTabs.addTab(bottomTabs.newTab());

        }

        bottomTabs.getTabAt(FRAGMENT1).setTag(FRAGMENT1);
        bottomTabs.getTabAt(FRAGMENT1).setIcon(tabIcons[0]);
        bottomTabs.getTabAt(FRAGMENT2).setTag(FRAGMENT2);
        bottomTabs.getTabAt(FRAGMENT2).setIcon(tabIcons[1]);


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

        bottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {


            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                appTitle.setVisibility(View.INVISIBLE);
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
                    case FRAGMENT1:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        callFragment(FRAGMENT1);
                        break;

                    case FRAGMENT2:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        callFragment(FRAGMENT2);
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                appTitle.setVisibility(View.INVISIBLE);
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
                    case FRAGMENT1:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        callFragment(FRAGMENT1);
                        break;

                    case FRAGMENT2:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        callFragment(FRAGMENT2);
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
                WeatherLoading weatherLoading = new WeatherLoading();
                transaction.replace(R.id.main_fragment_container, weatherLoading);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }



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