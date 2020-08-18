package com.example.yourschedule.DECORATOR;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.example.yourschedule.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

public class TodayDecorator implements DayViewDecorator {

    private CalendarDay date;
    Context context;
    public TodayDecorator(Context context) {
        date = CalendarDay.today();
        this.context = context;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null &&day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.NORMAL));
        view.addSpan(new RelativeSizeSpan(1.2f));
        view.addSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.gray700)));

    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);

    }
}
