package com.example.yourschedule;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AlarmService extends Service {

    ServiceThread thread;
    public AlarmService() {
    }

    @Override
    public void onCreate() {
        Log.d("service","onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("service","onStartCommand()");
        ServiceHandler handler = new ServiceHandler();
        thread = new ServiceThread(handler);
//        thread.stopForever();
        return START_STICKY;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.d("service","onDestroy()");
        ServiceHandler handler = new ServiceHandler();
        thread = new ServiceThread(handler);
        thread.start();
    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        Log.d("service","onTaskRemoved()");
//        SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd");
//        Calendar calendar = Calendar.getInstance();
//        String CompareDate = fm.format(calendar.getTime());
//        String year = CompareDate.substring(0,10).substring(0,4);
//        String month = CompareDate.substring(0,10).substring(5,7);
//        String day = CompareDate.substring(8,10);
//        Intent alarmServiceIntent = new Intent(getApplicationContext(),AlarmReceiver.class);
//        SharedPreferences sharedPreferences = getSharedPreferences("daily alarm", MODE_PRIVATE);
//        long millis = sharedPreferences.getLong(CompareDate, Calendar.getInstance().getTimeInMillis());
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), Integer.parseInt(year+month+day), alarmServiceIntent, 0);
//
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//
//        if (alarmManager != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(time,pendingIntent),pendingIntent);
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, millis, pendingIntent);
//            }else{
//                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
//            }
//        }
//        stopSelf();
////        super.onTaskRemoved(rootIntent);
//    }

//    private Handler mHandler = new Handler(){
//        public void handleMessage(Message msg){
//            Log.d("service","handleMessage()");
//            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
//            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
//                    notificationIntent, 0);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
//
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
//
//                String channelId = "default";
//                String channelName = "일정 알람 채널";
//                String description = "API 26버전 이상을 위한 것입니다.";
//
//                //NotificationManager.IMPORTANCE_HIGH :  소리와 알림메시지를 같이 보여줌
//                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//                channel.setDescription(description);
//
//                if (notificationManager != null) {
//                    // 노티피케이션 채널을 시스템에 등록
//                    notificationManager.createNotificationChannel(channel);
//                }
//            }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
//
//            builder.setAutoCancel(true)
//                    .setDefaults(NotificationCompat.DEFAULT_ALL)
//                    .setWhen(System.currentTimeMillis())
//                    .setTicker("{Time to watch some cool stuff!}")
//                    .setContentTitle("YourList")
//                    .setContentText("오늘 일정을 확인하세요!")
//                    .setContentInfo("INFO")
//                    .setContentIntent(pendingIntent);
//            if (notificationManager != null) {
//                // 노티피케이션 동작시킴
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    startForeground(1, builder.build());
//                }
//                    notificationManager.notify(1234, builder.build());
//            }
//        }
//    };
    public class ServiceHandler extends Handler {

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.d("service","handleMessage()");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                    notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남

                String channelId = "default";
                String channelName = "일정 알람 채널";
                String description = "API 26버전 이상을 위한 것입니다.";

                //NotificationManager.IMPORTANCE_HIGH :  소리와 알림메시지를 같이 보여줌
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription(description);

                if (notificationManager != null) {
                    // 노티피케이션 채널을 시스템에 등록
                    notificationManager.createNotificationChannel(channel);
                }
            }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

            builder.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setTicker("{Time to watch some cool stuff!}")
                    .setContentTitle("YourList")
                    .setContentText("오늘 일정을 확인하세요!")
                    .setContentInfo("INFO")
                    .setContentIntent(pendingIntent);
            if (notificationManager != null) {
                // 노티피케이션 동작시킴
                SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd");
                Calendar calendar = Calendar.getInstance();
                String CompareDate = fm.format(calendar.getTime());
                String year = CompareDate.substring(0,10).substring(0,4);
                String month = CompareDate.substring(0,10).substring(5,7);
                String day = CompareDate.substring(8,10);
                SharedPreferences sharedPreferences= getSharedPreferences("daily alarm", MODE_PRIVATE);
                long time = sharedPreferences.getLong(year+"."+month+"."+day, calendar.getTimeInMillis());
                if(calendar.getTimeInMillis()==time){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        startForeground(1, builder.build());
                    }
                    notificationManager.notify(1234, builder.build());
                    thread.stopForever();
                }
            }
        }
    }
}
