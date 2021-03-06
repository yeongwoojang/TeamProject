package com.example.yourschedule.ALARM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Switch;

import androidx.core.app.NotificationCompat;

import com.example.yourschedule.ACTIVITY.LoginActivity;
import com.example.yourschedule.ADAPTER.SchduleRecyclerViewAdapter;
import com.example.yourschedule.R;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    final private static String ALARM_CANCLE_ACTION = "alarmCancleACTION";
    final private static String ALARM_CALL_ACTION = "alarmCallAction";

    @Override
    public void onReceive(Context context, Intent intent) {

        switch (intent.getAction()) {
            case ALARM_CALL_ACTION:
                Log.d("condition", "AlarmCall");
                String requestCode = intent.getStringExtra("requestCode");
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent notificationIntent = new Intent(context, LoginActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(requestCode),
                        notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 쓰면 Oreo 이상에서 시스템 UI 에러난다..

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
                } else
                    builder.setSmallIcon(R.drawable.app_icon); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남

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
                    notificationManager.notify(1234, builder.build());

                }

                break;
            case ALARM_CANCLE_ACTION:

                break;
        }

//        Intent alarmServiceIntent = new Intent(context,AlarmService.class);
//        alarmServiceIntent.putExtra("requestCode",requestCode);
//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
//            context.startForegroundService(alarmServiceIntent);
//        }else{
//            context.startService(alarmServiceIntent);
//        }
//        String requestCode = intent.getStringExtra("requestCode");
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        Intent notificationIntent = new Intent(context, MainActivity.class);
//        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, Integer.parseInt(requestCode),
//                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
//
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 쓰면 Oreo 이상에서 시스템 UI 에러난다..
//
//            String channelId = "default";
//            String channelName = "일정 알람 채널";
//            String description = "API 26버전 이상을 위한 것입니다.";
//
//            //NotificationManager.IMPORTANCE_HIGH :  소리와 알림메시지를 같이 보여줌
//            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
//            channel.setDescription(description);
//
//            if (notificationManager != null) {
//                // 노티피케이션 채널을 시스템에 등록
//                notificationManager.createNotificationChannel(channel);
//            }
//        }else builder.setSmallIcon(R.mipmap.ic_launcher); // Oreo 이하에서 mipmap 사용하지 않으면 Couldn't create icon: StatusBarIcon 에러남
//
//        builder.setAutoCancel(true)
//                .setDefaults(NotificationCompat.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setTicker("{Time to watch some cool stuff!}")
//                .setContentTitle("YourList")
//                .setContentText("오늘 일정을 확인하세요!")
//                .setContentInfo("INFO")
//                .setContentIntent(pendingIntent);
//        if (notificationManager != null) {
//            // 노티피케이션 동작시킴
//            SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd");
//            Calendar calendar = Calendar.getInstance();
//            String CompareDate = fm.format(calendar.getTime());
//            String year = CompareDate.substring(0,10).substring(0,4);
//            String month = CompareDate.substring(0,10).substring(5,7);
//            String day = CompareDate.substring(8,10);
////            SharedPreferences sharedPreferences= context.getSharedPreferences("daily alarm", MODE_PRIVATE);
////            long time = sharedPreferences.getLong(year+"."+month+"."+day, calendar.getTimeInMillis());
////            if(calendar.getTimeInMillis()==time){
////                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//////                    startForeground(1, builder.build());
////                }
//            Log.d("noti","Beforenoti");
//                notificationManager.notify(1234, builder.build());
//////                thread.stopForever();
////            }
//        }
    }
}
