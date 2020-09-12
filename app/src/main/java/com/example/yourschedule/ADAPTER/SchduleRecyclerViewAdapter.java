package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ALARM.AlarmReceiver;
import com.example.yourschedule.ALARM.DeviceBootReceiver;
import com.example.yourschedule.ListWidgetProvider;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {

    final private static String ALARM_CANCLE_ACTION = "alarmCancleACTION";
    final private static String ALARM_CALL_ACTION = "alarmCallAction";


    private Activity activity;
    private String date;
    private List<ScheduleDTO> scheduleDTOS;
    private ArrayList<String> scheduleListSet = new ArrayList<String>();
    private ArrayList<Boolean> completeSet = new ArrayList<Boolean>();
    private ArrayList<String> scheduleSet = new ArrayList<String>();
    boolean isUpdate;
    int scheduleListSize;
    FirebaseAuth auth;
    Date currentDateTime;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");
    Calendar calendar = Calendar.getInstance();

    public SchduleRecyclerViewAdapter(Activity activity, List<ScheduleDTO> scheduleDTOS, String date, boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.date = date;
        if (isUpdate) {
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().equals(date)) {
                    scheduleListSet = (ArrayList<String>) scheduleDTOS.get(i).getSchedule();
                    completeSet = (ArrayList<Boolean>) scheduleDTOS.get(i).getIsComplete();
                }
            }
            scheduleSet.addAll(scheduleListSet);
        } else {

        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textview;
        ImageButton deleteBt;

        public ViewHolder(View itemView) {
            super(itemView);
            textview = (TextView) itemView.findViewById(R.id.scItem);
            deleteBt = (ImageButton) itemView.findViewById(R.id.deleteBt);
            deleteBt.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            delete(getAdapterPosition());
        }

        public void delete(int position) {
            try {
                scheduleListSet.remove(position);
                notifyItemRemoved(position);
            } catch (IndexOutOfBoundsException e) {
            }
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        scheduleListSize = scheduleListSet.size();
        holder.deleteBt.setTag(holder.getAdapterPosition());
        if (!isUpdate) {
            holder.textview.setText(scheduleListSet.get(position));
        } else {
            holder.textview.setText(scheduleListSet.get(position));

        }

    }

    //리사이클러뷰의 사이즈를 얻어오는 메소드
    @Override
    public int getItemCount() {
        return scheduleListSet.size();
    }


    public void close() {
        SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
        String alarmTime = date + " 12:00:00";

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

        auth = FirebaseAuth.getInstance();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Boolean> isComplete = new ArrayList<>();
        scheduleListSet.removeAll(Collections.singleton(""));
        for (int i = 0; i < scheduleListSet.size(); i++) {
            isComplete.add(false);
        }


        for (int i = 0; i < scheduleListSet.size(); i++) {
            for (int k = 0; k < scheduleSet.size(); k++) {
                if (scheduleListSet.get(i).equals(scheduleSet.get(k))) {
                    if (completeSet.get(k) == true) {
                        isComplete.set(i, true);
                        break;
                    }
                }
            }
        }

        if (scheduleListSet.size() > 0) {
            Arrays.asList(scheduleListSet);
            Arrays.asList(isComplete);
            scheduleDTO.setDate(date);
            scheduleDTO.setSchedule(scheduleListSet);
            scheduleDTO.setIsComplete(isComplete);

            try {
                currentDateTime = fm.parse(alarmTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            mDatabase.child(auth.getCurrentUser().
                    getDisplayName())
                    .child(date.replace(".", "-"))
                    .setValue(scheduleDTO);
            notifyItemChanged(scheduleListSize);

            Calendar insertCalendar = Calendar.getInstance();
            insertCalendar.setTime(currentDateTime);
            insertCalendar.setTimeInMillis(insertCalendar.getTimeInMillis());

            Calendar currentCalendar = Calendar.getInstance();
            SharedPreferences sharedPreferences = activity.getSharedPreferences("daily alarm", MODE_PRIVATE);


            if (sharedPreferences.contains(alarmTime.substring(0,10)) == false &&
                    (insertCalendar.compareTo(currentCalendar) == 1 ||
                            insertCalendar.compareTo(currentCalendar) == 0)) {
                Log.d("condition", "true");
                SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong(alarmTime.substring(0, 10) + "", (long) insertCalendar.getTimeInMillis());
                editor.apply();
                diaryNotification(insertCalendar, alarmManager, alarmTime.substring(0, 10));
            }

            SharePref sharePref = new SharePref();
            sharePref.addToShardPref(activity, scheduleDTO);

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                    new ComponentName(activity, ListWidgetProvider.class));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);

        } else {
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().equals(date)) {
                    mDatabase.child(auth.getCurrentUser().
                            getDisplayName()).
                            child(date.replace(".", "-"))
                            .removeValue();
                    SharePref sharePref = new SharePref();
                    sharePref.deleteShardPref(activity, date + "Key");
//                    sharePref.deletaAll(activity);
                    SharedPreferences sharedPreferences = activity.getSharedPreferences("daily alarm", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    try {
//                        editor.clear();
                        editor.remove(alarmTime.substring(0, 10) + "");
                        editor.apply();
                        Log.d("remove!", "execute!");
                        Log.d("remover", sharedPreferences.getAll() + "");
                    } catch (Exception e) {
                    }

                    cancleAlarm(alarmManager, alarmTime.substring(0, 10));
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                    int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                            new ComponentName(activity, ListWidgetProvider.class));
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
                    break;
                }

            }

        }
    }

    public void test1() {

        String year = date.substring(0, 10).substring(0, 4);
        String month = date.substring(0, 10).substring(5, 7);
        String day = date.substring(8, 10);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.MINUTE, 34);


        Boolean dailyNotify = true; // 무조건 알람을 사용

        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();

        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        alarmIntent.setAction("test");
        alarmIntent.putExtra("requestCode", calendar1.getTimeInMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, (int) calendar1.getTimeInMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

//         사용자가 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
                }
            }
