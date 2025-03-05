package com.example.myapplication.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

public class RoundedBackgroundSpan extends ReplacementSpan {

    private int backgroundColor;
    private int textColor;
    private int padding;

    public RoundedBackgroundSpan(int backgroundColor, int textColor, int padding) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.padding = padding;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return (int) (padding * 2 + paint.measureText(text.subSequence(start, end).toString()));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        String spanText = text.subSequence(start, end).toString();
        
        // 设置背景颜色
        paint.setColor(backgroundColor);
        float width = paint.measureText(spanText);
        RectF rect = new RectF(x, top + padding, x + width + 2 * padding, bottom - padding);
        canvas.drawRoundRect(rect, 20, 20, paint); // 20 表示圆角半径

        // 设置文本颜色
        paint.setColor(textColor);
        canvas.drawText(spanText, x + padding, y, paint);
    }
}