package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DrawerListAdapter extends RecyclerView.Adapter<DrawerListAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<String> scheduleDTO;
    private int month;

    public DrawerListAdapter(Activity activity, List<String> scheduleDTO) {
        this.activity = activity;
        this.scheduleDTO = scheduleDTO;

    }

    @Override
    public int getItemCount() {
        return scheduleDTO.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleText;
        public ViewHolder(View itemView) {
            super(itemView);
            scheduleText = (TextView) itemView.findViewById(R.id.drawerSchedule);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int posotion) {

        holder.scheduleText.setText(scheduleDTO.get(posotion));
        }

        //해당 날짜가 그 달의 몇주차인지 구하는 메소드
    public static String getWeek(String date){
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        Date test = null;
        try{
            test= transFormat.parse(date);
        }catch (Exception e){}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(test);
        String week = String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
        return week;
    }

    //해당 월이 몇주차까지 있는지 구하는 메소드
    public int Weeks()
    {
        Date formatDate =null;
        String week ="";
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        Calendar calendar = Calendar.getInstance();
        int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        String date = year+"."+month+"."+lastDayOfMonth;
        try{
            formatDate = transFormat.parse(date);
            calendar.setTime(formatDate);
            week = String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
        }catch (Exception e){}
        return Integer.parseInt(week);
    }

}

