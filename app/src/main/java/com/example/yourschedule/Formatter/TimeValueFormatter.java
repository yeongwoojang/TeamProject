package com.example.yourschedule.Formatter;

import android.util.Log;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TimeValueFormatter extends IndexAxisValueFormatter {

    List<Integer> mValues;

    public TimeValueFormatter(List<Integer> mValues) {
        this.mValues = mValues;
    }



    @Override
    public String getFormattedValue(float value) {
        String label = "";
        int val = (int)value;
            label = mValues.get(val)+"시";
        return label;
    }

}
