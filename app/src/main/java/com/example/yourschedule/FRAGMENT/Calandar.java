package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.DECORATOR.SaturDayDecorator;
import com.example.yourschedule.DECORATOR.ScheduleDecorator;
import com.example.yourschedule.DECORATOR.SunDayDecorator;
import com.example.yourschedule.DECORATOR.TodayDecorator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calandar extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    MaterialCalendarView materialCalendarView;
    ScheduleDecorator scheduleDecorator;
    boolean areYouUpdate = false;
    String selectedDate = "";
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    private Fragment fragment;
    private Fragment fffff;


    public Calandar newInstance() {
        return new Calandar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReadDBData(new CalendarCallback() {
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                Log.d("CallbackAccess","AccessCallback");
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
                scheduleDTOS.clear();
                scheduleDTOS = value;

                for(int i=0;i<value.size();i++){
                    if(value.get(i).getDate()!=null){
                        try{
                            scheduleDecorator = new ScheduleDecorator(transFormat.parse(value.get(i).getDate()));
                            materialCalendarView.addDecorators(scheduleDecorator, new SaturDayDecorator(), new SunDayDecorator());
                        }catch (ParseException e){}
                    }
                }

            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calandar, container, false);
        materialCalendarView = rootView.findViewById(R.id.calendar);
        fffff = this;
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setTopbarVisible(true);

        TodayDecorator todayDecorator = new TodayDecorator();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth = FirebaseAuth.getInstance();
        Log.d("TEST","ONCREATEVIEW");
        Log.d("aasdafsdf",scheduleDTOS.size()+"");


    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        selectedDate = format.format(date.getDate());
        boolean isGetUpdateFragment = false;
        for (int i = 0; i < scheduleDTOS.size(); i++) {
            if (scheduleDTOS.get(i).getDate().equals(selectedDate)) {
                areYouUpdate = true;
                getUpdateScheduleFragment();
                isGetUpdateFragment = true;
                break;
            } else {
                continue;
            }
        }
        if (!isGetUpdateFragment) {
            areYouUpdate = false;
            getPopupFragment(false);
        }
//        mDatabase.child(auth.getCurrentUser().getDisplayName())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    boolean isGetUpdateFragment = false;
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        Log.d("TEST","FIREBASE");
//                        scheduleDTOS.clear();
//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                            scheduleDTOS.add(scheduleDTO);
//                        }
//                        for (int i = 0; i < scheduleDTOS.size(); i++) {
//                            if (scheduleDTOS.get(i).getDate().equals(selectedDate)) {
//                                areYouUpdate = true;
//                                getUpdateScheduleFragment();
//                                isGetUpdateFragment = true;
//                                break;
//                            } else {
//                                continue;
//                            }
//                        }
//                        if (!isGetUpdateFragment) {
//                            areYouUpdate = false;
//                            getPopupFragment(false);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
    }

    private void getUpdateScheduleFragment() {
        UpdateScheduleFragment updateScheduleFragment = new UpdateScheduleFragment().newInstance();
        updateScheduleFragment.show(getActivity().getSupportFragmentManager(), updateScheduleFragment.TAG_EVENT_DIALOG);
        updateScheduleFragment.setDialogResult(new UpdateScheduleFragment.onMyUpdateDialogResult() {
            @Override
            public void finish() {
                fragment = getActivity().getSupportFragmentManager().findFragmentByTag(UpdateScheduleFragment.TAG_EVENT_DIALOG);
                DialogFragment dialogFragment = (DialogFragment) fragment;
                dialogFragment.dismissAllowingStateLoss();
                dialogFragment.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                calendarUpdate();









            }

            @Override
            public void showPopupFragment() {
                getPopupFragment(true);
            }
        });
    }

    private void getPopupFragment(boolean isUpdate) {
        if (isUpdate) {
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
//                    calendarUpdate();
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    ft.detach(fffff).attach(fffff).commit();
                    dialogFragment.dismiss();

                }

                @Override
                public boolean update() {
                    if (areYouUpdate) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        } else {
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
//                    calendarUpdate();
                    FragmentTransaction ft =getFragmentManager().beginTransaction();
                    ft.detach(fffff).attach(fffff).commit();
                    dialogFragment.dismiss();


                }

                @Override
                public boolean update() {
                    if (areYouUpdate) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
//        calendarUpdate();
    }

    public void ReadDBData(CalendarCallback calendarCallback) {
        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        mDatabase.child(auth.getCurrentUser().getDisplayName())
                .addValueEventListener(new ValueEventListener() {
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

    public interface CalendarCallback {
        void onCallback(List<ScheduleDTO> value);
    }

    public interface OnReturn{
        void onReturnData(List<ScheduleDTO> value);
    }

    public void returnData(List<ScheduleDTO> values,OnReturn onReturn){
        onReturn.onReturnData(values);
    }
}
