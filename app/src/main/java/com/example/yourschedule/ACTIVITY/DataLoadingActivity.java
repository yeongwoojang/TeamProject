package com.example.yourschedule.ACTIVITY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataLoadingActivity extends AppCompatActivity {


    private ImageView runningImage;

    private final int REQ_EXIT = 111;
    private Context mContext = null;
    private List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    private FirebaseAuth auth;
    private DatabaseReference mReference;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loading);

        mContext = this;

        runningImage = (ImageView)findViewById(R.id.running_image);

        SharePref sharePref = new SharePref();
//        sharePref.deletaAll(getApplicationContext());

        final AnimationDrawable drawable =
                (AnimationDrawable) runningImage.getBackground();
        drawable.start();

        readDatabase(new DataLoadCallBack() {
            @Override
            public void onCallback(List<ScheduleDTO> value) {
                scheduleDTOS.clear();
                if (value.size() != 0) {
                    scheduleDTOS = value;
                    sharePref.FireBaseToSharedPref(mContext, scheduleDTOS);
                    Intent goToMainIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivityForResult(goToMainIntent,REQ_EXIT);
                }else{
                    Intent goToMainIntent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivityForResult(goToMainIntent,REQ_EXIT);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_EXIT){
            if(resultCode==RESULT_OK){
                this.finish();
            }
        }
    }

    public void readDatabase(DataLoadCallBack dataLoadCallBack) {
        List<ScheduleDTO> scheduleDTOSTemp = new ArrayList<>();
        mReference = mDatabase.getReference("일정");
        auth = FirebaseAuth.getInstance();
        if(auth!=null){
            mReference.child(auth.getCurrentUser().getDisplayName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            scheduleDTOSTemp.clear();


                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                                scheduleDTOSTemp.add(scheduleDTO);
                            }

                            dataLoadCallBack.onCallback(scheduleDTOSTemp);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
//        Log.d("readUserName",auth.getCurrentUser().getDisplayName());

    }
    public interface DataLoadCallBack {
        void onCallback(List<ScheduleDTO> value);
    }
}