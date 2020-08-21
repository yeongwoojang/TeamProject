package com.example.yourschedule;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.example.yourschedule.ACTIVITY.MainActivity;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class ListWidget extends AppWidgetProvider {

    private static String BT_CLICK_ACTION = "android.action.MY_ACTION";

    FirebaseAuth auth;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();

//    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
//                                int appWidgetId) {
//
////        여기부분 다 사용할 일 없어져서 주석처리함!
//        CharSequence widgetText = context.getString(R.string.appwidget_text);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_widget);
////        views.setTextViewText(R.id.widgetTitle, widgetText);
//
//        // Instruct the widget manager to update the widget
//        appWidgetManager.updateAppWidget(appWidgetId, views);
//    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        Log.d("execute", "onUpdate");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            Log.d("widgetId", appWidgetId + "");
//            updateAppWidget(context, appWidgetManager, appWidgetId);

//            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_widget);
            initializeData(context, appWidgetId,appWidgetManager);
//            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            Log.d("please", scheduleDTOS.size() + "");
//            serviceIntent.putExtra("dataSet", (Serializable) scheduleDTOS);
//            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
//
//            views.setRemoteAdapter(R.id.widget_listView, serviceIntent);
//            Log.d("execute", "callService");
//            Log.d("execute", "setRemoteAdapter");

//            Intent clickIntent = new Intent(context, ListWidget.class);
//            clickIntent.setAction(ListWidget.BT_CLICK_ACTION);
//            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
//            clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//            views.setPendingIntentTemplate(R.id.widget_listView, pendingIntent);

//            appWidgetManager.updateAppWidget(appWidgetIds, views);
            Log.d("execute", "updateAppWidget");
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,R.id.widget_listView);

        }

    }

    //    @Override
//    public void onReceive(Context context, Intent intent) {
//        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
//        Log.d("execute","onReceive");
//        String action = intent.getAction();
//        RemoteViews listviewWidget = new RemoteViews(context.getPackageName(), R.layout.widget_item);
//        if(action.equals(BT_CLICK_ACTION)) {
//            int viewIndex = intent.getIntExtra("item", 0);
//            ArrayList<Schdule> arrayList = new ArrayList<>();
//            arrayList = (ArrayList<Schdule>) intent.getSerializableExtra("array");
//            if(arrayList.get(viewIndex).isChk()){
//                arrayList.get(viewIndex).s
//                listviewWidget.setImageViewResource(R.id.chkBt, R.drawable.baseline_panorama_fish_eye_white_18);
//            }else {
//                listviewWidget.setImageViewResource(R.id.chkBt, R.drawable.baseline_check_circle_white_18);
//            }
//            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
//            serviceIntent.putExtra("arrayList", arrayList);
//            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.test_widget);
//            widget.setRemoteAdapter(R.id.widget_listView, serviceIntent);
//        }
////        listviewWidget.setOnClickPendingIntent(R.id.chkBt,getPendingIntent(context,R.id.chkBt));
////
////        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
////        ComponentName cpName = new ComponentName(context, ListWidget.class);
////        appWidgetManager.updateAppWidget(cpName, listviewWidget);
////            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
////            serviceIntent.putExtra("arrayList", arrayList);
////            RemoteViews widget = new RemoteViews(context.getPackageName(), R.layout.test_widget);
////            widget.setRemoteAdapter(R.id.widget_listView, serviceIntent);
//        super.onReceive(context, intent);
//
//    }
    private PendingIntent getPendingIntent(Context context, int id) {
        Intent intent = new Intent(context, ListWidget.class);
        intent.setAction(BT_CLICK_ACTION);
        intent.putExtra("viewId", id);

        // 중요!!! getBroadcast를 이용할 때 동일한 Action명을 이용할 경우 서로 다른 request ID를 이용해야함
        // 아래와 같이 동일한 request ID를 주면 서로 다른 값을 putExtra()하더라도 제일 처음 값만 반환됨
        // return PendingIntent.getBroadcast(context, 0, intent, 0);
        return PendingIntent.getBroadcast(context, id, intent, 0);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }


    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    private void initializeData(Context context, int appWidgetIds,AppWidgetManager appWidgetManager) throws NullPointerException {
        try {
            scheduleDTOS.clear();
            ArrayList<ScheduleDTO> temp = new ArrayList<ScheduleDTO>();
            auth = FirebaseAuth.getInstance();
            mDatabase.child(auth.getCurrentUser().getDisplayName())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                                scheduleDTOS.add(scheduleDTO);
                            }
                            temp.addAll(scheduleDTOS);
                            Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
                            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
                            Log.d("please", scheduleDTOS.size() + "");
                            serviceIntent.putExtra("dataSet", (Serializable) scheduleDTOS);
                            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
                            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.test_widget);
                            views.setRemoteAdapter(R.id.widget_listView, serviceIntent);
                            appWidgetManager.updateAppWidget(appWidgetIds, views);


                            Log.d("execute", "dataLoadOK");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
        } catch (NullPointerException e) {
        }
    }
}

