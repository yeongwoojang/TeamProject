package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;

import java.util.ArrayList;
import java.util.List;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    private ArrayList<String[]> schedules = new ArrayList<String[]>();
    private List<String> idx;
    private Activity activity;
    private ArrayList<String> dataset = new ArrayList<String>();
    int AllscheduleSize;
    public SchduleRecyclerViewAdapter(List<String> idx,Activity activity) {
        this.idx = idx;
        this.activity = activity;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleNum;
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            scheduleNum = (TextView) itemView.findViewById(R.id.schedule);
            editText = (EditText) itemView.findViewById(R.id.scheduleEditText);
        }
    }



    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d("aaa",idx.get(position));
        holder.scheduleNum.setText(idx.get(position));
        holder.editText.setHint("일정을 입력하세요");
        Log.d("aaa","positon : "+position+"");
        AllscheduleSize = idx.size();
        dataset.add(0,"");

        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                dataset.set(position,editable.toString());
                Log.d("aaa",position+"");
                Log.d("aaa", dataset.get(position)+"입니다.");
                Log.d("aaa", dataset.size()+"size");
            }
        });
    }

    @Override
    public int getItemCount() {
        return idx.size();
    }

    public void addSchedule(String key){
        SharePref pref = new SharePref();
        for(int i=0; i<AllscheduleSize; i++){
            if(!(dataset.get(i).length()==0)){
                schedules.add(new String[]{dataset.get(i),"false"});
            }
        }
        if(schedules.size()==0){
            Log.d("aaa","하나 이상의 일정을 입력하세요");
        }
        pref.set(activity,key,schedules);
    }
}
