package com.example.yourschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class DeviceBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd");
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                context.startForegroundService(alarmIntent);
            }else{
                context.startService(alarmIntent);
            }
            Calendar calendar = Calendar.getInstance();
            String CompareDate = fm.format(calendar.getTime());
            String year = CompareDate.substring(0,10).substring(0,4);
            String month = CompareDate.substring(0,10).substring(5,7);
            String day = CompareDate.substring(8,10);

            SharedPreferences sharedPreferences = context.getSharedPreferences("daily alarm", MODE_PRIVATE);
                long millis = sharedPreferences.getLong(CompareDate, Calendar.getInstance().getTimeInMillis());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(year+month+day), alarmIntent, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(millis,pendingIntent),pendingIntent);
            }
        }
    }

}
