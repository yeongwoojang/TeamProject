package com.example.yourschedule;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class AlarmService extends Service {
    private static PowerManager.WakeLock sCpuWakeLock;
    private static WifiManager.WifiLock sWifiLock;
    private static ConnectivityManager manager;
    private static final String WAKELOCK_TAG = "testous:wakelock";
    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
////        Log.d("start","AlarmReceiver");
//
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingI = PendingIntent.getActivity(this, 0,
//                notificationIntent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//
//            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
//
//
//            String channelName = "매일 알람 채널";
//            String description = "매일 정해진 시간에 알람합니다.";
//            int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌
//
//            NotificationChannel channel = new NotificationChannel("default", channelName, importance);
//            channel.setDescription(description);
//
//            if (notificationManager != null) {
//                // 노티피케이션 채널을 시스템에 등록
//                notificationManager.createNotificationChannel(channel);
//            }
//        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
//
//
//        builder.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setTicker("{Time to watch some cool stuff!}")
//                .setContentTitle("YourList")
//                .setContentText("오늘 일정을 확인하세요!")
//                .setContentInfo("INFO")
//                .setContentIntent(pendingI);
//        if (notificationManager != null) {
//            // 노티피케이션 동작시킴
//            notificationManager.notify(1234, builder.build());
//        }
        mHandler.sendEmptyMessage(0);

        return START_REDELIVER_INTENT ;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
//        Log.d("start","AlarmReceiver");


            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingI = PendingIntent.getActivity(getApplicationContext(), 0,
                    notificationIntent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남


                String channelName = "매일 알람 채널";
                String description = "매일 정해진 시간에 알람합니다.";
                int importance = NotificationManager.IMPORTANCE_HIGH; //소리와 알림메시지를 같이 보여줌

                NotificationChannel channel = new NotificationChannel("default", channelName, importance);
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
                    .setContentIntent(pendingI);
            if (notificationManager != null) {
                // 노티피케이션 동작시킴
                notificationManager.notify(1234, builder.build());
            }
        }
    };
}
