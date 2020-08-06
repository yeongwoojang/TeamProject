package com.example.yourschedule.Formatter;

import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class YValueFormatter extends ValueFormatter {

    public DecimalFormat mFormat;
    private PieChart pieChart;

    public YValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    public YValueFormatter(PieChart pieChart) {
        this();
        this.pieChart = pieChart;
    }


    @Override
    public String getFormattedValue(float value) {
        Log.d("valueY",value+"");
        if(value==0f){
            return "";
        }else{
            return mFormat.format(value) + " %";
        }
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {
        if (pieChart != null && pieChart.isUsePercentValuesEnabled()) {
            return getFormattedValue(value);
        } else {
            return mFormat.format(value);
        }
    }
}
