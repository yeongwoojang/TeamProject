package com.example.yourschedule;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yourschedule.FRAGMENT.MyList;
import com.example.yourschedule.FRAGMENT.ScheduleList;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN =10;
//    private SessionCallback sessionCallback = new SessionCallback();
    //유저 프로필
    private final int FRAGMENT1 = 0;
    private final int FRAGMENT2 = 1;
    private final int FRAGMENT3 = 2;
    private final String[] bottomTab = {"일정관리", "추가예정", "추가예정"};
    private TabLayout bottom_tabs;
    private TextView t1;
    private SignInButton loginBt;
    private Context mContext = null;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, LoadingActivity.class));
        setContentView(R.layout.activity_main);

        mContext = this;
        t1 = (TextView)findViewById(R.id.test);
        bottom_tabs = (TabLayout) findViewById(R.id.bottom_tabs);
        loginBt = (SignInButton) findViewById(R.id.login_button);
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"@font/baemin.ttf");
        for (int i = 0; i < bottomTab.length; i++) {
            bottom_tabs.addTab(bottom_tabs.newTab());
            TextView view = new TextView(this);
            view.setGravity(bottom_tabs.GRAVITY_CENTER);
//            view.setTypeface(Typeface.createFromAsset(getAssets(), "font/myfont.ttf"));
            view.setTextColor(Color.parseColor("#ffffff"));
            view.setText(bottomTab[i]);
            bottom_tabs.getTabAt(i).setCustomView(view);
        }
        bottom_tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        bottom_tabs.getTabAt(FRAGMENT1).setTag(FRAGMENT1);
        bottom_tabs.getTabAt(FRAGMENT2).setTag(FRAGMENT2);
        bottom_tabs.getTabAt(FRAGMENT3).setTag(FRAGMENT3);

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        Log.d("test",mAuth.getCurrentUser()+"");
            if(mAuth.getCurrentUser()!=null){
                loginBt.setVisibility(View.INVISIBLE);
                bottom_tabs.setVisibility(View.VISIBLE);
                callFragment(FRAGMENT1);
                Log.d("test",mAuth.getCurrentUser().getDisplayName());
                String testData = new String();
                testData = (String)intent.getDataString();
                if(testData!=null){
                Log.d("test",testData);
                }else{
                    Log.d("test","공유받은 데이터가 없습니다.");
                }
            }else{
                loginBt.setVisibility(View.VISIBLE);
                bottom_tabs.setVisibility(View.INVISIBLE);
                Toast.makeText(this,"로그인해서 일정을 확인하세요!",Toast.LENGTH_SHORT).show();
            }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });


        bottom_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (Integer.parseInt(String.valueOf(tab.getTag()))) {
                    case FRAGMENT1:
                        // '버튼1' 클릭 시 '프래그먼트1' 호출
                        callFragment(FRAGMENT1);
                        break;

                    case FRAGMENT2:
                        // '버튼2' 클릭 시 '프래그먼트2' 호출
                        callFragment(FRAGMENT2);
                        break;

                    case FRAGMENT3:
                        callFragment(FRAGMENT3);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
//        callFragment(FRAGMENT1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent]
        // [END_EXCLUDE]
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d("Login",user.getDisplayName()+"Login, Success!");
                            loginBt.setVisibility(View.INVISIBLE);
                            bottom_tabs.setVisibility(View.VISIBLE);
                            callFragment(FRAGMENT1);

                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });
    }

    private void callFragment(int frament_no) {

        // 프래그먼트 사용을 위해
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (frament_no) {
            default:

            case FRAGMENT1:
                // '프래그먼트1' 호출
                ScheduleList scheduleList = new ScheduleList();
                transaction.replace(R.id.fragment_container, scheduleList);
                transaction.commit();
                break;

            case FRAGMENT2:
                // '프래그먼트2' 호출
                MyList graph = new MyList();
                transaction.replace(R.id.fragment_container, graph);
                transaction.commit();
                break;
        }
    }




}