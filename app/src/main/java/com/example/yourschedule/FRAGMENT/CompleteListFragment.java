package com.example.yourschedule.FRAGMENT;


import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.RateAdapter;
import com.example.yourschedule.Formatter.YValueFormatter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CompleteListFragment extends Fragment {

    String month;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RateAdapter rateAdapter;
    TextView TopText,completeListBt;
    ImageButton rightBt, leftBt;
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
//    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    List<String> scheduleDTO = new ArrayList<>();
//    List<String> thatDates = new ArrayList<>();
    private int completeCount;
    private int entireCount;
    private Fragment fragment;

    public CompleteListFragment newInstance() {
        return new CompleteListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat transFormat = new SimpleDateFormat("MM월 yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        month = transFormat.format(currentCalendar.getTime());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complete_list_popup, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        TopText = rootView.findViewById(R.id.topMonthText);
        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt = rootView.findViewById(R.id.leftBt);
        completeListBt =rootView.findViewById(R.id.completeListBt);
        fragment = this;
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeCount = 0;
        entireCount = 0;

        TopText.setText(month);
        List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
        List<String> thatDates = new ArrayList<>();
        SharePref sharePref = new SharePref();
        scheduleDTOS.addAll(sharePref.getEntire(getActivity()));
        for (int i = 0; i < scheduleDTOS.size(); i++) {
            if (scheduleDTOS.get(i).getDate().substring(0, 4).equals(month.substring(4))
                    && scheduleDTOS.get(i).getDate().substring(5, 7).equals(month.substring(0, 2))) {
                for (int j = 0; j < scheduleDTOS.get(i).getIsComplete().size(); j++) {
                    if (scheduleDTOS.get(i).getIsComplete().get(j)) {
                        thatDates.add(scheduleDTOS.get(i).getDate().substring(5));
                    }
                    if (scheduleDTOS.get(i).getIsComplete().get(j)) {
                        completeCount++;
                        entireCount++;
                        scheduleDTO.add(scheduleDTOS.get(i).getSchedule().get(j));
                    } else {
                        entireCount++;
                    }
                }
            }
        }
        rateAdapter = new RateAdapter(getActivity(), thatDates, scheduleDTOS, month);
        recyclerView.setAdapter(rateAdapter);
        rateAdapter.notifyDataSetChanged();

//        ReadDBData(new Calandar.CalendarCallback() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onCallback(List<ScheduleDTO> value) {
//                scheduleDTOS.clear();
//                scheduleDTOS = value;
//                scheduleDTO.clear();
//                thatDates.clear();

//                for (int i = 0; i < scheduleDTOS.size(); i++) {
//                    if (scheduleDTOS.get(i).getDate().substring(0, 4).equals(month.substring(4))
//                            && scheduleDTOS.get(i).getDate().substring(5, 7).equals(month.substring(0, 2))) {
//                        for (int j = 0; j < scheduleDTOS.get(i).getIsComplete().size(); j++) {
//                            if (scheduleDTOS.get(i).getIsComplete().get(j)) {
//                                thatDates.add(scheduleDTOS.get(i).getDate().substring(5));
//                            }
//                            if (scheduleDTOS.get(i).getIsComplete().get(j)) {
//                                completeCount++;
//                                entireCount++;
//                                scheduleDTO.add(scheduleDTOS.get(i).getSchedule().get(j));
//                            } else {
//                                entireCount++;
//                            }
//                        }
//                    }
//                }
//                rateAdapter = new RateAdapter(getActivity(), thatDates, scheduleDTOS, month);
//                recyclerView.setAdapter(rateAdapter);
//                rateAdapter.notifyDataSetChanged();
//            }
//        });
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(month.substring(0, 2)) == 1) {
                    month = 12 + "월 " + (Integer.parseInt(month.substring(4)) - 1);
                } else {
                    String[] array = month.split("월");

                    if ((Integer.parseInt(array[0])) > 10) {
                        month = (Integer.parseInt(array[0]) - 1) + "월 " + Integer.parseInt(month.substring(4));
                    } else {
                        month = "0" + (Integer.parseInt(array[0]) - 1) + "월 " + Integer.parseInt(month.substring(4));
                    }
                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fragment).attach(fragment).commit();
            }
        });
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(month.substring(0, 2)) == 12)) {
                    month = "0" + 1 + "월 " + (Integer.parseInt(month.substring(4)) + 1);
                } else {
                    String[] array = month.split("월");
                    if (Integer.parseInt(array[0]) > 8) {
                        month = (Integer.parseInt(array[0]) + 1) + "월 " + Integer.parseInt(month.substring(4));

                    } else {
                        month = "0" + (Integer.parseInt(array[0]) + 1) + "월 " + Integer.parseInt(month.substring(4));

                    }

                }
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fragment).attach(fragment).commitAllowingStateLoss();
            }
        });

        AnimationSet downSet = new AnimationSet(true);
        downSet.setInterpolator(new CycleInterpolator(1));
        Animation down = new TranslateAnimation(0,0,0,20.0f);
        down.setDuration(700);
        downSet.addAnimation(down);
        downSet.setFillAfter(false);
        recyclerView.setAnimation(downSet);

        List<ExpandableListAdapter> data = new ArrayList<>();


        completeListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerView.getVisibility()==View.VISIBLE){
                    rateAdapter.closeSubView();
                    recyclerView.scrollToPosition(0);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    recyclerView.clearAnimation();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.startAnimation(downSet);
                }
            }
        });

    }
    public void ReadDBData(Calandar.CalendarCallback calendarCallback) {
        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        mDatabase.child(auth.getCurrentUser().getDisplayName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        scheduleDTOSTemp.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                            scheduleDTOSTemp.add(scheduleDTO);
//                            if (scheduleDTO.getDate() != null) {
//                            calendarCallback.onCallback(scheduleDTO.getDate());
//                            }
                        }
                        calendarCallback.onCallback(scheduleDTOSTemp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}

