package com.example.yourschedule.FRAGMENT;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.yourschedule.DECORATOR.CustomTypeFaceSpan;
import com.example.yourschedule.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherForm extends Fragment {

    private ImageButton menuBt;
    private Fragment fragment;

    JsonObject jsonObject;
    double longitude;
    double latitude;

    public WeatherForm newInstance() {
        return new WeatherForm();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            longitude = getArguments().getDouble("longitude");
            latitude = getArguments().getDouble("latitude");

            jsonObject = new JsonParser().parse(getArguments().getString("jsonObject")).getAsJsonObject();
            Log.d("fsdgsfdg", jsonObject + "");
            Log.d("SFDgsdfg","sdfgsdfg");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather_form, container, false);
        menuBt = rootView.findViewById(R.id.menuBt);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragment = new TodayWeather().newInstance();
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        Gson gson = new Gson();
        String bundleJson = gson.toJson(jsonObject);
        bundle.putString("jsonObject", bundleJson);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.weather_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        menuBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getActivity(), view, Gravity.TOP);
                getActivity().getMenuInflater().inflate(R.menu.weather_menu, menu.getMenu());
                for (int i = 0; i < menu.getMenu().size(); i++) {
                    MenuItem menuItem = menu.getMenu().getItem(i);
                    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/baemin.ttf");
                    SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
                    mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    menuItem.setTitle(mNewTitle);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        Bundle bundle;
                        Gson gson;
                        String bundleJson;
                        switch (menuItem.getItemId()) {
                            case R.id.tomorrow:
                                fragment = new WeatherOfTomorrow().newInstance();
                                bundle = new Bundle();
                                bundle.putDouble("longitude", longitude);
                                bundle.putDouble("latitude", latitude);
                                gson = new Gson();
                                bundleJson = gson.toJson(jsonObject);
                                bundle.putString("jsonObject", bundleJson);
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.weather_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                            case R.id.today:
                                fragment = new TodayWeather().newInstance();
                                bundle = new Bundle();
                                bundle.putDouble("longitude", longitude);
                                bundle.putDouble("latitude", latitude);
                                gson = new Gson();
                                bundleJson = gson.toJson(jsonObject);
                                bundle.putString("jsonObject", bundleJson);
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.weather_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
    }
}
