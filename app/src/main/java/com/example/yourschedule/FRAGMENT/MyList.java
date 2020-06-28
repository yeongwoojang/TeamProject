package com.example.yourschedule.FRAGMENT;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.yourschedule.OBJECT.Schdule;
import com.example.yourschedule.R;
import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
import com.example.yourschedule.SharePref;
import com.kakao.auth.Session;

import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;


public class MyList extends Fragment {

    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    List<Schdule> schdules = new ArrayList<>();
    ImageButton shareBt;
    TextView dayOfWeek,dateText;
    String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
    SharePref pref = new SharePref();

    public MyList newInstance() {
        return new MyList();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        shareBt = rootView.findViewById(R.id.shareBt);
        dayOfWeek = rootView.findViewById(R.id.dayOfWeek);
        dateText = rootView.findViewById(R.id.date);
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
        String selectedDate = transFormat.format(calendar.getTime());
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)-1];
        dayOfWeek.setText(day);
        dateText.setText(selectedDate);

        shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TemplateParams params = FeedTemplate
//                        .newBuilder(ContentObject.newBuilder(
//                                "디저트 사진",
//                                "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg",
//                                LinkObject.newBuilder()
//                                        .setWebUrl("https://developers.kakao.com")
//                                        .setMobileWebUrl("https://developers.kakao.com")
//                                        .build())
//                                .setDescrption("일정 공유가 도착했습니다.")
//                                .build())
//                        .addButton(new ButtonObject(
//                                "앱에서 보기",
//                                LinkObject.newBuilder()
//                                        .setMobileWebUrl("'https://www.naver.com'")
//                                        .setAndroidExecutionParams("value=key")
//                                        .build()))
//                        .build();
//                KakaoLinkService.getInstance()
//                        .sendDefault(getActivity(), params, new ResponseCallback<KakaoLinkResponse>() {
//                            @Override
//                            public void onFailure(ErrorResult errorResult) {
//                                Log.e("KAKAO_API", "카카오링크 공유 실패: " + errorResult);
//                            }
//
//                            @Override
//                            public void onSuccess(KakaoLinkResponse result) {
//                                Log.i("KAKAO_API", "카카오링크 공유 성공");
//
//                                // 카카오링크 보내기에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
//                                Log.w("KAKAO_API", "warning messages: " + result.getWarningMsg());
//                                Log.w("KAKAO_API", "argument messages: " + result.getArgumentMsg());
//                            }
//                        });
            }
        });
        ArrayList<String> storedList = pref.get(this.getActivity(), selectedDate);
        if (storedList.size()!=0) {
            for (int i = 0; i < storedList.size(); i++) {
                schdules.add(new Schdule(storedList.get(i)));
            }
        }
        recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), schdules);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

}
