package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.DatePicker;
import android.widget.TextView;

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
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calandar extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    MaterialCalendarView materialCalendarView;
    ScheduleDecorator scheduleDecorator;
    SlidingUpPanelLayout slidingUpPanelLayout;
    boolean areYouUpdate = false;
    String selectedDate = "";
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    private Fragment fragment;
    private Fragment fffff;
    private TextView test;


    public Calandar newInstance() {
        return new Calandar();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_calandar, container, false);
        slidingUpPanelLayout = rootView.findViewById(R.id.slidingView);
        materialCalendarView = rootView.findViewById(R.id.calendar);
        test = rootView.findViewById(R.id.test);
        fffff = this;

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setTopbarVisible(true);



        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

        TodayDecorator todayDecorator = new TodayDecorator();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d("state","슬라이드");
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.d("state",newState+"");
                if((slidingUpPanelLayout.getPanelState()==SlidingUpPanelLayout.PanelState.EXPANDED
                        || slidingUpPanelLayout.getPanelState()==SlidingUpPanelLayout.PanelState.ANCHORED)
                && selectedDate.equals("2020.07.17")){
                    Log.d("state","???");
                    test.setText("OK");

                   materialCalendarView.setVisibility(View.INVISIBLE);
                }else{
                    Log.d("state","!!!");
                    materialCalendarView.setVisibility(View.VISIBLE);
                }
            }
        });


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
        ReadDBData(new CalendarCallback() {
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                Log.d("CallbackAccess","AccessCallback");
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
                scheduleDTOS.clear();
                scheduleDTOS = value;
                for(int i=0;i<scheduleDTOS.size();i++){
                    if(scheduleDTOS.get(i).getDate()!=null){
                        try{
                            Log.d("CallbackAccess","working....");
                            scheduleDecorator = new ScheduleDecorator(transFormat.parse(scheduleDTOS.get(i).getDate()));
                            materialCalendarView.addDecorators(scheduleDecorator);
                        }catch (ParseException e){}
                    }
                }
            }
        });




    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        selectedDate = format.format(date.getDate());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int slideablePanelHeight = size.y/3;
        int animationDuration = 300;

//        SlidingUpPanelResizeAnimation animation = new SlidingUpPanelResizeAnimation(slidingUpPanelLayout, slideablePanelHeight, animationDuration);
//        slidingUpPanelLayout.startAnimation(animation);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
        if(slidingUpPanelLayout.getPanelState()==SlidingUpPanelLayout.PanelState.EXPANDED || slidingUpPanelLayout.getPanelState()==SlidingUpPanelLayout.PanelState.ANCHORED){
            Log.d("state","???");
            test.setText("OK");
        }else{
            Log.d("state","!!!");
        }


//        boolean isGetUpdateFragment = false;
//        for (int i = 0; i < scheduleDTOS.size(); i++) {
//            if (scheduleDTOS.get(i).getDate().equals(selectedDate)) {
//                areYouUpdate = true;
//                getUpdateScheduleFragment();
//                isGetUpdateFragment = true;
//                break;
//            } else {
//                continue;
//            }
//        }
//        if (!isGetUpdateFragment) {
//            areYouUpdate = false;
//            getPopupFragment(false);
//        }

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

 class SlidingUpPanelResizeAnimation extends Animation {

    private SlidingUpPanelLayout mLayout;

    private float mTo;
    private float mFrom = 0;

    public SlidingUpPanelResizeAnimation(SlidingUpPanelLayout layout, float to, int duration) {
        mLayout = layout;
        mTo = to;
        setDuration(duration);
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float dimension = (mTo - mFrom) * interpolatedTime + mFrom;
        mLayout.setPanelHeight((int) dimension);
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);

//        mLayout.requestLayout();
    }

}