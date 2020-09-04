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
    private List<Boolean> chkList;



    public DrawerListAdapter(Activity activity, List<String> scheduleDTO,List<Boolean> chkList) {
        this.activity = activity;
        this.scheduleDTO = scheduleDTO;
        this.chkList = chkList;

    }

    @Override
    public int getItemCount() {
        return scheduleDTO.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleText;
        TextView chkText;
        public ViewHolder(View itemView) {
            super(itemView);
            scheduleText = (TextView) itemView.findViewById(R.id.drawerSchedule);
            chkText = (TextView)itemView.findViewById(R.id.drawerChk);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.scheduleText.setText(scheduleDTO.get(position));
        holder.chkText.setText(chkList.get(position) ? "Complete" : "Incomplete");
        }

}

