package com.example.yourschedule.ADAPTER;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import androidx.annotation.NonNull;

import com.example.yourschedule.FRAGMENT.Calandar;
import com.example.yourschedule.ListWidget;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    public Context context = null;
    private int appWidgetId;
    String today;
    private int itemIndex;


    public WidgetRemoteViewsFactory(Context context, Intent intent,ArrayList<ScheduleDTO> dataSet) {
        Log.d("execute", "inFactory");

        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        scheduleDTOS.addAll((ArrayList<ScheduleDTO>)intent.getSerializableExtra("dataSet"));
        Log.d("dateSet",scheduleDTOS.size()+", "+dataSet.size()+"");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        today = transFormat.format(calendar.getTime());

    }

    @Override
    public void onCreate() {
        Log.d("execute", "onCreateFactory");
//            initializeData();
        Log.d("execute", "AfterDataLoad");

    }

    @Override
    public void onDataSetChanged() {
        Log.d("execute", "onDataSetChanged()");
//        initializeData();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
//        Log.d("factory","getCount()");
        return 5;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("execute", "getViewAt" + position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        Log.d("asd", getCount() + "");
//        try {
//            scheduleDTOS.clear();
//            auth = FirebaseAuth.getInstance();
//            mDatabase.child(auth.getCurrentUser().getDisplayName())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                                if (scheduleDTO.getDate().equals(today)) {
//                                    if (position > scheduleDTO.getSchedule().size() - 1) {
//                                        remoteViews.setTextViewText(R.id.item, "");
//                                    } else {
//                                        remoteViews.setTextViewText(R.id.item, scheduleDTO.getSchedule().get(position));
//                                    }
//                                }
////                                scheduleDTOS.add(scheduleDTO);
//                            }
//                            Log.d("execute", "dataLoadOK");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//        } catch (NullPointerException e) {
//        }
//        if (getCount() > 0) {
//            remoteViews.setTextViewText(R.id.item, scheduleDTOS.get(itemIndex).getSchedule().get(position));
//        } else {
//            remoteViews.setTextViewText(R.id.item, "오늘은 계획이 없습니다.");
//        }
//        if(position>scheduleDTOS.get(0).getSchedule().size()-1){
//            remoteViews.setTextViewText(R.id.item,  "");
//        }else{
//            remoteViews.setTextViewText(R.id.item,scheduleDTOS.get(0).getSchedule().get(position));
//        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private void initializeData() throws NullPointerException {

//        try {
//            scheduleDTOS.clear();
//            auth = FirebaseAuth.getInstance();
//            mDatabase.child(auth.getCurrentUser().getDisplayName())
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
//                                scheduleDTOS.add(scheduleDTO);
//                            }
//                            Log.d("execute", "dataLoadOK");
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                        }
//                    });
//        }catch (NullPointerException e){}
    }
}
