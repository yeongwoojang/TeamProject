package com.example.yourschedule.FRAGMENT;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.ADAPTER.SchduleRecyclerViewAdapter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class PopupFragment extends DialogFragment {

    public static final String TAG_EVENT_DIALOG = "dialog_event";
    SchduleRecyclerViewAdapter adapter;
    ForDrawerListener forDrawerListener;
    ImageButton storeBt,closeBt;
    TextView dateView;
    RecyclerView schduleRecyclerView;
    EditText inputSchedule;
    OnMyPopupDialogResult mDialogResult;
    LinearLayoutManager linearLayoutManager;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    String date;
    boolean signal = false;



    public static PopupFragment newInstance() {
        PopupFragment popupFragment = new PopupFragment();
        return popupFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle mArgs = getArguments();
        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        if (mArgs != null) {
            date = getArguments().getString("date");
            Log.d("date",date);
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_popup, container, false);
        dateView = view.findViewById(R.id.dateView);
        storeBt = view.findViewById(R.id.storeBt);
        closeBt = view.findViewById(R.id.closeBt);
        inputSchedule = view.findViewById(R.id.textEd);

        schduleRecyclerView = view.findViewById(R.id.scheduleList);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        schduleRecyclerView.setLayoutManager(linearLayoutManager);

        SharePref sharePref = new SharePref();
        scheduleDTOS.clear();
        scheduleDTOS.addAll(sharePref.getEntire(getActivity()));

        if(mDialogResult.update()){
            adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,true);
        }else{
            adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,false);
        }
        adapter.notifyDataSetChanged();
        schduleRecyclerView.setAdapter(adapter);

//        ReadDBData(new ReadDataCallback() {
//            @Override
//            public void onCallback(List<ScheduleDTO> value) {
//                scheduleDTOS.clear();
//                scheduleDTOS = value;
//                if(mDialogResult.update()){
//                    adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,true);
//                }else{
//                    adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,false);
//                }
//                adapter.notifyDataSetChanged();
//                schduleRecyclerView.setAdapter(adapter);
//            }
//        });

        dateView.setText(date);

        storeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newData = inputSchedule.getText().toString();
                adapter.addSchedule(newData);
                inputSchedule.setText("");

//                signal = true;
//                mDialogResult.finish();

            }
        });
        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.close();
                signal = true;
                mDialogResult.finish();
            }
        });


        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

    }

    public interface OnMyPopupDialogResult {
        void finish();
        boolean update();
    }
    public void setDialogResult(OnMyPopupDialogResult dialogResult) {
        mDialogResult = dialogResult;
    }


    public interface ReadDataCallback {
        void onCallback(List<ScheduleDTO> value);
    }

    public void ReadDBData(ReadDataCallback readDataCallback){
        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();
        Log.d("popup",date);
        mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        scheduleDTOSTemp.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                            scheduleDTOSTemp.add(scheduleDTO);
                            Log.d("scheduleDTOSize",scheduleDTOSTemp+"");
                        }
                        readDataCallback.onCallback(scheduleDTOSTemp);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    interface ForDrawerListener{
        void getSignal();
    }
}
