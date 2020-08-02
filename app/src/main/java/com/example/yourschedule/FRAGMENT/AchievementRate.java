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
import com.github.mikephil.charting.utils.ColorTemplate;
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

public class AchievementRate extends Fragment implements SeekBar.OnSeekBarChangeListener {
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
    private HorizontalBarChart chart;
    private HorizontalBarChart chart2;
    private SeekBar seekBarX, seekBarY;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat transFormat = new SimpleDateFormat("MM월 yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        month = transFormat.format(currentCalendar.getTime());

    }

    //    private TextView tvX, tvY;
    private int completeCount;
    private int entireCount;
    private Fragment fffff;

    String month;

    public AchievementRate newInstance() {
        return new AchievementRate();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View rootView = inflater.inflate(R.layout.fragment_achievement_rate, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        TopText = rootView.findViewById(R.id.topMonthText);
        entireCountText = rootView.findViewById(R.id.entireCountText);
        completeCountText = rootView.findViewById(R.id.completCountText);

        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt = rootView.findViewById(R.id.leftBt);
        chart = rootView.findViewById(R.id.chart1);
        fffff = this;

        seekBarX = rootView.findViewById(R.id.seekBar1);
        seekBarY = rootView.findViewById(R.id.seekBar2);

        seekBarY.setOnSeekBarChangeListener(this);
        seekBarX.setOnSeekBarChangeListener(this);

        chart = rootView.findViewById(R.id.chart1);
        chart.setDrawBarShadow(true);
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(true);
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setDrawLabels(false);
        xl.setEnabled(false);

        YAxis yl = chart.getAxisLeft();
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);
        yl.setEnabled(false);
        yl.setAxisMaximum(100f);
        yl.setLabelCount(5);


        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);
        chart.getLegend().setFormSize(0);
        chart.setFitBars(false);
        chart.animateY(1000);
        // setting data
        seekBarY.setProgress(1);
        seekBarX.setProgress(100);

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

//        Legend l = chart.getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setFormSize(10f);
//        l.setForm(Legend.LegendForm.CIRCLE);
//        l.setTextColor(ContextCompat.getColor(chart.getContext(), R.color.white));
//        l.setXEntrySpace(10f);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeCount = 0;
        entireCount = 0;
        //이번달
        Log.d("month", month + "");
        TopText.setText(month);
        Log.d("asdasdsadas", Integer.parseInt(month.substring(0, 2)) + "");
        ReadDBData(new Calandar.CalendarCallback() {
            @SuppressLint("ResourceType")
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                scheduleDTOS.clear();
                scheduleDTOS = value;
                scheduleDTO.clear();
                thatDates.clear();

                for (int i = 0; i < scheduleDTOS.size(); i++) {
                    if (scheduleDTOS.get(i).getDate().substring(0, 4).equals(month.substring(4))
                            && scheduleDTOS.get(i).getDate().substring(5, 7).equals(month.substring(0, 2))) {
                        for (int j = 0; j < scheduleDTOS.get(i).getIsComplete().size(); j++) {
                            if(scheduleDTOS.get(i).getIsComplete().get(j)){
                                thatDates.add(scheduleDTOS.get(i).getDate().substring(5));
                            }
                            if (scheduleDTOS.get(i).getIsComplete().get(j)) {
                                completeCount++;
                                entireCount++;
                                Log.d("aaaaaaaaaaa",scheduleDTOS.get(i).getSchedule().get(j));
                                scheduleDTO.add(scheduleDTOS.get(i).getSchedule().get(j));
                            } else {
                                entireCount++;
                            }
                        }
                    }
                }








                entireCountText.setText("전체 목표 수 :"+ entireCount);
                completeCountText.setText("완료한 목표 수 : "+completeCount);
                rateAdapter = new RateAdapter(getActivity(), thatDates,scheduleDTO,month);
                recyclerView.setAdapter(rateAdapter);

                Log.d("completeCount", completeCount + "");
                Log.d("entireCount", entireCount + "");
                Log.d("percent", ((double) completeCount / (double) entireCount * 100) + "");
                float barWidth = 100;
                float spaceForBar = 10f;
                ArrayList<BarEntry> values = new ArrayList<>();
                for (int i = 0; i < seekBarX.getProgress(); i++) {
                    float val = (float) (Math.random() * seekBarY.getProgress());
                    values.add(new BarEntry(2, (float) ((double) completeCount / (double) entireCount * 100)
                    ));
                }
                chart.getDescription().setText(((int) ((double) completeCount / (double) entireCount * 100) + "%"));
                chart.getDescription().setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf"));
                chart.getDescription().setTextSize(20);
                chart.getDescription().setTextColor(R.color.colorPrimary);
                chart.getDescription().setTextAlign(Paint.Align.RIGHT);

                BarDataSet set1;

                if (chart.getData() != null &&
                        chart.getData().getDataSetCount() > 0) {
                    set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
//                  set1 = new BarDataSet(values, "witdraw per day");
                    set1.setValues(values);
                    chart.getData().notifyDataChanged();
                    chart.notifyDataSetChanged();
                } else {
                    set1 = new BarDataSet(values, "");
//                    set1.setDrawIcons(true);

                    ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    set1.setValueTextColor(R.color.colorPrimary);
                    BarData data = new BarData(dataSets);
                    data.setValueTextSize(20f);
                    data.setBarWidth(0.5f);
//            data.setValueTypeface(tfLight);
                    data.setBarWidth(barWidth);
                    chart.setData(data);
                    set1.setColors(ContextCompat.getColor(chart.getContext(), R.color.white));
                }

                ///////
                /////조질까봐 따로 만듬/////
                Calendar cal = Calendar.getInstance();
                int intYear=Integer.parseInt(String.valueOf(cal.get(Calendar.YEAR)));
                int intMonth=Integer.parseInt(String.valueOf(cal.get(Calendar.MONTH)));

                //그냥 2개로 나눔
                ArrayList<Integer> weekstart = new ArrayList<Integer>();
                ArrayList<Integer> weekend = new ArrayList<Integer>();

                weekstart.clear();
                weekend.clear();

                cal.set(Calendar.YEAR, intYear);
                cal.set(Calendar.MONTH, intMonth - 1);

                for (int week = 1; week < cal.getMaximum(Calendar.WEEK_OF_MONTH); week++) {
                    cal.set(Calendar.WEEK_OF_MONTH, week);

                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    int startDay = cal.get(Calendar.DAY_OF_MONTH);

                    cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                    int endDay = cal.get(Calendar.DAY_OF_MONTH);

                    if (week == 1 && startDay >= 7) {
                        startDay = 1;
                    }

                    if (week == cal.getMaximum(Calendar.WEEK_OF_MONTH) - 1 && endDay <= 7) {
                        endDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
                    }

                    System.out.println(week + "주 : " + startDay + " ~ " + endDay);
                    weekstart.add(startDay);
                    weekend.add(endDay);
                }

                int Entire = 0;
                int Choice = 0;
                ArrayList<BarEntry> entries = new ArrayList<>();

                try {
                    Log.d("stat_month", Calendar.MONTH + "");
                    Log.d("stat", weekstart.size() + "");
                    Log.d("stat_scheduleDTOS SIZE", scheduleDTOS.size() + "");
                    for (int i = 0; i < weekstart.size(); i++) {
                        for (int j = 0; j < scheduleDTOS.size(); j++) {
                            if (weekstart.get(i) < Integer.parseInt(scheduleDTOS.get(j).getDay())
                                    && weekend.get(i) > Integer.parseInt(scheduleDTOS.get(j).getDay())) {
                                Entire++;
                                for (int k = 0; k < scheduleDTOS.get(j).getIsComplete().size(); k++) {
                                    if (scheduleDTOS.get(j).getIsComplete().get(k)) {
                                        Choice++;
                                    }
                                }

                            }
                        }
                        entries.add(new BarEntry(i * 2f, (float) ((double) Choice / (double) Entire * 100)));
                        Log.d("stat", i + 1 + "주 - " + (float) ((double) Choice / (double) Entire * 100));
                        Entire = 0;
                        Choice = 0;
                    }



                BarDataSet barDataSet = new BarDataSet(entries, "week_test");
                    if (chart2.getData() != null &&
                            chart2.getData().getDataSetCount() > 0) {
//                  set1 = new BarDataSet(values, "witdraw per day");
                        barDataSet.setValues(entries);
                        chart2.getData().notifyDataChanged();
                        chart2.notifyDataSetChanged();
                    } else {
//                        barDataSet = new BarDataSet(entries, "");
//                        barDataSet.setBarBorderWidth(1f);
//                        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    }

                ArrayList<IBarDataSet> dataSets1 = new ArrayList<>();
                dataSets1.add(barDataSet);
                barDataSet.setValueTextColor(R.color.colorPrimary);
                BarData data1 = new BarData(dataSets1);
                data1.setValueTextSize(20f);
                data1.setBarWidth(0.5f);
//            data.setValueTypeface(tfLight);
                data1.setBarWidth(barWidth);
                chart2.setData(data1);
                barDataSet.setColors(ContextCompat.getColor(chart2.getContext(), R.color.white));
                }catch (Exception e){

                }

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
        chart.setFitBars(false);
        chart.invalidate();
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
