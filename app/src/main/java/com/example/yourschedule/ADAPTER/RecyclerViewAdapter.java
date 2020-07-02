package com.example.yourschedule.ADAPTER;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.FRAGMENT.MyList;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    private Activity activity;
    private List<Schdule> schdules;
    private MyList gp;

    public RecyclerViewAdapter(Activity activity, List<Schdule> schdules){
        this.activity = activity;
        this.schdules = schdules;

    }

    @Override
    public int getItemCount(){
        return schdules.size();
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
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
            final String today = transFormat.format(calendar.getTime());


                holder.item.setText(schdules.get(posotion).getItem());
                holder.scheduleChk.setChecked(schdules.get(posotion).isChk());
                holder.scheduleChk.setTag(schdules.get(posotion));



                if(chkValue(today,schdules.get(posotion).getItem())){
                    schdules.get(posotion).setChk(true);
        holder.scheduleChk.setChecked(schdules.get(posotion).isChk());
    }

                holder.scheduleChk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox checkBox = (CheckBox)v;
                        Schdule contact  = (Schdule)checkBox.getTag();

                        contact.setChk(checkBox.isChecked());
                        schdules.get(posotion).setChk(checkBox.isChecked());


                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("일정완료?")
                                .setMessage("선택하세요")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        changeScheduleChkValue(today,schdules.get(posotion).getItem());
                                    }
                                })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create()
                                .show();

                        Toast.makeText(
                                v.getContext(),
                                "Clicked on Checkbox: " + schdules.get(posotion).getItem() + " is "
                                        + checkBox.isChecked(), Toast.LENGTH_LONG).show();
                    }

                });



    }


    public void changeScheduleChkValue(String key, String value){
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String json = pref.getString(key, null);

        JSONObject jsonObject = null;
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                }
                if(jsonObject.getString(value).equals("false")){
                    jsonObject.put(value,"true");
                }else{
                    jsonObject.put(value,"false");
                }
                Log.d("jsonArray",jsonArray+"");
                editor.putString(key,jsonArray.toString()).apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean chkValue(String key, String value) {
        SharedPreferences pref = activity.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        String json = pref.getString(key, null);
        ArrayList<String> keyList = new ArrayList<>();
        JSONObject jsonObject = null;
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                }
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String kTemp = iterator.next().toString();
                        keyList.add(kTemp);
                }
                for (int i = 0; i < jsonObject.length(); i++) {
                    if (jsonObject.getString(value).equals("true")) {
                        return true;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private void removeItemView(int position){
        schdules.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,schdules.size());
    }


}
