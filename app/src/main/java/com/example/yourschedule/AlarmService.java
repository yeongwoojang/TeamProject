//package com.example.yourschedule;
//
//import android.app.AlarmManager;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//
//import java.util.Calendar;
//
//public class AlarmService extends Service {
//    public AlarmService() {
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
//        PackageManager pm = getPackageManager();
//        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
//        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//        Calendar calendar = (Calendar) intent.getSerializableExtra("calendar");
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        // 사용자가 매일 알람을 허용했다면
//
//            if (alarmManager != null) {
////                alarmManager.setAlarmClock(AlarmManager.RTC_WAKEUP, (long)calendar.getTimeInMillis(), pendingIntent);
//                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pendingIntent),pendingIntent);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),pendingIntent),pendingIntent);
//                }
//            }
////             부팅 후 실행되는 리시버 사용가능하게 설정
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP);
//        return START_NOT_STICKY;
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
