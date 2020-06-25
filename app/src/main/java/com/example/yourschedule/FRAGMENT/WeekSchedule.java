package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
import com.example.yourschedule.DECORATOR.SaturDayDecorator;
import com.example.yourschedule.DECORATOR.ScheduleDecorator;
import com.example.yourschedule.DECORATOR.SunDayDecorator;
import com.example.yourschedule.DECORATOR.TodayDecorator;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class WeekSchedule extends Fragment {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    SharePref pref = new SharePref();

    public WeekSchedule newInstance() {
        return new WeekSchedule();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekschedule, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}