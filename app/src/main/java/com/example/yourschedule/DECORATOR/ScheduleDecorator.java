package com.example.yourschedule.DECORATOR;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.example.yourschedule.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class ScheduleDecorator  implements DayViewDecorator {

    Map<CalendarDay,String> Calandar;
    private CalendarDay date;
    private final Calendar calendar = Calendar.getInstance();
    Context context;
    public ScheduleDecorator(Date value,Context context) {
       date = CalendarDay.from(value);
       this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.NORMAL));
//        view.addSpan(new RelativeSizeSpan(1.4f));
//        view.addSpan(new ForegroundColorSpan(Color.parseColor("#6495ED")));
//        view.addSpan(new DotSpan(3, Color.parseColor("#C71585")));
        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_divide));

    }

}
