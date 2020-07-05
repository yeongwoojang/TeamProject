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

import java.util.List;

public class ScheduleOfWeekAdapter extends RecyclerView.Adapter<ScheduleOfWeekAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<ScheduleDTO> scheduleDTOS;
    private String today;
    FirebaseAuth auth;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");

    public ScheduleOfWeekAdapter(Activity activity, List<ScheduleDTO> scheduleDTOS) {
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
    }

    @Override
    public int getItemCount() {
        return 6;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleText;
        public ViewHolder(View itemView) {
            super(itemView);
            scheduleText = (TextView) itemView.findViewById(R.id.schedule);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_of_week_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int posotion) {


        holder.scheduleText.setText("aaaa");
        }
}

