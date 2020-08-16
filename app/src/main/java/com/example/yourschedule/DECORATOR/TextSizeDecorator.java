package com.example.yourschedule.DECORATOR;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class TextSizeDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
//    private CalendarDay date;

    public TextSizeDecorator() {
//        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);

        return weekDay != Calendar.SUNDAY && weekDay != Calendar.SATURDAY && !day.equals(CalendarDay.today());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.NORMAL));
        view.addSpan(new RelativeSizeSpan(1.1f));
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")));
    }
}
