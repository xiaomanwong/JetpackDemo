package com.example.myapplication.widget.widget.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


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
    private float mArrowWidth;
    private float mArrowHeight;
    private float tempEnterX;
    private float tempEnterY;
    private float mWidth;
    private float mHeight;
    private float leftMargin;
    private float rightMargin;
    private float dynamicPaddingValue;
    private float staticPaddingValue;
    private Path mEnterPath1;
    private Path mEnterPath2;
    private Path mEnterPath3;
    private Paint mEnterPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mEnterPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mEnterPaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);

    private RectF RoundRect1;
    private RectF RoundRect2;
    private RectF RoundRect3;

    private int mHaloPaintAlpha = 26;
    private int mHaloPaintAlpha2 = 255;
    private float offset;

    private float gap;
    private ValueAnimator mHaloValueAnimator;
    private ValueAnimator mHaloPaintAlphaAnimator;
    private ValueAnimator moveAnimator;

    private String text = "点击跳转详情或第三方应用";
    private float textStart;
    private float textEnd;
    private Rect textRect;


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


    private void init() {
        mHaloPaint.setColor(Color.parseColor("#19ffffff"));
        mEnterPaint1.setColor(Color.parseColor("#ffffff"));
        mEnterPaint2.setColor(Color.parseColor("#ffffff"));
        mEnterPaint3.setColor(Color.parseColor("#ffffff"));
        mHaloPaint.setAlpha(mHaloPaintAlpha);
        mBackPaint.setColor(Color.parseColor("#99000000"));
        mBorderPaint.setColor(Color.parseColor("#1Affffff"));
        mBorderPaint.setStrokeWidth(dp2px(0.5f));
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setColor(Color.parseColor("#ff0000"));
        mTextPaint.setTextSize(dp2px(17f));
        leftMargin = dp2px(20f);
        rightMargin = dp2px(20f);
        dynamicPaddingValue = dp2px(12f);
        mArrowWidth = dp2px(8f);
        mArrowHeight = dp2px(10f);
        staticPaddingValue = dynamicPaddingValue;
        mEnterPath1 = new Path();
        mEnterPath2 = new Path();
        mEnterPath3 = new Path();
        measureText();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = width;
//        mHeight = dp2px(78f);
        mHeight =height;

        tempEnterX = mWidth - mArrowWidth * 3 * 2 - leftMargin - dp2px(6f);
        tempEnterY = (mHeight - mArrowHeight) / 2;
        gap = mArrowWidth - dp2px(3f);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

//        mWidth = w;
////        mHeight = dp2px(78f);
//        mHeight =h;
//
//        tempEnterX = mWidth - mArrowWidth * 3 * 2 - leftMargin;
//        tempEnterY = (mHeight - mArrowHeight) / 2;
//        gap = mArrowWidth - dp2px(3f);
//        setMeasuredDimension(w, h);
        RoundRect1 = new RectF(leftMargin + dynamicPaddingValue, dynamicPaddingValue,
                mWidth - rightMargin - dynamicPaddingValue, mHeight - dynamicPaddingValue);

        RoundRect2 = new RectF(leftMargin + staticPaddingValue - 2, staticPaddingValue - 2,
                mWidth - rightMargin - staticPaddingValue + 2, mHeight - staticPaddingValue + 2);

        RoundRect3 = new RectF(leftMargin + staticPaddingValue, staticPaddingValue,
                mWidth - rightMargin - staticPaddingValue, mHeight - staticPaddingValue);
        haloAnimator();
        super.onSizeChanged(w, h, oldw, oldh);
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
                float width = mTextPaint.measureText(text);
//                mTextPaint.measureText()
//                textStart = (mWidth - textRect.width()) / 2f;
//                textEnd = (mHeight + (textRect.height() - textRect.bottom)) / 2f;
                textStart = mWidth / 2f - width / 2f;
                Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
                float height = fontMetrics.bottom - fontMetrics.top;
                textEnd = mHeight / 2f + height /4f;
                invalidate();
            });
        }
    }

    private void haloAnimator() {
        //
        mHaloValueAnimator = ValueAnimator.ofFloat(dynamicPaddingValue, 0);
        mHaloValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                dynamicPaddingValue = (float) animation.getAnimatedValue();
                RoundRect1.set(leftMargin + dynamicPaddingValue, dynamicPaddingValue,
                        mWidth - rightMargin - dynamicPaddingValue, mHeight - dynamicPaddingValue);
                RoundRect2.set(leftMargin + staticPaddingValue - 2, staticPaddingValue - 2,
                        mWidth - rightMargin - staticPaddingValue + 2, mHeight - staticPaddingValue + 2);
                RoundRect3.set(leftMargin + staticPaddingValue, staticPaddingValue,
                        mWidth - rightMargin - staticPaddingValue, mHeight - staticPaddingValue);
                HaloWaveView.this.invalidate();
            }
        });
        mHaloValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mHaloValueAnimator.setDuration(1500);
        mHaloValueAnimator.setRepeatCount(-1);
        mHaloValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mHaloValueAnimator.start();

        mHaloPaintAlphaAnimator = ValueAnimator.ofInt(mHaloPaintAlpha, 0);
        mHaloPaintAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mHaloPaint.setAlpha((int) animation.getAnimatedValue());
                HaloWaveView.this.invalidate();
            }
        });
        mHaloPaintAlphaAnimator.setDuration(1500);
        mHaloPaintAlphaAnimator.setRepeatCount(-1);
        mHaloPaintAlphaAnimator.setRepeatMode(ValueAnimator.RESTART);
        mHaloPaintAlphaAnimator.start();


        moveAnimator = ValueAnimator.ofFloat(0, gap * 5 + mArrowWidth);
        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();
                HaloWaveView.this.invalidate();
            }
        });
        moveAnimator.setDuration(3000);
        moveAnimator.setRepeatCount(-1);
        moveAnimator.setRepeatMode(ValueAnimator.RESTART);
        moveAnimator.start();

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHaloValueAnimator != null) {
            mHaloValueAnimator.cancel();
        }
        if (mHaloPaintAlphaAnimator != null) {
            mHaloPaintAlphaAnimator.cancel();
        }
        if (moveAnimator != null) {
            moveAnimator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(RoundRect1, dp2px(40f), dp2px(40f), mHaloPaint);

        canvas.drawRoundRect(RoundRect2, dp2px(40f), dp2px(40f), mBorderPaint);

        canvas.drawRoundRect(RoundRect3, dp2px(40f), dp2px(40f), mBackPaint);

        if (offset < 2 * gap + mArrowWidth) {
            mEnterPath1.reset();
            float percent = offset / (2 * gap + mArrowWidth);
            mEnterPaint1.setAlpha(getEnterPaintAlpha(percent));
            mEnterPath1.moveTo(tempEnterX + offset, tempEnterY);
            mEnterPath1.lineTo(tempEnterX + offset + mArrowWidth, tempEnterY + mArrowHeight / 2);
            mEnterPath1.lineTo(tempEnterX + offset, tempEnterY + mArrowHeight);
            mEnterPath1.lineTo(tempEnterX + offset, tempEnterY);
            canvas.drawPath(mEnterPath1, mEnterPaint1);
        }

        if (offset > gap && offset < 3 * gap + mArrowWidth) {
            mEnterPath2.reset();
            float percent = ((offset - gap) / (2 * gap + mArrowWidth));
            mEnterPaint2.setAlpha(getEnterPaintAlpha(percent));
            mEnterPath2.moveTo(tempEnterX + offset - gap, tempEnterY);
            mEnterPath2.lineTo(tempEnterX + offset - gap + mArrowWidth, tempEnterY + mArrowHeight / 2);
            mEnterPath2.lineTo(tempEnterX + offset - gap, tempEnterY + mArrowHeight);
            mEnterPath2.lineTo(tempEnterX + offset - gap, tempEnterY);
            canvas.drawPath(mEnterPath2, mEnterPaint2);
        }

        if (offset > gap * 2 && offset < 4 * gap + mArrowWidth) {
            mEnterPath3.reset();
            float percent = ((offset - gap * 2) / (2 * gap + mArrowWidth));
            mEnterPaint3.setAlpha(getEnterPaintAlpha(percent));
            mEnterPath3.moveTo(tempEnterX + offset - gap * 2, tempEnterY);
            mEnterPath3.lineTo(tempEnterX + offset - gap * 2 + mArrowWidth, tempEnterY + mArrowHeight / 2);
            mEnterPath3.lineTo(tempEnterX + offset - gap * 2, tempEnterY + mArrowHeight);
            mEnterPath3.lineTo(tempEnterX + offset - gap * 2, tempEnterY);
            canvas.drawPath(mEnterPath3, mEnterPaint3);
        }


        canvas.drawText(text, textStart, textEnd, mTextPaint);
    }

    /**
     * 获取箭头的透明度
     *
     * @param percent
     * @return
     */
    private int getEnterPaintAlpha(float percent) {
        float v = 255f;
        if (percent < 0.2f) {
            v = 255f * 2.5f * percent;
        } else if (percent > 0.4f) {
            v = 255f * 2.5f * (1 - percent);
        }
        return (int) v;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private float dp2px(Float dpValue) {
        return getResources().getDisplayMetrics().widthPixels * dpValue / 375;
    }
}
