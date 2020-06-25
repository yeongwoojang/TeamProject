package com.example.yourschedule.ADAPTER;

import android.app.Activity;
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

import com.example.yourschedule.R;
import com.example.yourschedule.SharePref;

import java.util.ArrayList;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    private ArrayList<String[]> schedules = new ArrayList<String[]>();
    private Activity activity;
    private String date;
    private ArrayList<String> dataSet = new ArrayList<String>();
    boolean isUpdate;
    int scheduleListSize;
    SharePref pref = new SharePref();
    public SchduleRecyclerViewAdapter(Activity activity,String date,boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.activity = activity;
        this.date = date;
        if(isUpdate){
            dataSet = pref.get(activity,date);
            Log.d("aaaSize",dataSet.size()+"");
        }else{
            for(int i=0;i<3;i++){
                dataSet.add(i,"");
            }
            Log.d("aaaSize",dataSet.size()+"");
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        scheduleListSize = dataSet.size();
        if(!isUpdate){
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
                    dataSet.set(position,editable.toString());
                    Log.d("aaaPosition",position+"");
                }
            });
        }else{
            holder.editText.setText(dataSet.get(position));
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
                    dataSet.set(position,editable.toString());
                    Log.d("aaaPosition",position+"");
                }
            });

        }

    }

    //리사이클러뷰의 사이즈를 얻어오는 메소드
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    //일정 입력 EditText를 추가하는 메소드
    public void addEditText(){
        if(scheduleListSize<5){
            dataSet.add(scheduleListSize,"");
            notifyItemChanged(scheduleListSize);
        }else{
            Toast.makeText(activity,"최대 5개의 일정만 입력가능",Toast.LENGTH_SHORT).show();
        }
    }
    //일정을 저장하는 메소드
    public void addSchedule(String key){
        SharePref pref = new SharePref();
            for(int i = 0; i< scheduleListSize; i++){
                if(!(dataSet.get(i).length()==0)){
                    schedules.add(new String[]{dataSet.get(i),"false"});
                }
            }

        if(schedules.size()==0){
            Log.d("aaa","하나 이상의 일정을 입력하세요");
        }
        pref.set(activity,key,schedules);
    }
}
