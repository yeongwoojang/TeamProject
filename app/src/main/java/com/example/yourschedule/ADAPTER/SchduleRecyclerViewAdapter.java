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

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SchduleRecyclerViewAdapter extends RecyclerView.Adapter<SchduleRecyclerViewAdapter.ViewHolder> {
    public final String PREFERENCE = "com.example.yourschdule.FRAGMENT";
    private Activity activity;
    private String date;
    private List<ScheduleDTO> scheduleDTOS;
    private ArrayList<String> scheduleListSet = new ArrayList<String>();
    private ArrayList<Boolean> completeSet = new ArrayList<Boolean>();
    private ArrayList<String> scheduleSet = new ArrayList<String>();
    boolean isUpdate;
    int scheduleListSize;
    FirebaseAuth auth;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("일정");


    public SchduleRecyclerViewAdapter(Activity activity, List<ScheduleDTO> scheduleDTOS, String date, boolean isUpdate) {
        this.isUpdate = isUpdate;
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.date = date;
        if (isUpdate) {
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().equals(date)) {
                    scheduleListSet = (ArrayList<String>) scheduleDTOS.get(i).getSchedule();
                    completeSet = (ArrayList<Boolean>) scheduleDTOS.get(i).getIsComplete();
                }
            }
            scheduleSet.addAll(scheduleListSet);

        } else {
            for (int i = 0; i < 3; i++) {
                scheduleListSet.add(i, "");
            }
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        scheduleListSize = scheduleListSet.size();
        if (!isUpdate) {
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
                    scheduleListSet.set(position, editable.toString());
                }
            });
        } else {
            holder.editText.setText(scheduleListSet.get(position));
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
                    scheduleListSet.set(position, editable.toString());
                }
            });


        }

    }

    //리사이클러뷰의 사이즈를 얻어오는 메소드
    @Override
    public int getItemCount() {
        return scheduleListSet.size();
    }

    //일정 입력 EditText를 추가하는 메소드
    public void addEditText() {
        if (scheduleListSize < 5) {
            scheduleListSet.add(scheduleListSize, "");
            notifyItemChanged(scheduleListSize);
        } else {
            Toast.makeText(activity, "최대 5개의 일정만 입력가능", Toast.LENGTH_SHORT).show();
        }
    }

    //일정을 저장하는 메소드
    public void addSchedule() {
        auth = FirebaseAuth.getInstance();
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        List<Boolean> isComplete = new ArrayList<>();
            scheduleListSet.removeAll(Collections.singleton(""));
            for(int i = 0; i < scheduleListSet.size(); i++){
                isComplete.add(false);
            }


            for (int i = 0; i < scheduleListSet.size(); i++) {
                for (int k = 0; k < scheduleSet.size(); k++) {
                    if (scheduleListSet.get(i).equals(scheduleSet.get(k))) {
                        Log.d("DB_Data", "DB에 " + scheduleSet.get(k) + "가 저장되어 있습니다.");
                        if (completeSet.get(k) == true) {
                            isComplete.set(i, true);
                            break;
                        }
                    }
                }
            }
        if (scheduleListSet.size() >= 1) {
            Arrays.asList(scheduleListSet);
            Arrays.asList(isComplete);
            scheduleDTO.setDate(date);
            scheduleDTO.setSchedule(scheduleListSet);
            scheduleDTO.setIsComplete(isComplete);
        } else {
            Toast.makeText(activity, "하나 이상의 일정을 입력하세요", Toast.LENGTH_SHORT).show();
        }
        mDatabase.child(auth.getCurrentUser().
                getDisplayName())
                .child(date.replace(".", "-"))
                .setValue(scheduleDTO);
    }
}
