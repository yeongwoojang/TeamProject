package com.example.yourschedule;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourschedule.FRAGMENT.MyList;
import com.example.yourschedule.FRAGMENT.ScheduleList;
import com.google.android.material.tabs.TabLayout;

public class Main_menu extends AppCompatActivity {

//    private final int FRAGMENT1 = 0;
//    private final int FRAGMENT2 = 1;
//    private final int FRAGMENT3 = 2;
//    private final String[] bottomTab = {"일정관리", "날씨정보", "추가예정"};
//
//    private Context mContext = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_menu);
//
//        mContext = this;
//
//        final TabLayout bottom_tabs = (TabLayout) findViewById(R.id.bottom_tabs);
//
//        for (int i = 0; i < bottomTab.length; i++) {
//            bottom_tabs.addTab(bottom_tabs.newTab());
//            TextView view = new TextView(this);
//            view.setGravity(bottom_tabs.GRAVITY_CENTER);
//            view.setText(bottomTab[i]);
////            view.setTypeface(Typeface.createFromAsset(getAssets(), "font/myfont.ttf"));
//            bottom_tabs.getTabAt(i).setCustomView(view);
//
//        }
//        bottom_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
//        bottom_tabs.getTabAt(0).setTag(0);
//        bottom_tabs.getTabAt(1).setTag(1);
//        bottom_tabs.getTabAt(2).setTag(2);
//
//
//
//
//        bottom_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
//                    case 0:
//                        // '버튼1' 클릭 시 '프래그먼트1' 호출
//                        callFragment(FRAGMENT1);
//                        break;
//
//                    case 1:
//                        // '버튼2' 클릭 시 '프래그먼트2' 호출
//                        callFragment(FRAGMENT2);
//                        break;
//
//                    case 2:
//                        callFragment(FRAGMENT3);
//                        break;
//                }
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//        callFragment(FRAGMENT1);
//
//    }
//
//    private void callFragment(int frament_no) {
//
//        // 프래그먼트 사용을 위해
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//        switch (frament_no) {
//            default:
//
//            case 0:
//                // '프래그먼트1' 호출
//                ScheduleList accountBook = new ScheduleList();
//                transaction.replace(R.id.fragment_container, accountBook);
//                transaction.commit();
//                break;
//
//            case 1:
//                // '프래그먼트2' 호출
//                MyList graph = new MyList();
//                transaction.replace(R.id.fragment_container, graph);
//                transaction.commit();
//                break;
//
////            case 2:
////                // BP 데이터 저장/불러오기
////                BP bp = new BP();
////                transaction.replace(R.id.fragment_container, bp);
////                transaction.commit();
////                break;
//
//        }
//    }
}
