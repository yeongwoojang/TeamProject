package com.example.yourschedule.DECORATOR;

import android.content.Context;
import android.graphics.Color;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import androidx.core.content.ContextCompat;

import com.example.yourschedule.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class SunDayDecorator implements DayViewDecorator {

    private final Calendar calendar = Calendar.getInstance();
    Context context;
    public SunDayDecorator(Context context){
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            return weekDay == Calendar.SUNDAY;


    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.red300)));
        view.addSpan(new RelativeSizeSpan(1.1f));
    }
}
