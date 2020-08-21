package com.example.yourschedule;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViewsService;

import com.example.yourschedule.ADAPTER.WidgetRemoteViewsFactory;
import com.example.yourschedule.OBJECT.ScheduleDTO;

import java.util.ArrayList;
import java.util.Collection;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetRemoteViewsService extends RemoteViewsService {

    public WidgetRemoteViewsService() {
        super();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("execute","onGetViewFactory");
        ArrayList<ScheduleDTO> dataSet = new ArrayList<>();
        dataSet.addAll(( ArrayList<ScheduleDTO>) intent.getSerializableExtra("dataSet"));
        Log.d("dateSet","service : "+dataSet.size());
        return new WidgetRemoteViewsFactory(this.getApplicationContext(),intent,dataSet);

    }
}
