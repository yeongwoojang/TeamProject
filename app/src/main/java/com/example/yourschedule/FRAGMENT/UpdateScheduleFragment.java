package com.example.yourschedule.FRAGMENT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.yourschedule.R;

public class UpdateScheduleFragment extends DialogFragment {
    public static final String TAG_EVENT_DIALOG = "dialog_event";
    onMyUpdateDialogResult onMyUpdateDialogResult;
    Button yesBt,noBt;
    public static UpdateScheduleFragment newInstance(){
        return new UpdateScheduleFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_schedule,container,false);
        yesBt = view.findViewById(R.id.yesBt);
        noBt = view.findViewById(R.id.noBt);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        yesBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMyUpdateDialogResult.showPopupFragment();
                onMyUpdateDialogResult.finish();
            }
        });

        noBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMyUpdateDialogResult.finish();
            }
        });
    }

    public interface onMyUpdateDialogResult{
        void finish();
        void showPopupFragment();
    }
    public void setDialogResult(onMyUpdateDialogResult onMyUpdateDialogResult){
        this.onMyUpdateDialogResult = onMyUpdateDialogResult;
    }
}
