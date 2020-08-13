package com.example.yourschedule;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AlarmReceiver extends BroadcastReceiver {

//    private static PowerManager.WakeLock sCpuWakeLock;
//    private static WifiManager.WifiLock sWifiLock;
//    private static ConnectivityManager manager;
//    private static final String WAKELOCK_TAG = "testous:wakelock";

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = (Calendar) intent.getSerializableExtra("calendar");
        Intent alarmServiceIntent = new Intent(context,AlarmService.class);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            context.startForegroundService(alarmServiceIntent);
        }else{
            context.startService(alarmServiceIntent);
        }
//        if (sCpuWakeLock != null) {
//            return;
//        }else{
//
//        }
//
//        if (sWifiLock != null) {
//            return;
//        }
//
//        WifiManager wifiManager = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
//        sWifiLock = wifiManager.createWifiLock("wifilock");
//        sWifiLock.setReferenceCounted(true);
//        sWifiLock.acquire();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
////        Log.d("start","AlarmReceiver");
//
//
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingI = PendingIntent.getActivity(context, 0,
//                notificationIntent, 0);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
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
    }
}
