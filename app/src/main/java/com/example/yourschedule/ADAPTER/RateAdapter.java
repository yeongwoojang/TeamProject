package com.example.yourschedule.ADAPTER;

import android.animation.ValueAnimator;
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<ScheduleDTO> scheduleDTOS;
    private List<String> thatDates;
    private List<ScheduleDTO> scheduleOfDate = new ArrayList<ScheduleDTO>();
    private String dateOfMonth;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;
    private int colsePosition;
    LinearLayoutManager linearLayoutManager;
    ListSubViewAdapter listSubViewAdapter;


    public RateAdapter(Activity activity, List<String> thatDates, List<ScheduleDTO> scheduleDTOS, String dateOfMonth) {
        this.activity = activity;
        this.scheduleDTOS = scheduleDTOS;
        this.dateOfMonth = dateOfMonth;
        this.thatDates = thatDates;
        reSetDates();

    }

    public void closeSubView(){
        selectedItems.delete(colsePosition);

        if (prePosition != -1) {
            notifyItemChanged(prePosition);
        }
        notifyItemChanged(colsePosition);
        prePosition = colsePosition;
    }

    private void reSetDates() {
        HashSet<String> reSetDates = new HashSet<String>(thatDates);
        thatDates = new ArrayList<String>(reSetDates);
        Collections.sort(thatDates);
    }


    @Override
    public int getItemCount() {

        return thatDates.size();
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
        if (holder instanceof ViewHolder) {
            holder.onBind(position);
        }

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView thatDate;
        private RecyclerView recyclerView;
        private int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            completeScheduleText = (TextView)itemView.findViewById(R.id.complete_schedule);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.subRecycleView);
            thatDate = (TextView) itemView.findViewById(R.id.date);
        }

        void onBind(int position) {
            this.position = position;
            thatDate.setText(thatDates.get(position));

            changeVisibility(selectedItems.get(position));
            itemView.setOnClickListener(this);
            thatDate.setOnClickListener(this);

        }



        @Override
        public void onClick(View view) {
            colsePosition =0;
            scheduleOfDate.clear();
            for (int i = 0; i < scheduleDTOS.size(); i++) {
                if (scheduleDTOS.get(i).getDate().substring(5).equals(thatDates.get(position))) {
//                    for (int j = 0; j < scheduleDTOS.get(i).getIsComplete().size(); j++) {
//                        if (scheduleDTOS.get(i).getIsComplete().get(j)) {
//                            scheduleDTO.add(scheduleDTOS.get(i).getSchedule().get(j));
//                        }
//                    }
                    scheduleOfDate.add(scheduleDTOS.get(i));
                    break;
                }
            }


            //펼처진 상태
            if (selectedItems.get(position)) {
                selectedItems.delete(position);

                //펼쳐지지 않은 상태
            } else {
                selectedItems.delete(prePosition);
                selectedItems.put(position, true);
                colsePosition = position;


            }

            if (prePosition != -1) {
                notifyItemChanged(prePosition);
            }
            notifyItemChanged(position);
            prePosition = position;


        }

        private void changeVisibility(final boolean isExpanded) {

            int dpValue =120;
            float d = activity.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    int value = (int) animation.getAnimatedValue();
//                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(activity) {
//                        @Override protected int getVerticalSnapPreference() {
//                            Log.d("Asdfasdf","asdfasdf?");
//                            return LinearSmoothScroller.SNAP_TO_START;
//                        }
//                    };
                    linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(linearLayoutManager);
//                    smoothScroller.setTargetPosition(0);
//                    linearLayoutManager.startSmoothScroll(smoothScroller);
//                    recyclerView.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(activity));
//                    recyclerView.smoothScrollToPosition(position);
                    listSubViewAdapter = new ListSubViewAdapter(activity, scheduleOfDate, thatDates.get(position));

                    recyclerView.setAdapter(listSubViewAdapter);
//                    recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView,new RecyclerView.State(),getItemCount());
                    listSubViewAdapter.notifyItemChanged(position);

                    recyclerView.getLayoutParams().height = value;
//                    AnimationSet downSet = new AnimationSet(true);
//                    downSet.setInterpolator(new CycleInterpolator(1));
//                    Animation down = new TranslateAnimation(0,0,50.f,0);
//                    down.setDuration(100);
//                    downSet.addAnimation(down);

//                    downSet.setFillAfter(true);
//                    recyclerView.setAnimation(downSet);
                    recyclerView.requestLayout();
//                    recyclerView.startAnimation(downSet);
                    recyclerView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

                }
            });

            va.start();
        }


    }

}
