package com.example.yourschedule.DECORATOR;
import android.content.Context;
import android.util.Log;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Calendar;
import java.util.Date;

public class DaysTextDecorator implements DayViewDecorator {

    String dayText;
    Context context;
    private final Calendar calendar = Calendar.getInstance();
    private CalendarDay date;
    public DaysTextDecorator(Context context,Date value,String dayText) {
        date = CalendarDay.from(value);
        this.dayText = dayText;
        this.context = context;
    }



    @Override
    public boolean shouldDecorate(CalendarDay day) {
        Log.d("day",day+"");
        day.copyTo(calendar);
        return day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new AddTextToDates(context,dayText));
//        view.setDaysDisabled(true);
    }
}
