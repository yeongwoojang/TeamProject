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

import java.util.ArrayList;
import java.util.List;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {
    private List<String> scheduls = new ArrayList<String>();
    private List<String> idx;
    private Activity activity;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("aaa",idx.get(position));
        holder.scheduleNum.setText(idx.get(position));
        holder.editText.setHint("일정을 입력하세요");
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                scheduls.add(editable.toString());
            }
        });
        scheduls.add(holder.editText.getText().toString());
    }

    @Override
    public int getItemCount() {
        return idx.size();
    }

    public void addSchedule(List<String> s){
        scheduls = s;
    }
}