//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }


    public void test2() {

        String year = date.substring(0, 10).substring(0, 4);
        String month = date.substring(0, 10).substring(5, 7);
        String day = date.substring(8, 10);


        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.MINUTE, 35);


        Boolean dailyNotify = true; // 무조건 알람을 사용

        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();

        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        alarmIntent.setAction("test");
        alarmIntent.putExtra("requestCode", calendar2.getTimeInMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, (int) calendar2.getTimeInMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

//         사용자가 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
                }
            }
//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }


    public void test3() {

        String year = date.substring(0, 10).substring(0, 4);
        String month = date.substring(0, 10).substring(5, 7);
        String day = date.substring(8, 10);


        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.MINUTE, 36);


        Boolean dailyNotify = true; // 무조건 알람을 사용

        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();

        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        alarmIntent.setAction("test");
        alarmIntent.putExtra("requestCode", calendar3.getTimeInMillis());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, (int) calendar3.getTimeInMillis(),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

//         사용자가 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
                }
            }
//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

    public void addSchedule(String newData) {

        auth = FirebaseAuth.getInstance();
//        ScheduleDTO scheduleDTO = new ScheduleDTO();
//        List<Boolean> isComplete = new ArrayList<>();
        scheduleListSet.removeAll(Collections.singleton(""));
        if (!newData.equals("")) {
            if (!scheduleListSet.contains(newData)) {
                scheduleListSet.add(newData);
            } else {
                Toast.makeText(activity, "이미 기입한 내용입니다.", Toast.LENGTH_SHORT).show();
            }
        }


//            SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
////            editor.putLong(alarmTime.substring(0, 10) + "", (long) insertCalendar.getTimeInMillis());
//            editor.clear();
//            editor.apply();
//        for (int i = 0; i < scheduleListSet.size(); i++) {
//            isComplete.add(false);
//        }
//
//
//        for (int i = 0; i < scheduleListSet.size(); i++) {
//            for (int k = 0; k < scheduleSet.size(); k++) {
//                if (scheduleListSet.get(i).equals(scheduleSet.get(k))) {
//                    if (completeSet.get(k) == true) {
//                        isComplete.set(i, true);
//                        break;
//                    }
//                }
//            }
//        }

//        if (scheduleListSet.size() > 0) {
//            Arrays.asList(scheduleListSet);
//            Arrays.asList(isComplete);
//            scheduleDTO.setDate(date);
//            scheduleDTO.setSchedule(scheduleListSet);
//            scheduleDTO.setIsComplete(isComplete);
//            Log.d("calendar", calendar.getTime() + "");
//            SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
//            String alarmTime = date + " 12:00:00";
//            Log.d("alarmTime", alarmTime);
//            try {
//                currentDateTime = fm.parse(alarmTime);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }


//            Calendar insertCalendar = Calendar.getInstance();
//            insertCalendar.setTime(currentDateTime);
//            insertCalendar.setTimeInMillis(insertCalendar.getTimeInMillis());
//
//            Calendar currentCalendar = Calendar.getInstance();
//            SharedPreferences sharedPreferences= activity.getSharedPreferences("daily alarm", MODE_PRIVATE);
//
//

//            if(sharedPreferences.contains(alarmTime.substring(0, 10))==false&&
//                    (insertCalendar.compareTo(currentCalendar)==1||
//                    insertCalendar.compareTo(currentCalendar)==0)){
//                Log.d("condition","true");
//                SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
//                editor.putLong(alarmTime.substring(0, 10) + "", (long) insertCalendar.getTimeInMillis());
//                editor.apply();
//                diaryNotification(insertCalendar, alarmTime.substring(0, 10));
//            }
//            mDatabase.child(auth.getCurrentUser().
//                    getDisplayName())
//                    .child(date.replace(".", "-"))
//                    .setValue(scheduleDTO);
        notifyItemChanged(scheduleListSize);

//            sharePref.addToShardPref(activity,scheduleDTO);


//        } else {
//            for (int i = 0; i < scheduleDTOS.size(); i++) {
//                if (scheduleDTOS.get(i).getDate().equals(date)) {
//                    mDatabase.child(auth.getCurrentUser().
//                            getDisplayName()).
//                            child(date.replace(".", "-"))
//                            .removeValue();
//                    break;
//                }
//            }
//
//        }
    }

    void cancleAlarm(AlarmManager alarmManager, String aTime) {
        String year = aTime.substring(0, 10).substring(0, 4);
        String month = aTime.substring(0, 10).substring(5, 7);
        String day = aTime.substring(8, 10);

//        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        Intent alarmCancleIntent = new Intent(activity, AlarmReceiver.class);
        alarmCancleIntent.setAction(ALARM_CALL_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, Integer.parseInt(year + month + day),
                alarmCancleIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

    void diaryNotification(Calendar calendar, AlarmManager alarmManager, String aTime) {
        String year = aTime.substring(0, 10).substring(0, 4);
        String month = aTime.substring(0, 10).substring(5, 7);
        String day = aTime.substring(8, 10);
        Boolean dailyNotify = true; // 무조건 알람을 사용

        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();


        Intent alarmCallIntent = new Intent(activity, AlarmReceiver.class);
        alarmCallIntent.setAction(ALARM_CALL_ACTION);
        alarmCallIntent.putExtra("requestCode", year + month + day);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, Integer.parseInt(year + month + day),
                alarmCallIntent, PendingIntent.FLAG_UPDATE_CURRENT);

//        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("daily alarm", MODE_PRIVATE);
        long time = sharedPreferences.getLong(aTime + "", calendar.getTimeInMillis());

//         사용자가 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                } else {
                    alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
                }
            }
//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

}
