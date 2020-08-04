package com.example.yourschedule.FRAGMENT;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.RateAdapter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

public class WeekAchievementRate extends Fragment implements SeekBar.OnSeekBarChangeListener {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RateAdapter rateAdapter;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    List<String> scheduleDTO = new ArrayList<>();
    List<String> thatDates = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    TextView TopText,entireCountText,completeCountText;
    ImageButton rightBt, leftBt;
    private HorizontalBarChart chart2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat transFormat = new SimpleDateFormat("MM월 yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        month = transFormat.format(currentCalendar.getTime());

    }

    private Fragment fffff;

    String month;

    public WeekAchievementRate newInstance() {
        return new WeekAchievementRate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View rootView = inflater.inflate(R.layout.fragment_week_achievement_rate, container, false);

//        recyclerView = rootView.findViewById(R.id.recyclerView);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
//        recyclerView.setLayoutManager(linearLayoutManager);

        TopText = rootView.findViewById(R.id.topMonthText);

        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt = rootView.findViewById(R.id.leftBt);
        fffff = this;


        //////chart2
        chart2 = rootView.findViewById(R.id.chart2);
        chart2.setDrawBarShadow(true);
        chart2.setDrawValueAboveBar(true);
        chart2.getDescription().setEnabled(true);
        chart2.setPinchZoom(false);
        chart2.setDrawGridBackground(true);
        chart2.getXAxis().setDrawGridLines(false);
        chart2.getAxisLeft().setDrawGridLines(false);
        chart2.getAxisRight().setDrawGridLines(false);

        XAxis xl2 = chart2.getXAxis();
        xl2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl2.setDrawAxisLine(false);
        xl2.setDrawGridLines(false);
        xl2.setDrawLabels(false);
        xl2.setEnabled(false);

        YAxis yl2 = chart2.getAxisLeft();
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
        yl2.setAxisMinimum(0f);
        yl2.setEnabled(false);
        yl2.setAxisMaximum(100f);
        yl2.setLabelCount(5);


        YAxis yr2 = chart2.getAxisRight();
        yr2.setDrawAxisLine(true);
        yr2.setDrawGridLines(false);
        yr2.setEnabled(false);
        chart2.getLegend().setFormSize(0);
        chart2.setFitBars(false);
        chart2.animateY(1000);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //이번달
        TopText.setText(month);
        ReadDBData(new Calandar.CalendarCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                scheduleDTOS.clear();
                scheduleDTOS = value;
                scheduleDTO.clear();
                thatDates.clear();

                Calendar cal = Calendar.getInstance();
                int intYear = Integer.parseInt(String.valueOf(cal.get(Calendar.YEAR)));
                int intMonth = Integer.parseInt(String.valueOf(cal.get(Calendar.MONTH)));
                Log.d("intMonth", intMonth + "");

                //그냥 2개로 나눔
                ArrayList<Integer> weekStart = new ArrayList<Integer>();
                ArrayList<Integer> weekEnd = new ArrayList<Integer>();
                String[] array = month.split("월 ");
                Log.d("sdfgsdgfd", array[1]);
                weekStart.clear();
                weekEnd.clear();

                cal.set(Calendar.YEAR, Integer.parseInt(array[1]));
                cal.set(Calendar.MONTH, Integer.parseInt(array[0]) - 1);

                int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                Log.d("lastDay", lastDayOfMonth + "");

                int howMuchWeek = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
                Log.d("howMuchWeek", howMuchWeek + "");
                Log.d("Sfdgsdgfsdfgsdfgsd", cal.getActualMaximum(Calendar.WEEK_OF_MONTH) + "");
                for (int week = 1; week <= howMuchWeek; week++) {
                    cal.set(Calendar.WEEK_OF_MONTH, week);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    Log.d("time", cal.getTime() + "");
                    int startDay = cal.get(Calendar.DAY_OF_MONTH);
                    Log.d("startDay", startDay + "");
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    int endDay = cal.get(Calendar.DAY_OF_MONTH);
                    Log.d("endDay", endDay + "");
                    if (week == 1 && startDay >= 25) {
                        startDay = 1;
                        Log.d("startDay2", startDay + "");

                    }
                    Log.d("week", week + "");
                    Log.d("sadfasg", cal.getActualMaximum(Calendar.DAY_OF_MONTH) + "");
                    Log.d("rdsglkfjfskd", cal.getActualMaximum(Calendar.WEEK_OF_MONTH) + "");
                    //for문을 돌다가 week가 해당 월의 마지막 주이고 마지막 날짜가 7보다 작을경우
                    if (week == howMuchWeek && endDay < 7) {
                        Log.d("adsfasdfsdfasfsdf", "진입");
                        //해당 월의 마지막 날짜를 endDay에 대입
                        endDay = lastDayOfMonth;
                        Log.d("endDay2", endDay + "");
                    }
                    //week 시작, 끝 추가
                    weekStart.add(startDay);
                    weekEnd.add(endDay);

                }
                for (int i = 0; i < weekStart.size(); i++) {
                    Log.d("weekStart", weekStart.get(i) + "");
                    Log.d("weekEnd", weekEnd.get(i) + "");
                }
                int entireCount = 0;
                int completeCount = 0;
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<Integer> entireCountList = new ArrayList<>();
                ArrayList<Integer> completeCountList = new ArrayList<>();

                for (int i = 0; i < weekStart.size(); i++) {
                    entireCountList.add(i,0);
                    completeCountList.add(i,0);
                    for (int j = 0; j < scheduleDTOS.size(); j++) {
                        if (scheduleDTOS.get(j).getDate().substring(0, 4).equals(month.substring(4))
                                && scheduleDTOS.get(j).getDate().substring(5, 7).equals(month.substring(0, 2))) {
                            if (weekStart.get(i) <= Integer.parseInt(scheduleDTOS.get(j).getDay())
                                    && weekEnd.get(i) >= Integer.parseInt(scheduleDTOS.get(j).getDay())) {
                                switch (i) {
                                    case 0:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 1 + "번 째 주입니다.");
                                        if(entireCountList.get(0)!=0){
                                            entireCountList.set(0,entireCountList.get(0)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(0,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(0,completeCountList.get(0)+1);
                                            }
                                        }
                                        break;
                                    case 1:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 2 + "번 째 주입니다.");
                                        if(entireCountList.get(1)!=0){
                                            entireCountList.set(1,entireCountList.get(1)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(1,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(1,completeCountList.get(1)+1);
                                            }
                                        }
                                        break;
                                    case 2:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 3 + "번 째 주입니다.");
                                        if(entireCountList.get(2)!=0){
                                            entireCountList.set(2,entireCountList.get(2)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(2,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(2,completeCountList.get(2)+1);
                                            }
                                        }
                                        break;
                                    case 3:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 4 + "번 째 주입니다.");
                                        if(entireCountList.get(3)!=0){
                                            entireCountList.set(3,entireCountList.get(3)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(3,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(3,completeCountList.get(3)+1);
                                            }
                                        }
                                        break;
                                    case 4:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 5 + "번 째 주입니다.");
                                        if(entireCountList.get(4)!=0){
                                            entireCountList.set(4,entireCountList.get(4)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(4,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(4,completeCountList.get(4)+1);
                                            }
                                        }
                                        break;
                                    case 5:
                                        Log.d("whatWeek", scheduleDTOS.get(j).getDate() + "는" + 6 + "번 째 주입니다.");
                                        if(entireCountList.get(5)!=0){
                                            entireCountList.set(5,entireCountList.get(5)+scheduleDTOS.get(j).getIsComplete().size());
                                        }else{
                                            entireCountList.set(5,scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for(int k=0;k<scheduleDTOS.get(j).getIsComplete().size();k++){
                                            if(scheduleDTOS.get(j).getIsComplete().get(k)){
                                                completeCountList.set(5,completeCountList.get(5)+1);
                                            }
                                        }
                                    default:
                                        break;
                                }
                                continue;
                            }
                        }

                        if ((float) ((double) completeCount / (double) entireCount * 100) > 0) {
                            entries.add(new BarEntry(i * 2f, (float) ((double) completeCount / (double) entireCount * 100)));

                        } else {
                            entries.add(new BarEntry(i * 2f, (float) 0));
                        }
                        Log.d("stat", i + 1 + "주 - " + entries.get(i));
                        Log.d("completeCount", completeCount + "");
                        Log.d("entireCount", entireCount + "");
                        entireCount = 0;
                        completeCount = 0;
                    }

                    chart2.getDescription().setText("");
//                    chart2.getDescription().setText(((int) ((double) Choice / (double) Entire * 100) + "%"));

                    Log.d("stat_그래프 몇개?", entries.size() + "");
                    BarDataSet barDataSet = new BarDataSet(entries, "week_test");
                    barDataSet.setBarBorderWidth(0.9f);
                    barDataSet.setColors(R.color.white);
                    BarData data1 = new BarData(barDataSet);
                    chart2.setData(data1);
//                    chart2.invalidate();
//                    if (chart2.getData() != null &&
//                            chart2.getData().getDataSetCount() > 0) {
////                  set1 = new BarDataSet(values, "witdraw per day");
//                        barDataSet.setValues(entries);
                    chart2.getData().notifyDataChanged();
                    chart2.notifyDataSetChanged();
//                    } else {
//                        barDataSet = new BarDataSet(entries, "");
//                    }

//                ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
//                dataSets1.add(barDataSet);
//                barDataSet.setValueTextColor(R.color.colorPrimary);
//                data1.setValueTextSize(20f);
//                data1.setBarWidth(0.5f);
////            data.setValueTypeface(tfLight);
//                data1.setBarWidth(barWidth);
//                barDataSet.setColors(ContextCompat.getColor(chart2.getContext(), R.color.white));
//                }catch (Exception e){
//
//                }

                }
                Log.d("fdsggdfgsfg",entireCountList.get(1)+"");
                Log.d("fdsggdfgsfg2",entireCountList.get(2)+"");
                Log.d("dfghfdghhdfghdfh1",completeCountList.get(0)+"");
                Log.d("dfghfdghhdfghdfh2",completeCountList.get(1)+"");
            }
        });

        ///////


        SimpleDateFormat sdf = new SimpleDateFormat("MM월 ");
        leftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(month.substring(0, 2)) == 1) {
                    month = 12 + "월 " + (Integer.parseInt(month.substring(4)) - 1);
                } else {
                    String[] array = month.split("월");
                    Log.d("sdfgsdgfd", array[0]);
                    if ((Integer.parseInt(array[0])) > 10) {
                        month = (Integer.parseInt(array[0]) - 1) + "월 " + Integer.parseInt(month.substring(4));
                    } else {
                        month = "0" + (Integer.parseInt(array[0]) - 1) + "월 " + Integer.parseInt(month.substring(4));
                    }
                }
                Log.d("alterMonth", month);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fffff).attach(fffff).commit();
            }
        });
        rightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("cccMonth",(Integer.parseInt(month.substring(0, 2)))+"");
                if ((Integer.parseInt(month.substring(0, 2)) == 12)) {
                    month = "0"+1 + "월 " + (Integer.parseInt(month.substring(4)) + 1);
                } else {
                    String[] array = month.split("월");
                    if (Integer.parseInt(array[0]) > 8) {
                        month = (Integer.parseInt(array[0]) + 1) + "월 " + Integer.parseInt(month.substring(4));

                    } else {
                        month = "0" + (Integer.parseInt(array[0]) + 1) + "월 " + Integer.parseInt(month.substring(4));

                    }

                }
                Log.d("alterMonth", month);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(fffff).attach(fffff).commit();
            }
        });
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        tvX.setText(String.valueOf(seekBarX.getProgress()));
//        tvY.setText(String.valueOf(seekBarY.getProgress()));

//        setData(seekBarX.getProgress(), seekBarY.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface CalendarCallback {
        void onCallback(List<ScheduleDTO> value);
    }

    public interface OnReturn {
        void onReturnData(List<ScheduleDTO> value);
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
