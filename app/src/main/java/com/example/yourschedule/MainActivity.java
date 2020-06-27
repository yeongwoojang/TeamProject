package com.example.yourschedule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.yourschedule.FRAGMENT.MyList;
import com.example.yourschedule.FRAGMENT.ScheduleList;
import com.google.android.material.tabs.TabLayout;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.message.template.TemplateParams;
import com.kakao.message.template.TextTemplate;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.exception.KakaoException;

public class MainActivity extends AppCompatActivity {

    private final int FRAGMENT1 = 0;
    private final int FRAGMENT2 = 1;
    private final int FRAGMENT3 = 2;
    private final String[] bottomTab = {"일정관리", "날씨정보", "추가예정"};

    private Context mContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,LoadingActivity.class));
        setContentView(R.layout.activity_main);

        mContext = this;
        Session.getCurrentSession().addCallback(sessionCallback);

        final TabLayout bottom_tabs = (TabLayout) findViewById(R.id.bottom_tabs);

        for (int i = 0; i < bottomTab.length; i++) {
            bottom_tabs.addTab(bottom_tabs.newTab());
            TextView view = new TextView(this);
            view.setGravity(bottom_tabs.GRAVITY_CENTER);
            view.setText(bottomTab[i]);
//            view.setTypeface(Typeface.createFromAsset(getAssets(), "font/myfont.ttf"));
            bottom_tabs.getTabAt(i).setCustomView(view);

        }
        bottom_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        bottom_tabs.getTabAt(FRAGMENT1).setTag(FRAGMENT1);
        bottom_tabs.getTabAt(FRAGMENT2).setTag(FRAGMENT2);
        bottom_tabs.getTabAt(FRAGMENT3).setTag(FRAGMENT3);

        bottom_tabs.setVisibility(View.INVISIBLE);


        bottom_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
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

            }
        });
    }

    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            default:

            case FRAGMENT1:
                // '프래그먼트1' 호출
                ScheduleList accountBook = new ScheduleList();
                transaction.replace(R.id.fragment_container, accountBook);
                transaction.commit();
                break;

            case FRAGMENT2:
                // '프래그먼트2' 호출
                MyList graph = new MyList();
                transaction.replace(R.id.fragment_container, graph);
                transaction.commit();
                break;
        }
    }

    private ISessionCallback sessionCallback = new ISessionCallback() {
        @Override
        public void onSessionOpened() {
            Log.i("KAKAO_SESSION", "로그인 성공");
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("KAKAO_SESSION", "로그인 실패", exception);
        }
    };
}