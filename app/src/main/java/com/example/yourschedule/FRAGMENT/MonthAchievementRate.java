package com.example.yourschedule.FRAGMENT;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.RateAdapter;
import com.example.yourschedule.DECORATOR.CustomTypeFaceSpan;
import com.example.yourschedule.Formatter.YValueFormatter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
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

public class MonthAchievementRate extends Fragment {
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    List<String> scheduleDTO = new ArrayList<>();
    List<String> thatDates = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    TextView TopText,completeListBt;
    ImageButton rightBt, leftBt,menuBt;
    private HorizontalBarChart barChart;
    private int completeCount;
    private int entireCount;
    private Fragment fragment;

    String month;

    public MonthAchievementRate newInstance() {
        return new MonthAchievementRate();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SimpleDateFormat transFormat = new SimpleDateFormat("MM월 yyyy");
        Calendar currentCalendar = Calendar.getInstance();
        month = transFormat.format(currentCalendar.getTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month_achievement_rate, container, false);

        TopText = rootView.findViewById(R.id.topMonthText);
        completeListBt = rootView.findViewById(R.id.completeListBt);
        rightBt = rootView.findViewById(R.id.rightBt);
        leftBt = rootView.findViewById(R.id.leftBt);
        menuBt = rootView.findViewById(R.id.menuBt);
        fragment = this;


        barChart = rootView.findViewById(R.id.horizontalBar);
        barChart.setDrawBarShadow(true);
        barChart.getDescription().setEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.setTouchEnabled(false);
        barChart.setDrawValueAboveBar(true);

        XAxis xl = barChart.getXAxis();
        xl.setDrawGridLines(false);
        xl.setDrawAxisLine(false);
        xl.setDrawLabels(true);
        xl.setEnabled(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextColor(ContextCompat.getColor(barChart.getContext(), R.color.black));
        xl.setAxisMinimum(0f);
        xl.setAxisMaximum(10f);
        xl.setLabelCount(5,true);
        xl.setCenterAxisLabels(false);

        xl.setTextSize(20);
        xl.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf"));
        xl.setValueFormatter(new IndexAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value) {
                if(value==5f){
                    return "달성률";
                }else{
                    return "";
                }
            }
        });
        YAxis yl = barChart.getAxisLeft();
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        yl.setAxisMaximum(100f);
        yl.setEnabled(false);

        YAxis yr = barChart.getAxisRight();
        yr.setDrawAxisLine(false);
        yr.setDrawGridLines(false);
        yr.setEnabled(false);

        barChart.getLegend().setFormSize(0);
        barChart.setFitBars(false);
        barChart.animateY(1000);

        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        completeCount = 0;
        entireCount = 0;

        TopText.setText(month);

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

                float barWidth = 9f;
                float spaceForBar = 10f;
                ArrayList<BarEntry> entries = new ArrayList<>();
                if(completeCount!=0){
                    entries.add(new BarEntry(5f, (float) ((double) completeCount / (double) entireCount * 100)));
                }else{
                    entries.add(new BarEntry(5f,0f));
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
                barDataSet.setColors(ContextCompat.getColor(barChart.getContext(), R.color.deepPurple200));

            }
        });

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



        menuBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getActivity(),view, Gravity.TOP);
                getActivity().getMenuInflater().inflate(R.menu.rate_menu,menu.getMenu());

                for (int i = 0; i < menu.getMenu().size(); i++) {
                    MenuItem menuItem = menu.getMenu().getItem(i);
                    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf");
                    SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
                    mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    menuItem.setTitle(mNewTitle);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        switch (menuItem.getItemId()){
                            case R.id.monthRate :
                                fragment = new MonthAchievementRate().newInstance();
                                fragmentTransaction.replace(R.id.testFragment, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                            case R.id.weekRate :
                                fragment = new WeekAchievementRate().newInstance();
                                    fragmentTransaction.replace(R.id.testFragment, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commitAllowingStateLoss();
                                break;
                            case R.id.completeList :
                                fragment = new CompleteListFragment().newInstance();
                                fragmentTransaction.replace(R.id.testFragment, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                        }

                        return false;
                    }
                });
                menu.show();

            }
        });


    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.rate_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                        }
                        calendarCallback.onCallback(scheduleDTOSTemp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }
}
