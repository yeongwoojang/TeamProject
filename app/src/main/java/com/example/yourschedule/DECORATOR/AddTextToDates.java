package com.example.yourschedule.DECORATOR;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.yourschedule.R;

public class AddTextToDates implements LineBackgroundSpan {

    private String dayText;
    private Context context;


    public AddTextToDates(Context context, String text) {
        this.dayText = text;
        this.context = context;
    }

    @Override
    public void drawBackground(@NonNull Canvas canvas,
                               @NonNull Paint paint,
                               int left,
                               int right,
                               int top,
                               int baseline,
                               int bottom,
                               @NonNull CharSequence text,
                               int start,
                               int end,
                               int lnum) {
        int textColor = ContextCompat.getColor(context, R.color.red200);
        int backgoudColor = ContextCompat.getColor(context, R.color.red200);
        Paint mPaint = new Paint();
        mPaint.setColor(textColor);
        mPaint.setTextSize(20);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setStrokeWidth(20);

        canvas.drawText(dayText, 0, (float) (bottom)+15, mPaint);
    }
}
