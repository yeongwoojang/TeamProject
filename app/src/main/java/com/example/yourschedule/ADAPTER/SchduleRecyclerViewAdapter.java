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
import androidx.core.app.AlarmManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.AlamHATT;
import com.example.yourschedule.AlarmReceiver;
import com.example.yourschedule.AlarmService;
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
            String alarmTime = date+" 19:38:00";
            Log.d("alarmTime",alarmTime);
            try {
                currentDateTime = fm.parse(alarmTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDateTime);
            calendar.setTimeInMillis(calendar.getTimeInMillis());
            Toast.makeText(activity,alarmTime.substring(0,10)+" 12시에 알람이 설정되었습니다!", Toast.LENGTH_LONG).show();

            //Preference에 설정한 값 저장
            SharedPreferences.Editor editor = activity.getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
            editor.putLong(alarmTime.substring(0,10)+"", (long)calendar.getTimeInMillis());
            editor.apply();
            Log.d("split",alarmTime.substring(0,10)+"");


            diaryNotification(calendar,alarmTime.substring(0,10));
        } else {
            Toast.makeText(activity, "하나 이상의 일정을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        mDatabase.child(auth.getCurrentUser().
                getDisplayName())
                .child(date.replace(".", "-"))
                .setValue(scheduleDTO);

    }


    void diaryNotification(Calendar calendar,String aTime)
    {
        String year = aTime.substring(0,10).substring(0,4);
        String month = aTime.substring(0,10).substring(5,7);
        String day = aTime.substring(8,10);

        Boolean dailyNotify = true; // 무조건 알람을 사용
        ComponentName receiver = new ComponentName(activity, DeviceBootReceiver.class);
        PackageManager pm = activity.getPackageManager();

        Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
        alarmIntent.putExtra("calendar",calendar);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                activity, Integer.parseInt(year+month+day),
                alarmIntent,0);
//
        AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);
        SharedPreferences sharedPreferences= activity.getSharedPreferences("daily alarm", MODE_PRIVATE);
        long time = sharedPreferences.getLong(aTime+"", calendar.getTimeInMillis());
        Log.d("asdfasdf",time+"");

//         사용자가 매일 알람을 허용했다면
        if (dailyNotify) {
            if (alarmManager != null) {
//                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, (long)calendar.getTimeInMillis(), pendingIntent);
                alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(time,pendingIntent),pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(time,pendingIntent),pendingIntent);
                }
            }
//             부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }
    }

}
