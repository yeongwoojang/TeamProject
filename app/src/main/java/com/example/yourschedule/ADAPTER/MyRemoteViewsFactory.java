package com.example.yourschedule.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.load.engine.Resource;
import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.OBJECT.WidgetItem;
import com.example.yourschedule.R;

import java.util.ArrayList;

public class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    //context 설정하기
    public Context context = null;
    public ArrayList<Schdule> arrayList;
    Resources res = null;

    public MyRemoteViewsFactory(Context context) {
        this.context = context;
        res = context.getResources();
    }

    //DB를 대신하여 arrayList에 데이터를 추가하는 함수ㅋㅋ
    public void setData() {
        arrayList = new ArrayList<>();
        arrayList.add(new Schdule("1", false));
        arrayList.add(new Schdule("2", true));
        arrayList.add(new Schdule("3", true));
        arrayList.add(new Schdule("4", false));
    }

    //이 모든게 필수 오버라이드 메소드

    //실행 최초로 호출되는 함수
    @Override
    public void onCreate() {
        setData();
    }



    //항목 추가 및 제거 등 데이터 변경이 발생했을 때 호출되는 함수
    //브로드캐스트 리시버에서 notifyAppWidgetViewDataChanged()가 호출 될 때 자동 호출
    @Override
    public void onDataSetChanged() {
        setData();
    }

    //마지막에 호출되는 함수
    @Override
    public void onDestroy() {

    }

    // 항목 개수를 반환하는 함수
    @Override
    public int getCount() {
        return arrayList.size();
    }

    //각 항목을 구현하기 위해 호출, 매개변수 값을 참조하여 각 항목을 구성하기위한 로직이 담긴다.
    // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주어야 하는 함수
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews listviewWidget = new RemoteViews(context.getPackageName(), R.layout.item_collection);
        listviewWidget.setTextViewText(R.id.text1, arrayList.get(position).getItem());
        if(arrayList.get(position).isChk()==false){
            listviewWidget.setImageViewResource(R.id.scheduleChk, R.drawable.baseline_panorama_fish_eye_white_18);
        }
        else{
            listviewWidget.setImageViewResource(R.id.scheduleChk, R.drawable.baseline_check_circle_white_18);
        }

        // 항목 선택 이벤트 발생 시 인텐트에 담겨야 할 항목 데이터를 추가해주는 코드
        Intent dataIntent = new Intent();
        dataIntent.putExtra("item_id", arrayList.get(position).isChk());
        dataIntent.putExtra("item_data", arrayList.get(position).getItem());
        listviewWidget.setOnClickFillInIntent(R.id.text1, dataIntent);
        //setOnClickFillInIntent 브로드캐스트 리시버에서 항목 선택 이벤트가 발생할 때 실행을 의뢰한 인텐트에 각 항목의 데이터를 추가해주는 함수
        //브로드캐스트 리시버의 인텐트와 Extra 데이터가 담긴 인텐트를 함치는 역할을 한다.

        return listviewWidget;
    }

    //로딩 뷰를 표현하기 위해 호출, 없으면 null
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    //항목의 타입 갯수를 판단하기 위해 호출, 모든 항목이 같은 뷰 타입이라면 1을 반환하면 된다.
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    //각 항목의 식별자 값을 얻기 위해 호출
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 같은 ID가 항상 같은 개체를 참조하면 true 반환하는 함수
    @Override
    public boolean hasStableIds() {
        return false;
    }
}
