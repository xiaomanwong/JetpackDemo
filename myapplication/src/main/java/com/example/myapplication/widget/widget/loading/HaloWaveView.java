package com.example.myapplication.widget.widget.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

/**
 * @author admin
 * @date 2021/11/3
 * @Desc
 */
public class HaloWaveView extends View {

    private final Paint mHaloPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mWidth;
    private float mHeight;
    private float leftMargin;
    private float rightMargin;
    private float dynamicPaddingValue;
    private float staticPaddingValue;
    private Rect textRect;
    private float textStart;
    private float textEnd;
    //    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String text = "跳转至第三方店铺文案";

    public HaloWaveView(Context context) {
        super(context);
        init();
    }

    public HaloWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HaloWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HaloWaveView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.text = text;
            measureText();
        }
    }


    private void measureText() {
        if (mTextPaint != null) {
            post(() -> {
                textRect = new Rect();
                mTextPaint.getTextBounds(text, 0, 0, textRect);
//                textStart = (mWidth - textRect.width()) / 2f;
//                textEnd = (mHeight + (textRect.height() - textRect.bottom)) / 2f;
                textStart = (mWidth / 2f - textRect.centerX()) / 2f;
                 textEnd = mHeight / 2f + textRect.height();
                System.out.println("width: " + textStart);
                System.out.println("width: " + textEnd);
                invalidate();
            });
        }
    }

    private void init() {
        mHaloPaint.setColor(Color.parseColor("#ff0000"));
        mBackPaint.setColor(Color.parseColor("#99000000"));
        mBorderPaint.setColor(Color.parseColor("#99ffffff"));
        mBorderPaint.setStrokeWidth(dp2px(0.5f));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.parseColor("#ff0000"));
        mTextPaint.setTextSize(dp2px(17f));
        measureText();
        leftMargin = dp2px(20f);
        rightMargin = dp2px(20f);
        dynamicPaddingValue = dp2px(12f);
        staticPaddingValue = dynamicPaddingValue;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = width;
        mHeight = dp2px(78f);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        haloAnimator();
    }

    private void haloAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(dynamicPaddingValue, 0, dynamicPaddingValue);
        valueAnimator.addUpdateListener(animation -> {
            dynamicPaddingValue = (float) animation.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setDuration(800);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.start();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(new RectF(leftMargin + dynamicPaddingValue, dynamicPaddingValue,
                        mWidth - rightMargin - dynamicPaddingValue, mHeight - dynamicPaddingValue),
                dp2px(40f), dp2px(40f), mHaloPaint);

        canvas.drawRoundRect(new RectF(leftMargin + staticPaddingValue - 2, staticPaddingValue - 2,
                        mWidth - rightMargin - staticPaddingValue + 2, mHeight - staticPaddingValue + 2),
                dp2px(40f), dp2px(40f), mBorderPaint);

        canvas.drawRoundRect(new RectF(leftMargin + staticPaddingValue, staticPaddingValue,
                        mWidth - rightMargin - staticPaddingValue, mHeight - staticPaddingValue),
                dp2px(40f), dp2px(40f), mBackPaint);
//        canvas.drawText(text, textStart, textEnd, mTextPaint);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private float dp2px(Float dpValue) {
        return (float) (getResources().getDisplayMetrics().widthPixels * dpValue / 375);
    }
}
