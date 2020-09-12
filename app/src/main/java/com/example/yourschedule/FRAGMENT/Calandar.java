package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.DrawerListAdapter;
import com.example.yourschedule.DECORATOR.DaysTextDecorator;
import com.example.yourschedule.DECORATOR.TextSizeDecorator;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.DECORATOR.SaturDayDecorator;
import com.example.yourschedule.DECORATOR.ScheduleDecorator;
import com.example.yourschedule.DECORATOR.SunDayDecorator;
import com.example.yourschedule.DECORATOR.TodayDecorator;
import com.example.yourschedule.SharePref;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Calandar extends Fragment implements OnDateSelectedListener, OnMonthChangedListener {

    MaterialCalendarView materialCalendarView;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DrawerListAdapter drawerListAdapter;
    ScheduleDecorator scheduleDecorator;
    DaysTextDecorator daysTextDecorator;
    SlidingUpPanelLayout slidingUpPanelLayout;
    boolean areYouUpdate = false;
    String selectedDate = "";
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;

    private Fragment fragment;
    private Fragment fffff;
    private TextView detailDate;
    private Button modifyBt, deleteScheduleBt;
    private ImageView monthImage;
    AnimationDrawable animationDrawable;

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
        recyclerView = rootView.findViewById(R.id.drawerListView);
        modifyBt = rootView.findViewById(R.id.modifyBt);
        monthImage = rootView.findViewById(R.id.month_image);
        detailDate = rootView.findViewById(R.id.detailDate);
        fffff = this;

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setTopbarVisible(true);



        slidingUpPanelLayout.setPanelHeight(0);
        GradientDrawable drawable =

                (GradientDrawable) getActivity().getDrawable(R.drawable.round_image_form);

        monthImage.setBackground(drawable);
        monthImage.setClipToOutline(true);


        TodayDecorator todayDecorator = new TodayDecorator(getActivity());
        TextSizeDecorator textSizeDecorator = new TextSizeDecorator(getActivity());
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if ((slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || slidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
                    materialCalendarView.setVisibility(View.INVISIBLE);
                } else {
                    materialCalendarView.setVisibility(View.VISIBLE);
                }
            }
        });
        modifyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isGetUpdateFragment = false;
                for (int i = 0; i < scheduleDTOS.size(); i++) {
                    if (scheduleDTOS.get(i).getDate().equals(selectedDate)) {
                        Log.d("selectedDate", "from modifyBt");
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
                slidingUpPanelLayout.setPanelHeight(0);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
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
                new SaturDayDecorator(getActivity()), new SunDayDecorator(getActivity()), todayDecorator, textSizeDecorator);

        SharePref sharePref = new SharePref();
        scheduleDTOS = sharePref.getEntire(getActivity());
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        for (int i = 0; i < scheduleDTOS.size(); i++) {
            if (scheduleDTOS.get(i).getDate() != null) {
                try {
                    scheduleDecorator = new ScheduleDecorator(transFormat.parse(scheduleDTOS.get(i).getDate()), getActivity());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                materialCalendarView.addDecorators(scheduleDecorator);
            }
        }


    }


    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        selectedDate = format.format(date.getDate());


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        detailDate.setText(selectedDate);

        recyclerView.setVisibility(View.INVISIBLE);

        for (int i = 0; i < scheduleDTOS.size(); i++) {
            if (scheduleDTOS.get(i).getDate().equals(selectedDate)) {
                modifyBt.setText("MODIFY");
                drawerListAdapter = new DrawerListAdapter(getActivity(), scheduleDTOS.get(i).getSchedule(), scheduleDTOS.get(i).getIsComplete());
                recyclerView.setAdapter(drawerListAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                break;
            } else {
                modifyBt.setText("INSERT");
            }
        }

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
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
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
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
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
        Log.d("month", date.getMonth() + "");
        switch (date.getMonth() + 1) {
            case 1:
                monthImage.setImageResource(R.drawable.bkg_01_january);
                break;
            case 2:
                monthImage.setImageResource(R.drawable.bkg_02_february);
                break;
            case 3:
                monthImage.setImageResource(R.drawable.bkg_03_march);
                break;
            case 4:
                monthImage.setImageResource(R.drawable.bkg_04_april);
                break;
            case 5:
                monthImage.setImageResource(R.drawable.bkg_05_may);
                break;
            case 6:
                monthImage.setImageResource(R.drawable.bkg_06_june);
                break;
            case 7:
                monthImage.setImageResource(R.drawable.bkg_07_july);
                break;
            case 8:
                monthImage.setImageResource(R.drawable.bkg_08_august);
                break;
            case 9:
                monthImage.setImageResource(R.drawable.bkg_09_september);
                break;
            case 10:
                monthImage.setImageResource(R.drawable.bkg_10_october);
                break;
            case 11:
                monthImage.setImageResource(R.drawable.bkg_11_november);
                break;
            case 12:
                monthImage.setImageResource(R.drawable.bkg_12_december);
                break;
        }

    }



    void animate() {
//        animationDrawable.start();

//        if(animationDrawable.isRunning()) {
//            animationDrawable.stop();
//        }
//        monthImage.setVisibility(View.VISIBLE);
//        animationDrawable.start();
//        animationDrawable.setOneShot(true);

    }
}



