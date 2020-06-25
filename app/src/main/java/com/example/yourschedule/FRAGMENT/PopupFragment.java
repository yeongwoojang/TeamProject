package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.ADAPTER.SchduleRecyclerViewAdapter;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class PopupFragment extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "dialog_event";
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    SchduleRecyclerViewAdapter adapter;
    RecyclerView schduleRecyclerView;
    OnMyPopupDialogResult mDialogResult;
    LinearLayoutManager linearLayoutManager;
    SharePref pref = new SharePref();
    String date;


    public static PopupFragment newInstance() {
        PopupFragment popupFragment = new PopupFragment();
        return popupFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        if (mArgs != null) {
            date = getArguments().getString("date");
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popup, container, false);
        final TextView dateView = view.findViewById(R.id.dateView);
        final Button previousBt, nextBt, storeBt, additionalBt;


        schduleRecyclerView = view.findViewById(R.id.scheduleList);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        schduleRecyclerView.setLayoutManager(linearLayoutManager);

        previousBt = view.findViewById(R.id.previousButton);
        nextBt = view.findViewById(R.id.nextButton);
        storeBt = view.findViewById(R.id.storeBt);
        additionalBt = view.findViewById(R.id.additionalScheduleBt);

//        adapter = new SchduleRecyclerViewAdapter(getActivity(),date);
//        schduleRecyclerView.setAdapter(adapter);
        if(mDialogResult.update()){
            adapter = new SchduleRecyclerViewAdapter(getActivity(),date,true);
        }else{
            adapter = new SchduleRecyclerViewAdapter(getActivity(),date,false);
        }
        schduleRecyclerView.setAdapter(adapter);

        previousBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
                    Date transDate = transFormat.parse(date);
                    calendar.setTime(transDate);
                    calendar.add(Calendar.DATE, -1);
                    String format = transFormat.format(calendar.getTime());
                    date = format;
                    dateView.setText(format + "");
                    //asd
                } catch (Exception e) {
                }
            }
        });
        dateView.setText(date);
        nextBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
                    Date transDate = transFormat.parse(date);
                    calendar.setTime(transDate);
                    calendar.add(Calendar.DATE, 1);
                    String format = transFormat.format(calendar.getTime());
                    date = format;
                    dateView.setText(format + "");
                } catch (Exception e) {
                }
            }
        });


        storeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addSchedule(date);
                mDialogResult.finish();
            }
        });

        additionalBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.addEditText();
            }
        });

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    public interface OnMyPopupDialogResult {
        void finish();
        boolean update();
    }
    public void setDialogResult(OnMyPopupDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

}
