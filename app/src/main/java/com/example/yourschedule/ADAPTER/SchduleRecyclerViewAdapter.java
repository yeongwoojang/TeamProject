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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.AlarmReceiver;
import com.example.yourschedule.DeviceBootReceiver;
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
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
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
            for (int i = 0; i < 3; i++) {
                scheduleListSet.add(i, "");
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            editText = (EditText) itemView.findViewById(R.id.scheduleEditText);

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
        if (!isUpdate) {
            holder.editText.setHint("일정을 입력하세요.");
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                //리사이클러뷰에 있는 EditText의 텍스트가 입력이 되면 DataSet에 추가한다.
                @Override
                public void afterTextChanged(Editable editable) {
                    scheduleListSet.set(position, editable.toString());
                }
            });
        } else {
            holder.editText.setText(scheduleListSet.get(position));
            holder.editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                //리사이클러뷰에 있는 EditText의 텍스트가 입력이 되면 DataSet에 추가한다.
                @Override
                public void afterTextChanged(Editable editable) {
                    scheduleListSet.set(position, editable.toString());
                }
            });


        }

    }

    //리사이클러뷰의 사이즈를 얻어오는 메소드
    @Override
    public int getItemCount() {
        return scheduleListSet.size();
    }

    //일정 입력 EditText를 추가하는 메소드
    public void addEditText() {
        if (scheduleListSize < 5) {
            scheduleListSet.add(scheduleListSize, "");
            notifyItemChanged(scheduleListSize);
        } else {
            Toast.makeText(activity, "최대 5개의 일정만 입력가능", Toast.LENGTH_SHORT).show();
        }
    }

    //일정을 저장하는 메소드
    public void addSchedule() {


//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.YEAR,Integer.parseInt(alarmTime.substring(0,4)));
//        calendar.set(Calendar.MONTH,Integer.parseInt(alarmTime.substring(5,7))-1);
//        calendar.set(Calendar.DATE,Integer.parseInt(alarmTime.substring(8,10)));
//        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(alarmTime.substring(11,13)));
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);

//            editor.clear();
//            editor.commit();


//        long millis = sharedPreferences.getLong("nextNotifyTime", (long)calendar.getTimeInMillis());
//        Calendar nextNotifyTime = new GregorianCalendar();
//        nextNotifyTime.setTimeInMillis(millis);

//        Log.d("next",nextNotifyTime.getTime()+"");
        auth = FirebaseAuth.getInstance();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Boolean> isComplete = new ArrayList<>();
            scheduleListSet.removeAll(Collections.singleton(""));
            for(int i = 0; i < scheduleListSet.size(); i++){
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
        if (scheduleListSet.size() >= 1) {
            Arrays.asList(scheduleListSet);
            Arrays.asList(isComplete);
            scheduleDTO.setDate(date);
            scheduleDTO.setSchedule(scheduleListSet);
            scheduleDTO.setIsComplete(isComplete);
            Log.d("calendar",calendar.getTime()+"");

            SimpleDateFormat fm = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            String alarmTime = date+" 17:00:00";
            Log.d("alarmTime",alarmTime);
            try {
                currentDateTime = fm.parse(alarmTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDateTime);
            Log.d("cur",calendar.getTime()+"입니다.");
            Log.d("current",currentDateTime+"입니다.");
            Toast.makeText(activity,currentDateTime + "으로 알람이 설정되었습니다!", Toast.LENGTH_LONG).show();
            SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong("nextNotifyTime", (long)calendar.getTimeInMillis());
            editor.apply();
            diaryNotification(calendar);
        } else {
            Toast.makeText(activity, "하나 이상의 일정을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        mDatabase.child(auth.getCurrentUser().
                getDisplayName())
                .child(date.replace(".", "-"))
                .setValue(scheduleDTO);

    }


    void diaryNotification(Calendar calendar)
    {
//        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
//        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
//        Boolean dailyNotify = sharedPref.getBoolean(SettingsActivity.KEY_PREF_DAILY_NOTIFICATION, true);
        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = activity.getPackageManager();
        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
//        else { //Disable Daily Notifications
//            if (PendingIntent.getBroadcast(this, 0, alarmIntent, 0) != null && alarmManager != null) {
//                alarmManager.cancel(pendingIntent);
//                //Toast.makeText(this,"Notifications were disabled",Toast.LENGTH_SHORT).show();
//            }
//            pm.setComponentEnabledSetting(receiver,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP);
//        }
    }

}
