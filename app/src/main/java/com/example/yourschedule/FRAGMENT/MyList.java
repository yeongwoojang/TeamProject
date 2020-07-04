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
import com.example.yourschedule.OBJECT.ScheduleDTO;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
import com.example.yourschedule.SharePref;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyList extends Fragment {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Schdule> schdules = new ArrayList<>();
    List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    ImageButton settingBt,closeSettingBt;
    TextView dayOfWeek,dateText,logoutBt,shareBt;
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
        shareBt = rootView.findViewById(R.id.shareBt);
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
        try{
            mDatabase.getReference("일정").child(auth.getCurrentUser().getDisplayName())
//                .child(today.replace(".","-"))
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                ScheduleDTO scheduleDTO = snapshot.getValue(ScheduleDTO.class);
                                Log.d("Firebase",snapshot.getValue()+"");
                                scheduleDTOS.add(scheduleDTO);
                                Log.d("Firebase", scheduleDTOS.size()+"");
                            }
                            recyclerViewAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }catch (Exception e){

        }


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

        //여기서 일단 오늘 일정 공유테스트 ㄱㄱ
        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedTemplate params = FeedTemplate
                        .newBuilder(ContentObject.newBuilder("일정이 도착했습니다.",
                                "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
                                LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                        .setMobileWebUrl("https://developers.kakao.com").build())
                                .build())
                        .setSocial(SocialObject.newBuilder().setLikeCount(10).setCommentCount(20)
                                .setSharedCount(30).setViewCount(40).build())
                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
                                .setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com")
                                .setAndroidExecutionParams("key1=value1")
                                .setIosExecutionParams("key1=value1")
                                .build()))
                        .build();
                Map<String, String> serverCallbackArgs = new HashMap<String, String>();
                serverCallbackArgs.put("user_id", auth+"");

                KakaoLinkService.getInstance().sendDefault(getActivity(), params, serverCallbackArgs, new ResponseCallback<KakaoLinkResponse>() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.d("TEST","FAILED");
                    }

                    @Override
                    public void onSuccess(KakaoLinkResponse result) {
                        // 템플릿 밸리데이션과 쿼터 체크가 성공적으로 끝남. 톡에서 정상적으로 보내졌는지 보장은 할 수 없다. 전송 성공 유무는 서버콜백 기능을 이용하여야 한다.
                        Log.d("TEST","SUCCESS");
                    }
                });
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

        ArrayList<String> storedList = pref.get(this.getActivity(), today);
        if (storedList.size()!=0) {
            for (int i = 0; i < storedList.size(); i++) {
                schdules.add(new Schdule(storedList.get(i)));
            }
        }
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), scheduleDTOS,today);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

}
