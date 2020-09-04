package com.example.yourschedule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.yourschedule.ALARM.AlarmReceiver;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ListWidgetProvider extends AppWidgetProvider {

    private static String BT_CLICK_ACTION = "android.action.MY_ACTION";
    public static final String EXTRA_ITEM = "com.example.yourschedule.EXTRA_ITEM";
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
    SharePref sharePref = new SharePref();
    FirebaseAuth auth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {

//
////        여기부분 다 사용할 일 없어져서 주석처리함!
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
////        views.setTextViewText(R.id.widgetTitle, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("plaeseUpdate","onUpdate!");
        Calendar calendar = Calendar.getInstance();
        for (int appWidgetId : appWidgetIds) {
            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
            views.setTextViewText(R.id.widgetTitle, "Today Schedule");
            views.setRemoteAdapter(R.id.widget_listView, serviceIntent);
            Intent clickIntent = new Intent(context, ListWidgetProvider.class);
            clickIntent.setAction(ListWidgetProvider.BT_CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_listView, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetIds, views);
    }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("plaeseUpdate","onReceive!");

        String today = sdf.format(calendar.getTime());
        if(sharePref.get(context, today)!=null) {
            ScheduleDTO scheduleDTO = sharePref.get(context, today);
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            String action = intent.getAction();
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
            if (action.equals(BT_CLICK_ACTION)) {
                int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);
                int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
                List<Boolean> chkList = (List<Boolean>) intent.getSerializableExtra("chkList");
                List<String> scheduleList = intent.getStringArrayListExtra("scheduleList");
                remoteViews.setTextViewText(R.id.item, scheduleList.get(viewIndex));
                if (chkList.get(viewIndex) == false) {
                    Log.d("result", "false");
                    scheduleDTO.getIsComplete().set(viewIndex, true);
//                remoteViews.setImageViewResource(R.id.chkBt,R.drawable.baseline_check_circle_white_18);
                } else {
                    Log.d("result", "true");
                    scheduleDTO.getIsComplete().set(viewIndex, false);
//                remoteViews.setImageViewResource(R.id.chkBt,R.drawable.baseline_panorama_fish_eye_white_18);
                }
                auth = FirebaseAuth.getInstance();

                mDatabase.child(auth.getCurrentUser().getDisplayName())
                        .child(today.replace(".", "-"))
                        .setValue(scheduleDTO);
                sharePref.addToShardPref(context, scheduleDTO);
//            mgr.updateAppWidget(appWidgetId, remoteViews);
                mgr.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listView);
            }
        }
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d("plaeseUpdate","onEdabled!");
        dayUpdate(context);
    }


    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    void dayUpdate(Context context) {
        final Calendar midnight = Calendar.getInstance();
        Date now = new Date();
        midnight.setTime(new Date());
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0
        );
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);

        Intent alarmIntent = new Intent(context, AlarmManagerBroadcasrReceiver.class);
//        alarmIntent.setAction("test");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, (int) midnight.getTimeInMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, midnight.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

        if (midnight.getTimeInMillis() - now.getTime() <= 0) {
            Log.d("compare", "12시 지남");
        } else {
            Log.d("compare", "12시 안지남");
        }
    }

}

