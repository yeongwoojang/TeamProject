package com.example.yourschedule.FRAGMENT;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MyList extends Fragment {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Schdule> schdules = new ArrayList<>();
    ImageButton settingBt,closeSettingBt;
    TextView dayOfWeek,dateText,logoutBt,appUnLinkBt;
    DrawerLayout settingViewLayout;
    View settingView;
    String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
    FirebaseAuth auth;
    FirebaseDatabase mDatabase;
    SharePref pref = new SharePref();

    public MyList newInstance() {
        return new MyList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dateText = rootView.findViewById(R.id.date);
        settingBt = rootView.findViewById(R.id.settingBt);
        settingViewLayout = rootView.findViewById(R.id.settingLayout);
        settingView = rootView.findViewById(R.id.settingDetail);
        closeSettingBt = rootView.findViewById(R.id.closeSettingBt);
        logoutBt = rootView.findViewById(R.id.logoutBt);
        appUnLinkBt = rootView.findViewById(R.id.appUnlinkBt);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.addItemDecoration(
                new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);



        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");
        String today = transFormat.format(calendar.getTime());
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];
        dayOfWeek.setText(day);
        dateText.setText(today);

        mDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
                .child(today.replace(".","-"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Log.d("Firebase", snapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        settingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingViewLayout.openDrawer(settingView);
            }
        });
        closeSettingBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingViewLayout.closeDrawer(settingView);
            }
        });

        logoutBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance()
                        .requestLogout(new LogoutResponseCallback() {
                            @Override
                            public void onCompleteLogout() {
                                Log.i("KAKAO_API", "로그아웃 완료");
                                settingViewLayout.closeDrawer(settingView);
                            }
                        });
            }
        });

        appUnLinkBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance()
                        .requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                            }

                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                Log.e("KAKAO_API", "연결 끊기 실패: " + errorResult);

                            }
                            @Override
                            public void onSuccess(Long result) {
                                Log.i("KAKAO_API", "연결 끊기 성공. id: " + result);
                                settingViewLayout.closeDrawer(settingView);
                            }
                        });
            }
        });
        ArrayList<String> storedList = pref.get(this.getActivity(), today);
        if (storedList.size()!=0) {
            for (int i = 0; i < storedList.size(); i++) {
                schdules.add(new Schdule(storedList.get(i)));
            }
        }
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), schdules);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
