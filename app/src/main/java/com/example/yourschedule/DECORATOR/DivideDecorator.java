package com.example.yourschedule.DECORATOR;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.yourschedule.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;

public class DivideDecorator implements DayViewDecorator {
    private final Calendar calendar = Calendar.getInstance();
        private CalendarDay date;
    Context context;

    public DivideDecorator(Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        day.copyTo(calendar);
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        return weekDay==Calendar.SUNDAY||weekDay==Calendar.SATURDAY||
                weekDay==Calendar.FRIDAY||weekDay==Calendar.THURSDAY||
                weekDay==Calendar.WEDNESDAY||weekDay==Calendar.TUESDAY||
                weekDay==Calendar.MONDAY;
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.calendar_divide));

    }
}
