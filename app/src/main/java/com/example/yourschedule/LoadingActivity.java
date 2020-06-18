package com.example.yourschedule;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//주석추가
//ywBranchTest
        Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                finish();

                overridePendingTransition(R.anim.fadein,R.anim.fadeout);
            }
        };

        handler.sendEmptyMessageDelayed(0,2000);

    }

    public void onBackPressed(){}

}