package com.example.yourschedule.FRAGMENT;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.ADAPTER.SchduleRecyclerViewAdapter;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
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
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    SchduleRecyclerViewAdapter adapter;
    Button previousBt, nextBt, storeBt, additionalBt;
    TextView dateView;
    RecyclerView schduleRecyclerView;
    OnMyPopupDialogResult mDialogResult;
    LinearLayoutManager linearLayoutManager;
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    String date;


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
        previousBt = view.findViewById(R.id.previousButton);
        nextBt = view.findViewById(R.id.nextButton);
        storeBt = view.findViewById(R.id.storeBt);
        additionalBt = view.findViewById(R.id.additionalScheduleBt);


        schduleRecyclerView = view.findViewById(R.id.scheduleList);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        schduleRecyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        schduleRecyclerView.setLayoutManager(linearLayoutManager);

        ReadDBData(new ReadDataCallback() {
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                Log.d("CallBack?","onNo...");
                scheduleDTOS.clear();
                scheduleDTOS = value;
                Log.d("scheduleDTOSSize",scheduleDTOS.size()+"");
                if(mDialogResult.update()){
                    adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,true);
                }else{
                    adapter = new SchduleRecyclerViewAdapter(getActivity(),scheduleDTOS,date,false);
                }
                adapter.notifyDataSetChanged();
                schduleRecyclerView.setAdapter(adapter);
            }
        });

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
                adapter.addSchedule();
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
        Log.d("Seqeunce",1+"");
        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();

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

}
