package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
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
                Log.d("Firebase",listCount+"");
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int posotion){
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
//            final String today = transFormat.format(calendar.getTime());


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


                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("일정완료?")
                                .setMessage("선택하세요")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        changeScheduleChkValue(scheduleDTOS.get(itemIndex).getSchedule().get(posotion));
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("safdsdf",checkBox.getTag().toString());
                                        checkBox.setChecked(chkValue(checkBox.getTag().toString()));
                                    }
                                })
                                .create()
                                .show();
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
//        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        String json = pref.getString(key, null);
//
//        JSONObject jsonObject = null;
//        if (json != null) {
//            try {
//                JSONArray jsonArray = new JSONArray(json);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//                }
//                if(jsonObject.getString(value).equals("false")){
//                    jsonObject.put(value,"true");
//                }else{
//                    jsonObject.put(value,"false");
//                }
//                Log.d("jsonArray",jsonArray+"");
//                editor.putString(key,jsonArray.toString()).apply();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public Boolean chkValue(String value) {
        for(int i=0; i<listCount;i++){
            if(scheduleDTOS.get(itemIndex).getSchedule().get(i).equals(value)){
                if(scheduleDTOS.get(itemIndex).getIsComplete().get(i)==false){
                    return false;
                }
            }
        }
//        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
//        String json = pref.getString(key, null);
//        ArrayList<String> keyList = new ArrayList<>();
//        JSONObject jsonObject = null;
//        if (json != null) {
//            try {
//                JSONArray jsonArray = new JSONArray(json);
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    jsonObject = jsonArray.getJSONObject(i);
//                }
//                Iterator iterator = jsonObject.keys();
//                while (iterator.hasNext()) {
//                    String kTemp = iterator.next().toString();
//                        keyList.add(kTemp);
//                }
//                for (int i = 0; i < jsonObject.length(); i++) {
//                    if (jsonObject.getString(value).equals("true")) {
//                        return true;
//                    }
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return false;
        return true;
    }

//    private void removeItemView(int position){
//        schdules.remove(position);
//        notifyItemRemoved(position);
//        notifyItemRangeChanged(position,schdules.size());
//    }


}
