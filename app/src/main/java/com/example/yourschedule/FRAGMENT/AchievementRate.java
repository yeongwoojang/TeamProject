package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AchievementRate extends Fragment {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
//    RecyclerView recyclerView;
//    LinearLayoutManager linearLayoutManager;
//    ScheduleOfWeekAdapter scheduleOfWeekAdapter;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    TextView TopText;
    ImageButton rightBt,leftBt;
    LineChart chart;
    String month;
    public AchievementRate newInstance() {
        return new AchievementRate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_achievement_rate, container, false);
        TopText = rootView.findViewById(R.id.topMonthText);
        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt  = rootView.findViewById(R.id.leftBt);
        chart = rootView.findViewById(R.id.chart);
//        recyclerView = rootView.findViewById(R.id.recyclerView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
//        recyclerView.setLayoutManager(linearLayoutManager);
        DecimalFormat df = new DecimalFormat("0");
        Calendar currentCalendar = Calendar.getInstance();
        //이번달
        month  = df.format(currentCalendar.get(Calendar.MONTH) + 1);
        TopText.setText(month+"월");
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(month)==1){
                    month = 12+"";
                    TopText.setText(month+"월");
                }else{
                    month = ((Integer.parseInt(month))-1)+"";
                    TopText.setText(month+"월");
                }

            }
        });
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(month)==12){
                    month = 1+"";
                    TopText.setText(month+"월");
                }else{
                    month = ((Integer.parseInt(month))+1)+"";
                    TopText.setText(month+"월");
                }

            }
        });
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
//        try {
//            mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                                scheduleDTOS.add(scheduleDTO);
//                                scheduleOfWeekAdapter.notifyDataSetChanged();
//                            }
//                        }
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//        } catch (Exception e) {
//
//        }
//        scheduleOfWeekAdapter = new ScheduleOfWeekAdapter(getActivity(),scheduleDTOS);
//        recyclerView.setAdapter(scheduleOfWeekAdapter);
    }






}
