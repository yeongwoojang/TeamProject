package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ListWidgetProvider;
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private List<ScheduleDTO> scheduleDTOS;
    private String today;
    private int itemIndex;
    private int listCount;
    FirebaseAuth auth;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");

    public RecyclerViewAdapter(Activity activity, List<ScheduleDTO> scheduleDTOS,String today){
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.today = today;
    }

    @Override
    public int getItemCount(){

        for(int i=0;i<scheduleDTOS.size();i++){
            if(scheduleDTOS.get(i).getDate().equals(today)){
                itemIndex = i;
                listCount = scheduleDTOS.get(i).getSchedule().size();
                return listCount;
            }
        }
        return 0;
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView item;
        CheckBox scheduleChk;


        public ViewHolder(View itemView){
            super(itemView);
            item = (TextView)itemView.findViewById(R.id.item);
            scheduleChk = (CheckBox)itemView.findViewById(R.id.scheduleChk);

//
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_today,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int posotion){
                //ItemInex로 오늘 날짜인지를 구분.
                holder.item.setText(scheduleDTOS.get(itemIndex).getSchedule().get(posotion));
                holder.scheduleChk.setChecked(scheduleDTOS.get(itemIndex).getIsComplete().get(posotion));
                holder.scheduleChk.setTag(scheduleDTOS.get(itemIndex).getSchedule().get(posotion));


                if(chkValue(scheduleDTOS.get(itemIndex).getSchedule().get(posotion))){
                    scheduleDTOS.get(itemIndex).getIsComplete().set(posotion,true);
                     holder.scheduleChk.setChecked(scheduleDTOS.get(itemIndex).getIsComplete().get(posotion));
    }

                holder.scheduleChk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox)v;

//                        Schdule contact  = (Schdule)checkBox.getTag();

//                        holder.scheduleChk.
//                        contact.setChk(checkBox.isChecked());
//                        schdules.get(posotion).setChk(checkBox.isChecked());
                        changeScheduleChkValue(scheduleDTOS.get(itemIndex).getSchedule().get(posotion));
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                                new ComponentName(activity, ListWidgetProvider.class));
                        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_listView);
                    }
                });



    }


    public void changeScheduleChkValue(String value){
        for(int i=0;i<listCount;i++){
            if(scheduleDTOS.get(itemIndex).getSchedule().get(i).equals(value)){
                if( scheduleDTOS.get(itemIndex).getIsComplete().get(i).equals(false)){
                    scheduleDTOS.get(itemIndex).getIsComplete().set(i,true);
                }else{
                    scheduleDTOS.get(itemIndex).getIsComplete().set(i,false);
                }
            }

        }
        auth = FirebaseAuth.getInstance();
        ScheduleDTO scheduleDTO = scheduleDTOS.get(itemIndex);
        mDatabase.child(auth.getCurrentUser().getDisplayName())
                .child(today.replace(".","-"))
                .setValue(scheduleDTO);
        SharePref sharePref = new SharePref();
        sharePref.addToShardPref(activity,scheduleDTO);
    }

    public Boolean chkValue(String value) {
        for(int i=0; i<listCount;i++){
            if(scheduleDTOS.get(itemIndex).getSchedule().get(i).equals(value)){
                if(scheduleDTOS.get(itemIndex).getIsComplete().get(i)==false){
                    return false;
                }
            }
        }
        return true;
    }





}
