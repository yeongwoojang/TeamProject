package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourschedule.R;
import com.google.android.material.tabs.TabLayout;


public class ScheduleList extends Fragment implements MyList.logoutListener{

    private final int FRAGMENT1 = 0;
    private final int FRAGMENT2 = 1;
    private final int FRAGMENT3 = 2;


    private final String[] topTab = {"오늘의 일정", "달력", "월간 목표 달성률","주간 목표 달성률"};

    public ScheduleList newInstance() {
        return new ScheduleList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_schedule_list, container, false);
        final TabLayout top_tabs = rootView.findViewById(R.id.top_tabs);
        for (int i = 0; i < topTab.length; i++) {
            top_tabs.addTab(top_tabs.newTab());
            TextView view = new TextView(getActivity());
            view.setGravity(top_tabs.GRAVITY_CENTER);
            view.setText(topTab[i]);
//            view.setTypeface(Typeface.createFromAsset(getAssets(), "font/myfont.ttf"));
            top_tabs.getTabAt(i).setCustomView(view);

        }
        top_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        top_tabs.getTabAt(0).setTag(0);
        top_tabs.getTabAt(1).setTag(1);
        top_tabs.getTabAt(2).setTag(2);
        top_tabs.getTabAt(3).setTag(3);
        Fragment fragment;
        fragment = new MyList().newInstance();
        setChildFragment(fragment);

        top_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment;
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {


                    case 0:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        fragment = new MyList().newInstance();
                        setChildFragment(fragment);
                        break;

                    case 1:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        fragment = new Calandar().newInstance();
                        setChildFragment(fragment);
                        break;

                    case 2:
                        fragment = new MonthAchievementRate().newInstance();
                        setChildFragment(fragment);
                        break;

                    case 3:
                        fragment = new WeekAchievementRate().newInstance();
                        setChildFragment(fragment);
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

        return rootView;
    }


    private void setChildFragment(Fragment child) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        if (!child.isAdded()) {
            fragmentTransaction.replace(R.id.fragment_container, child);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void finish(Fragment child) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(child).commit();
    }
}
