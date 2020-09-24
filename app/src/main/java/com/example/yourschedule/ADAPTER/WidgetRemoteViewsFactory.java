package com.example.yourschedule.ADAPTER;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.yourschedule.AlarmManagerBroadcastReceiver;
import com.example.yourschedule.ListWidgetProvider;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {


    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    public Context context = null;
    private List<String> scheduleList = new ArrayList<>();
    private List<Boolean> chkList = new ArrayList<>();
    private int appWidgetId;
    String today;
    private int itemIndex;
    private List<String> testList = new ArrayList<>();
    private List<Boolean> testChk = new ArrayList<>();

    public WidgetRemoteViewsFactory(Context context, Intent intent) {

        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d("execute", "onCreate!");

        initializeData();

    }

    @Override
    public void onDataSetChanged() {
        Log.d("execute", "onDataSetChanged!");
        initializeData();

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d("execute", "getViewAt()");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.item_widget);
        remoteViews.setTextViewText(R.id.item, scheduleList.get(position));


        if (chkList.get(position) != false) {
            remoteViews.setImageViewResource(R.id.chkBt, R.drawable.baseline_check_circle_white_18);
        } else {
            remoteViews.setImageViewResource(R.id.chkBt, R.drawable.baseline_panorama_fish_eye_white_18);
        }

        Bundle extras = new Bundle();
        extras.putInt(ListWidgetProvider.EXTRA_ITEM, position);
        extras.putSerializable("chkList", (Serializable) chkList);
        extras.putSerializable("scheduleList", (Serializable) scheduleList);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.chkBt, fillIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    private void initializeData() {
        Log.d("initial", "update");
        SharePref sharePref = new SharePref();
        scheduleDTOS.clear();
        scheduleList.clear();
        testList.clear();
        chkList.clear();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        Calendar calendar = Calendar.getInstance();
        String today = sdf.format(calendar.getTime());
        Log.d("today", today);
        scheduleDTOS = sharePref.getEntire(context);


        for (int i = 0; i < scheduleDTOS.size(); i++) {
                if(scheduleDTOS.get(i).getDate().equals(today)){
                    scheduleList.addAll(scheduleDTOS.get(i).getSchedule());
                    chkList.addAll(scheduleDTOS.get(i).getIsComplete());
                    break;
                }
        }


        List<String> test = new ArrayList<>();
        List<Boolean> testchk = new ArrayList<>();
        test.add(calendar.getTimeInMillis() + "");
        testchk.add(false);
        for (int i = 0; i < scheduleDTOS.size(); i++) {
            if (scheduleDTOS.get(i).getDate().equals(today)) {
                testList.addAll(test);
                testChk.addAll(testchk);
                break;
            }
        }
        Log.d("list", scheduleList + "");
        Log.d("chk", chkList + "");
    }
    void dayUpdate(Context context) {
        Calendar midnight = Calendar.getInstance();
//        Date now = new Date();
//        midnight.setTime(now);
        midnight.setTimeInMillis(System.currentTimeMillis());
//        midnight.add(Calendar.DAY_OF_MONTH, 0);
//        midnight.set(Calendar.HOUR, 16);
        midnight.set(Calendar.HOUR_OF_DAY, 16);
        midnight.set(Calendar.MINUTE, 38);
        midnight.set(Calendar.SECOND, 0);
//        midnight.set(Calendar.AM_PM, Calendar.PM);


        Intent alarmIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) midnight.getTimeInMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
            }
        }
    }
}
