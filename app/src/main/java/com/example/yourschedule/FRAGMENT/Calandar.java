package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.example.yourschedule.DECORATOR.SaturDayDecorator;
import com.example.yourschedule.DECORATOR.ScheduleDecorator;
import com.example.yourschedule.DECORATOR.SunDayDecorator;
import com.example.yourschedule.DECORATOR.TodayDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calandar extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    ArrayList<String> DecoratorList = new ArrayList<String>();
    MaterialCalendarView materialCalendarView;
    ScheduleDecorator scheduleDecorator;
    SharePref pref = new SharePref();
    boolean areYouUpdate = false;

    String selectedDate="";
    private Fragment fragment;

    public Calandar newInstance() {
        return new Calandar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calandar, container, false);
        materialCalendarView = rootView.findViewById(R.id.calendar);

        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setTopbarVisible(true);

        TodayDecorator todayDecorator = new TodayDecorator();


        materialCalendarView.state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setMinimumDate(CalendarDay.from(1900, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        materialCalendarView.setDynamicHeightEnabled(true);
        materialCalendarView.setDateTextAppearance(R.style.TextAppearance_MaterialCalendarWidget_Date);

        materialCalendarView.addDecorators(
                new SaturDayDecorator(), new SunDayDecorator(), todayDecorator);

        return rootView;
    }
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        selectedDate = format.format(date.getDate());
        if(pref.get(getActivity(),selectedDate).size()!=0){
            areYouUpdate = true;
            getUpdateScheduleFragment();
        }else{
            areYouUpdate = false;
            getPopupFragment(false);
        }
    }
    private void getUpdateScheduleFragment() {
        UpdateScheduleFragment updateScheduleFragment = new UpdateScheduleFragment().newInstance();
        updateScheduleFragment.show(getActivity().getSupportFragmentManager(),updateScheduleFragment.TAG_EVENT_DIALOG);
        updateScheduleFragment.setDialogResult(new UpdateScheduleFragment.onMyUpdateDialogResult() {
            @Override
            public void finish() {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(UpdateScheduleFragment.TAG_EVENT_DIALOG);
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismissAllowingStateLoss();
                    dialogFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    calendarUpdate();
                    dialogFragment.dismiss();
            }

            @Override
            public void showPopupFragment() {
                getPopupFragment(true);
            }
        });
    }

    private void getPopupFragment(boolean isUpdate) {
        if(isUpdate){
            PopupFragment popupFragment = new PopupFragment().newInstance();
            Bundle args = new Bundle();
            args.putString("date", selectedDate);
            popupFragment.setArguments(args);
            popupFragment.show(getActivity().getSupportFragmentManager(), popupFragment.TAG_EVENT_DIALOG);
            popupFragment.setDialogResult(new PopupFragment.OnMyPopupDialogResult() {
                @Override
                public void finish() {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(PopupFragment.TAG_EVENT_DIALOG);
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismissAllowingStateLoss();
                    dialogFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    calendarUpdate();
                    dialogFragment.dismiss();
                }
                @Override
                public boolean update() {
                    if(areYouUpdate){
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        }else{
            PopupFragment popupFragment = new PopupFragment().newInstance();
            Bundle args = new Bundle();
            args.putString("date", selectedDate);
            popupFragment.setArguments(args);
            popupFragment.show(getActivity().getSupportFragmentManager(), popupFragment.TAG_EVENT_DIALOG);
            popupFragment.setDialogResult(new PopupFragment.OnMyPopupDialogResult() {
                @Override
                public void finish() {
                    fragment = getActivity().getSupportFragmentManager().findFragmentByTag(PopupFragment.TAG_EVENT_DIALOG);
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismissAllowingStateLoss();
                    dialogFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    calendarUpdate();
                    dialogFragment.dismiss();
                }

                @Override
                public boolean update() {
                    if(areYouUpdate){
                        return true;
                    }else{
                        return false;
                    }
                }
            });
        }

    }
    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        calendarUpdate();
    }

    public void calendarUpdate(){

        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");

        String selectedDate;

        Calendar calendar = Calendar.getInstance();

        String year = yearFormat.format(calendar.getTime());
        String month = monthFormat.format(calendar.getTime());


        ArrayList<String> storedList;

        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, i);
            selectedDate = transFormat.format((calendar.getTime()));
//            storedList = getStringArrayPref(selectedDate); //key값에 따른 데이터 유무 확인해서 있으면 value 가져오는거
            storedList = pref.get(this.getActivity(), selectedDate);

            if (storedList.size()!=0) {
                scheduleDecorator = new ScheduleDecorator((calendar.getTime()));
                materialCalendarView.addDecorators(scheduleDecorator, new SaturDayDecorator(), new SunDayDecorator());
            }
        }

    }
}
