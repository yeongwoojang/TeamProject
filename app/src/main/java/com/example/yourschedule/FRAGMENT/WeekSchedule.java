package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeekSchedule extends Fragment {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    SharePref pref = new SharePref();

    public WeekSchedule newInstance() {
        return new WeekSchedule();
    }

    @Override
    public View onCreateView( LayoutInflater inflater,  ViewGroup container,
                              Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weekschedule, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Schdule> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String selectedDate = transFormat.format(calendar.getTime());

        ///////////////// get Current Week of the year
        calendar=Calendar.getInstance();
        Log.v("Current Week", String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)));
        int current_week=calendar.get(Calendar.WEEK_OF_YEAR);
        int week_start_day=calendar.getFirstDayOfWeek(); // this will get the starting day os week in integer format i-e 1 if monday
        Toast.makeText(getContext(),"Current Week is"+current_week +"Start Day is"+week_start_day,Toast.LENGTH_SHORT).show();

        // get the starting and ending date
        // Set the calendar to sunday of the current week
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.println("Current week = " + Calendar.DAY_OF_WEEK);

        // Print dates of the current week starting on Sunday
        DateFormat df = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
        String startDate = "", endDate = "";

        startDate = df.format(calendar.getTime());
////////////////////////////////
        ArrayList<String> storedList;
        selectedDate = startDate;
        for(int i=0;i<7;i++){
            storedList = pref.get(this.getActivity(), selectedDate);
            if (storedList != null) {
                for (int j = 0; j < storedList.size(); j++) {
                    Log.d("GetData",storedList.get(j));
                    list.add(new Schdule(storedList.get(j)));
                }
            }
            calendar.add(Calendar.DATE, 1);
            selectedDate = df.format(calendar.getTime());

        }


    }
}