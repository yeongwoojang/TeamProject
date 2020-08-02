package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;

import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder>{

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<String> scheduleDTO;
    private List<String> thatDates;
    private String dateOfMonth;

    public RateAdapter(Activity activity, List<String> thatDates,List<String> scheduleDTO, String dateOfMonth) {
        this.activity = activity;
        this.scheduleDTO = scheduleDTO;
        this.dateOfMonth = dateOfMonth;
        this.thatDates = thatDates;
        for(int i=0;i<thatDates.size();i++){
            Log.d("thatDates",thatDates.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return scheduleDTO.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.complete_item, parent, false);
        ViewHolder viewHolder = new RateAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.thatDate.setText(thatDates.get(position));
        holder.completeScheduleText.setText(scheduleDTO.get(position));
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView thatDate;
        TextView completeScheduleText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            completeScheduleText = (TextView)itemView.findViewById(R.id.complete_schedule);
            thatDate = (TextView)itemView.findViewById(R.id.date);
        }
    }
}
