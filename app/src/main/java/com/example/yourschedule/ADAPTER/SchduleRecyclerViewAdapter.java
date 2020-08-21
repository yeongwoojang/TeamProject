package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
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


    //일정을 저장하는 메소드

    public void close() {
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


            mDatabase.child(auth.getCurrentUser().
                    getDisplayName())
                    .child(date.replace(".", "-"))
                    .setValue(scheduleDTO);
            notifyItemChanged(scheduleListSize);


        } else {
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().equals(date)) {
                    mDatabase.child(auth.getCurrentUser().
                            getDisplayName()).
                            child(date.replace(".", "-"))
                            .removeValue();
                    break;
                }
            }
        }
    }

    public void addSchedule(String newData) {

        auth = FirebaseAuth.getInstance();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Boolean> isComplete = new ArrayList<>();
        scheduleListSet.removeAll(Collections.singleton(""));
        if (!newData.equals("")) {
            scheduleListSet.add(newData);
        }
        for (int i = 0; i < scheduleListSet.size(); i++) {
            isComplete.add(false);
        }


        for (int i = 0; i < scheduleListSet.size(); i++) {
            for (int k = 0; k < scheduleSet.size(); k++) {
                if (scheduleListSet.get(i).equals(scheduleSet.get(k))) {
                    Log.d("DB_Data", "DB에 " + scheduleSet.get(k) + "가 저장되어 있습니다.");
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
            Log.d("calendar", calendar.getTime() + "");
            SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String alarmTime = date + " 12:00:00";
            Log.d("alarmTime", alarmTime);
            try {
                currentDateTime = fm.parse(alarmTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            Calendar insertCalendar = Calendar.getInstance();
            insertCalendar.setTime(currentDateTime);
            insertCalendar.setTimeInMillis(insertCalendar.getTimeInMillis());

            Calendar currentCalendar = Calendar.getInstance();
//            long currentTime = currentCalendar.getTimeInMillis();

            SharedPreferences sharedPreferences= activity.getSharedPreferences("daily alarm", MODE_PRIVATE);
//            long time = sharedPreferences.getLong(alarmTime.substring(0, 10) + "", calendar.getTimeInMillis());


            //Preference에 설정한 값 저장

            if(!sharedPreferences.contains(alarmTime.substring(0, 10))||
                    (insertCalendar.compareTo(currentCalendar)==1||
                    insertCalendar.compareTo(currentCalendar)==0)){
                SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong(alarmTime.substring(0, 10) + "", (long) insertCalendar.getTimeInMillis());
                editor.apply();
                diaryNotification(insertCalendar, alarmTime.substring(0, 10));
            }
            mDatabase.child(auth.getCurrentUser().
                    getDisplayName())
                    .child(date.replace(".", "-"))
                    .setValue(scheduleDTO);
            notifyItemChanged(scheduleListSize);


        } else {
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().equals(date)) {
                    mDatabase.child(auth.getCurrentUser().
                            getDisplayName()).
                            child(date.replace(".", "-"))
                            .removeValue();
                    break;
                }
            }

        }
    }


    void diaryNotification(Calendar calendar, String aTime) {
        String year = aTime.substring(0, 10).substring(0, 4);
        String month = aTime.substring(0, 10).substring(5, 7);
        String day = aTime.substring(8, 10);
        Boolean dailyNotify = true; // 무조건 알람을 사용

        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();

        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        alarmIntent.setAction("test");
        alarmIntent.putExtra("requestCode", year + month + day);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, Integer.parseInt(year + month + day),
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
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
