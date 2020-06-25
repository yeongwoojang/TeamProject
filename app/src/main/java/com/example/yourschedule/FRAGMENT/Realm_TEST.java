package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourschedule.ADAPTER.RecyclerViewAdapter;
import com.example.yourschedule.R;
import com.example.yourschedule.REALM.Member;
import com.example.yourschedule.SharePref;

import io.realm.Realm;

class Realm_TEST extends Fragment {
    public final String PREFERENCE = "com.example.yourschedule.FRAGMENT";
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;
    SharePref pref = new SharePref();

    public Realm_TEST newInstance() {
        return new Realm_TEST();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.realm_test, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        Button button1 = rootView.findViewById(R.id.button1);
        Button button2 = rootView.findViewById(R.id.button2);
        Button button3 = rootView.findViewById(R.id.button3);
        Realm realm = Realm.getDefaultInstance();

        Member member = new Member();
        member.setName("st");
        member.setAge(2);



        return rootView;
    }
}
