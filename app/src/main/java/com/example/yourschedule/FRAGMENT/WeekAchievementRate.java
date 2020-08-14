package com.example.yourschedule.FRAGMENT;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.yourschedule.Formatter.WeekValueFormatter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.Formatter.YValueFormatter;
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

public class WeekAchievementRate extends Fragment {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RateAdapter rateAdapter;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    List<String> scheduleDTO = new ArrayList<>();
    List<String> thatDates = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    TextView TopText, completeListBt, notCompleteListBt;
    ImageButton rightBt, leftBt;
    private HorizontalBarChart barChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat transFormat = new SimpleDateFormat("MM월 yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        month = transFormat.format(currentCalendar.getTime());

    }

    private Fragment fragment;

    String month;

    public WeekAchievementRate newInstance() {
        return new WeekAchievementRate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View rootView = inflater.inflate(R.layout.fragment_week_achievement_rate, container, false);

//        recyclerView = rootView.findViewById(R.id.recyclerView);
//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        recyclerView.addItemDecoration(
//                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
//        recyclerView.setLayoutManager(linearLayoutManager);

        TopText = rootView.findViewById(R.id.topMonthText);
        completeListBt = rootView.findViewById(R.id.completeListBt);
        notCompleteListBt = rootView.findViewById(R.id.notCompleteListText);
        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt = rootView.findViewById(R.id.leftBt);
        fragment = this;

        barChart = rootView.findViewById(R.id.horizontalBar);
        barChart.setDrawBarShadow(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        barChart.setClickable(true);
        barChart.setDragEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawValueAboveBar(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);

        XAxis xl = barChart.getXAxis();
        xl.setDrawAxisLine(false);
        xl.setDrawLabels(true);
        xl.setEnabled(true);
        xl.setTextColor(ContextCompat.getColor(barChart.getContext(), R.color.white));


        YAxis yl = barChart.getAxisLeft();
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(100f);
        yl.setEnabled(false);
        yl.setDrawLabels(false);


        YAxis yr = barChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setEnabled(false);
        yr.setDrawLabels(false);

        barChart.getLegend().setFormSize(0);
        barChart.setFitBars(true);
        barChart.animateY(1000);

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

                //그냥 2개로 나눔
                ArrayList<Integer> weekStart = new ArrayList<Integer>();
                ArrayList<Integer> weekEnd = new ArrayList<Integer>();
                String[] array = month.split("월 ");
                weekStart.clear();
                weekEnd.clear();

                cal.set(Calendar.YEAR, Integer.parseInt(array[1]));
                cal.set(Calendar.MONTH, Integer.parseInt(array[0]) - 1);

                int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

                int howMuchWeek = cal.getActualMaximum(Calendar.WEEK_OF_MONTH);
                for (int week = 1; week <= howMuchWeek; week++) {
                    cal.set(Calendar.WEEK_OF_MONTH, week);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    int startDay = cal.get(Calendar.DAY_OF_MONTH);
                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    int endDay = cal.get(Calendar.DAY_OF_MONTH);
                    if (week == 1 && startDay >= 25) {
                        startDay = 1;

                    }
                    //for문을 돌다가 week가 해당 월의 마지막 주이고 마지막 날짜가 7보다 작을경우
                    if (week == howMuchWeek && endDay < 7) {
                        //해당 월의 마지막 날짜를 endDay에 대입
                        endDay = lastDayOfMonth;
                    }
                    weekStart.add(startDay);
                    weekEnd.add(endDay);
                    ArrayList<String> labels = new ArrayList<>();
                    for (int i = weekStart.size(); i > 0; i--) {
                        labels.add(i + "");
                    }
                    barChart.getXAxis().setValueFormatter(new WeekValueFormatter(labels));
                    barChart.getXAxis().setTextSize(20);
                    barChart.getXAxis().setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf"));
                    barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    barChart.getXAxis().setGranularity(1f);
                    barChart.getXAxis().setCenterAxisLabels(false);
                }
                for (int i = 0; i < weekStart.size(); i++) {
                }

                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<Integer> entireCountList = new ArrayList<>();
                ArrayList<Integer> completeCountList = new ArrayList<>();
                for (int i = 0; i < weekStart.size(); i++) {
                    entireCountList.add(i, 0);
                    completeCountList.add(i, 0);
                }
                for (int i = 0; i < weekStart.size(); i++) {

                    for (int j = 0; j < scheduleDTOS.size(); j++) {
                        if (scheduleDTOS.get(j).getDate().substring(0, 4).equals(month.substring(4))
                                && scheduleDTOS.get(j).getDate().substring(5, 7).equals(month.substring(0, 2))) {
                            if (weekStart.get(i) <= Integer.parseInt(scheduleDTOS.get(j).getDay())
                                    && weekEnd.get(i) >= Integer.parseInt(scheduleDTOS.get(j).getDay())) {
                                switch (i) {
                                    case 0:
                                        if (entireCountList.get(0) != 0) {
                                            entireCountList.set(0, entireCountList.get(0) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(0, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(0, completeCountList.get(0) + 1);
                                            }
                                        }
                                        break;
                                    case 1:
                                        if (entireCountList.get(1) != 0) {
                                            entireCountList.set(1, entireCountList.get(1) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(1, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(1, completeCountList.get(1) + 1);
                                            }
                                        }
                                        break;
                                    case 2:
                                        if (entireCountList.get(2) != 0) {
                                            entireCountList.set(2, entireCountList.get(2) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(2, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(2, completeCountList.get(2) + 1);
                                            }
                                        }
                                        break;
                                    case 3:
                                        if (entireCountList.get(3) != 0) {
                                            entireCountList.set(3, entireCountList.get(3) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(3, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(3, completeCountList.get(3) + 1);
                                            }
                                        }
                                        break;
                                    case 4:
                                        if (entireCountList.get(4) != 0) {
                                            entireCountList.set(4, entireCountList.get(4) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(4, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(4, completeCountList.get(4) + 1);
                                            }
                                        }
                                        break;
                                    case 5:
                                        if (entireCountList.get(5) != 0) {
                                            entireCountList.set(5, entireCountList.get(5) + scheduleDTOS.get(j).getIsComplete().size());
                                        } else {
                                            entireCountList.set(5, scheduleDTOS.get(j).getIsComplete().size());
                                        }
                                        for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                            if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                                completeCountList.set(5, completeCountList.get(5) + 1);
                                            }
                                        }
                                    default:
                                        break;
                                }
                                continue;
                            }
                        }
                    }
                }


                float barWidth = 9f;
                float spaceForBar = 10f;
                for (int i = 0; i < weekStart.size(); i++) {
                    if (completeCountList.get(i) != 0) {
                        entries.add(new BarEntry((weekStart.size() - 1 - i) * spaceForBar, (float) ((double) completeCountList.get(i) / (double) entireCountList.get(i) * 100)));
                    } else {
                        entries.add(new BarEntry((weekStart.size() - 1 - i) * spaceForBar, 0f));
                    }

                }


                BarDataSet barDataSet;
                barDataSet = new BarDataSet(entries, "");

                barDataSet.setValueTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf"));
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(barDataSet);
                BarData barData = new BarData(dataSets);
                barData.setValueTextSize(20f);
                barData.setBarWidth(barWidth);
                barData.setDrawValues(true);
                barData.setValueFormatter(new YValueFormatter());
                barData.setValueTextColor(ContextCompat.getColor(barChart.getContext(), R.color.stringMainColor));
                barChart.setData(barData);
                barDataSet.setColors(ContextCompat.getColor(barChart.getContext(), R.color.white));
            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("MM월 ");
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
                ft.detach(fragment).attach(fragment).commit();
            }
        });

        completeListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        notCompleteListBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
