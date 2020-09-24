package com.example.yourschedule.FRAGMENT;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
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

public class RateFormFragment extends Fragment {

    public RateFormFragment newInstance() {
        return new RateFormFragment();
    }

    private ImageButton menuBt;
    private Fragment fragment =null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rate_form, container, false);
        menuBt = rootView.findViewById(R.id.menuBt);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = this;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragment = new MonthAchievementRate().newInstance();
        fragmentTransaction.replace(R.id.rate_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();

        menuBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(getActivity(),view, Gravity.TOP);
                getActivity().getMenuInflater().inflate(R.menu.rate_menu,menu.getMenu());

                for (int i = 0; i < menu.getMenu().size(); i++) {
                    MenuItem menuItem = menu.getMenu().getItem(i);
                    Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/bm_dohyeon_ttf.ttf");
                    SpannableString mNewTitle = new SpannableString(menuItem.getTitle());
                    mNewTitle.setSpan(new CustomTypeFaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                    menuItem.setTitle(mNewTitle);
                }
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        switch (menuItem.getItemId()){
                            case R.id.monthRate :
                                fragment = new MonthAchievementRate().newInstance();
                                fragmentTransaction.replace(R.id.rate_container, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commitAllowingStateLoss();
                                break;
                            case R.id.weekRate :
                                fragment = new WeekAchievementRate().newInstance();
                                fragmentTransaction.replace(R.id.rate_container, fragment);
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
