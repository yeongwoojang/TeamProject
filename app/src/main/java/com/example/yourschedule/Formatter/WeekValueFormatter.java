package com.example.yourschedule.Formatter;

import android.util.Log;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WeekValueFormatter extends IndexAxisValueFormatter {

    ArrayList<String> mValues;

    public WeekValueFormatter(ArrayList<String> mValues) {
        this.mValues = mValues;
    }

    @Override
    public String getFormattedValue(float value) {
        //value = 0f,10f,20f,30f,40f,50f
        int val = (int)(value/10f);
        String label = "";
        if (val >= 0 && val < mValues.size()) {
            label = mValues.get(val)+"주차";
        } else {
            label = "";
        }
        return label;
    }

    @Override
    public String[] getValues() {
        return super.getValues();
    }

    @Override
    public void setValues(String[] values) {
        super.setValues(values);
    }
}
