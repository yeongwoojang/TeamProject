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

public class ListSubViewAdapter extends RecyclerView.Adapter<ListSubViewAdapter.ViewHolder> {

    List<ScheduleDTO> scheduleDTOS;
    Activity activity;
    String thatDate;
    public ListSubViewAdapter(Activity activity, List<ScheduleDTO> scheduleDTOS, String thatDate) {
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.thatDate = thatDate;
        for(int i=0;i<scheduleDTOS.size();i++){
            Log.d("data",scheduleDTOS.get(i).getSchedule()+"");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_sub_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.completeItem.setText(scheduleDTOS.get(0).getSchedule().get(position));
        Log.d("sibal",scheduleDTOS.get(0).getSchedule().get(position));


    }

    @Override
    public int getItemCount() {
        return scheduleDTOS.get(0).getSchedule().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView completeItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            completeItem = itemView.findViewById(R.id.completeItem);

//
        }


    }
}

