package com.example.yourschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlamHATT {
    private Context context;

    public AlamHATT(Context context) {
        this.context = context;
    }

    public void alam(Calendar calendar){
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context,Broadcast.class);

        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

        //알람시간 calendar에 set해주기

        Log.d("time",calendar.getTime()+"");
        //알람 예약
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);


    }
}
