package com.example.yourschedule;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class AlarmManagerBroadcasrReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("plaeseUpdate","onreceive!");

        Intent serviceIntent = new Intent(context, WidgetRemoteViewsService.class);
        serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  AppWidgetManager.INVALID_APPWIDGET_ID);
        serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setRemoteAdapter(R.id.widget_listView, serviceIntent);

        ComponentName thisWidget = new ComponentName(context, ListWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(thisWidget,views);
    }
}
