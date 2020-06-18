package com.example.yourschedule.FRAGMENT;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.ADAPTER.SchduleRecyclerViewAdapter;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PopupFragment extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "dialog_event";
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    SchduleRecyclerViewAdapter adapter;
    RecyclerView schduleRecyclerView;
    OnMyDialogResult mDialogResult;
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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        schduleRecyclerView.setLayoutManager(linearLayoutManager);
        List<String> idx = new ArrayList<String>();
        idx.add("일정1");
        idx.add("일정2");
        idx.add("일정3");
        adapter = new SchduleRecyclerViewAdapter(idx,getActivity());
        schduleRecyclerView.setAdapter(adapter);



        previousBt = view.findViewById(R.id.previousButton);
        nextBt = view.findViewById(R.id.nextButton);
        storeBt = view.findViewById(R.id.storeBt);
        additionalBt = view.findViewById(R.id.additionalScheduleBt);


        final EditText[] scheduleText =
                {(EditText) view.findViewById(R.id.schedule1), (EditText) view.findViewById(R.id.schedule2), (EditText) view.findViewById(R.id.schedule3)};

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
//                ArrayList<String[]> schedule = new ArrayList<String[]>();
//                String sText;
//                for (int i = 0; i < scheduleText.length; i++) {
//                    sText = scheduleText[i].getText().toString().trim();
//                    Log.d("Text", sText);
//                    if (sText.length() > 0) {
//                        schedule.add(new String[]{sText, "false"});
//                    }
//                }
//                if (schedule.size() == 0) {
//                    Toast.makeText(getActivity(), "최소 하나의 일정은 입력하세요.", Toast.LENGTH_SHORT).show();
//                } else {
//                    Log.d("Text", "Success");
//                    pref.set(getActivity(), date, schedule);
//                    mDialogResult.finish("true");
//                }
                adapter.addSchedule(date);
                mDialogResult.finish("true");
            }
        });

        additionalBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
////        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////        LayoutInflater inflater = getActivity().getLayoutInflater();
////        View view = inflater.inflate(R.layout.fragment_popup, null);
////        final TextView dateView = view.findViewById(R.id.dateView);
////        final Button previousBt, nextBt, storeBt;
////
////        previousBt = view.findViewById(R.id.previousButton);
////        nextBt = view.findViewById(R.id.nextButton);
////        storeBt = view.findViewById(R.id.storeBt);
////
////
////
////        final EditText[] scheduleText =
////                {(EditText) view.findViewById(R.id.schedule1), (EditText) view.findViewById(R.id.schedule2), (EditText) view.findViewById(R.id.schedule3)};
////
////        previousBt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                try {
////                    Calendar calendar = Calendar.getInstance();
////                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
////                    Date transDate = transFormat.parse(date);
////                    calendar.setTime(transDate);
////                    calendar.add(Calendar.DATE, -1);
////                    String format = transFormat.format(calendar.getTime());
////                    date = format;
////                    dateView.setText(format + "");
////                    //asd
////                } catch (Exception e) {
////                }
////            }
////        });
////        dateView.setText(date);
////        nextBt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                try {
////                    Calendar calendar = Calendar.getInstance();
////                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
////                    Date transDate = transFormat.parse(date);
////                    calendar.setTime(transDate);
////                    calendar.add(Calendar.DATE, 1);
////                    String format = transFormat.format(calendar.getTime());
////                    date = format;
////                    dateView.setText(format + "");
////                } catch (Exception e) {
////                }
////            }
////        });
////
////
////        storeBt.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                ArrayList<String[]> schedule = new ArrayList<String[]>();
////                String sText;
////                for (int i = 0; i < scheduleText.length; i++) {
////                    sText = scheduleText[i].getText().toString().trim();
////                    Log.d("Text",sText);
////                    if (sText.length()>0) {
////                        schedule.add(new String[]{sText, "false"});
////                    }
////                }
////                if (schedule.size() == 0) {
////                    Toast.makeText(getActivity(), "최소 하나의 일정은 입력하세요.", Toast.LENGTH_SHORT).show();
////                } else {
////                    Log.d("Text","Success");
////                    pref.set(getActivity(), date, schedule);
////                    mDialogResult.finish("true");
////                }
////            }
////        });
////        builder.setView(view);
////        return builder.create();
//    }

    public void setDialogResult(OnMyDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult {
        void finish(String result);
    }
}
