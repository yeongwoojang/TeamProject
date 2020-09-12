package com.example.yourschedule.Formatter;

import android.util.Log;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.Collection;

public class TempValueFormatter extends ValueFormatter {


    @Override
    public String getFormattedValue(float value) {

        return value+"℃";

    }


}
