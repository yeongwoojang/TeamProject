package com.example.yourschedule.ACTIVITY;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.example.yourschedule.R;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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