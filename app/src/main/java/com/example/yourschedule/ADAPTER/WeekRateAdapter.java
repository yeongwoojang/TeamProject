package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;

import java.util.ArrayList;
import java.util.List;

public class WeekRateAdapter extends RecyclerView.Adapter<WeekRateAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<ScheduleDTO> scheduleDTOS;
    private List<String> weekList;
    private List<ScheduleDTO> scheduleOfDate = new ArrayList<ScheduleDTO>();
    private String dateOfMonth;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();



    public WeekRateAdapter(Activity activity, List<String> weekList, List<ScheduleDTO> scheduleDTOS, String dateOfMonth) {
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.dateOfMonth = dateOfMonth;
        this.weekList = weekList;

    }


    @Override
    public int getItemCount() {

        return weekList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_complete_item, parent, false);
        ViewHolder viewHolder = new WeekRateAdapter.ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.week.setText(weekList.get(position));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView week;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            week = (TextView) itemView.findViewById(R.id.week);
        }


    }

}
