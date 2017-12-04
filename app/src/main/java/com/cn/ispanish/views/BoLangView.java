package com.cn.ispanish.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.ColorHandle;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 2017/10/31.
 */

public class BoLangView extends View implements View.OnClickListener {

    private int mWaveLength;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mCenterY;
    private int mWaveCount;
    private int offset, middleOffset, lastOffset;

    private Path mPath, middlePath, lastPath;
    private Paint mPaint, middlePaint, lastPaint;


    private ValueAnimator mValueAnimatior;

    public BoLangView(Context context) {
        super(context);
    }

    public BoLangView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(8);
        mPaint.setColor(ColorHandle.getColorForID(context, R.color.bleu_36));

        middlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middlePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        middlePaint.setStrokeWidth(8);
        middlePaint.setColor(ColorHandle.getColorForID(context, R.color.jordy_blue));

        lastPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        lastPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lastPaint.setStrokeWidth(8);
        lastPaint.setColor(ColorHandle.getColorForID(context, R.color.pale_cornflower_blue));

        mWaveLength = 1200;

    }

    public BoLangView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 需要计算出屏幕能容纳多少个波形
        mPath = new Path();
        middlePath = new Path();
        lastPath = new Path();
        mScreenHeight = h;
        mScreenWidth = w;
        mCenterY = h / 2;
        mWaveCount = (int) Math.round(mScreenWidth / mWaveLength + 1.5); // 计算波形的个数
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();
        mPath.moveTo(-mWaveLength, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            mPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + offset, mCenterY + 60, -mWaveLength / 2 + i * mWaveLength + offset, mCenterY);
            mPath.quadTo(-mWaveLength / 4 + i * mWaveLength + offset, mCenterY - 60, i * mWaveLength + offset, mCenterY);
        }
        mPath.lineTo(mScreenWidth, mScreenHeight);
        mPath.lineTo(0, mScreenHeight);
        mPath.close();

        middlePath.reset();
        middlePath.moveTo(-mWaveLength, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            middlePath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + middleOffset, mCenterY + 60, -mWaveLength / 2 + i * mWaveLength + middleOffset, mCenterY);
            middlePath.quadTo(-mWaveLength / 4 + i * mWaveLength + middleOffset, mCenterY - 60, i * mWaveLength + middleOffset, mCenterY);
        }
        middlePath.lineTo(mScreenWidth, mScreenHeight);
        middlePath.lineTo(0, mScreenHeight);
        middlePath.close();

        lastPath.reset();
        lastPath.moveTo(-mWaveLength, mCenterY);
        for (int i = 0; i < mWaveCount; i++) {
            lastPath.quadTo(-mWaveLength * 3 / 4 + i * mWaveLength + lastOffset, mCenterY + 60, -mWaveLength / 2 + i * mWaveLength + lastOffset, mCenterY);
            lastPath.quadTo(-mWaveLength / 4 + i * mWaveLength + lastOffset, mCenterY - 60, i * mWaveLength + lastOffset, mCenterY);
        }
        lastPath.lineTo(mScreenWidth, mScreenHeight);
        lastPath.lineTo(0, mScreenHeight);
        lastPath.close();

        canvas.drawPath(lastPath, lastPaint);
        canvas.drawPath(middlePath, middlePaint);
        canvas.drawPath(mPath, mPaint);

    }


    @Override
    public void onClick(View v) {
        statrBolang();
    }

    public void statrBolang() {
        mValueAnimatior = ValueAnimator.ofInt(0, mWaveLength);
        mValueAnimatior.setDuration(1200);
        mValueAnimatior.setInterpolator(new LinearInterpolator());
        mValueAnimatior.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatior.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                middleOffset = offset - 250;
                lastOffset = middleOffset - 250;
                invalidate();
            }
        });
        mValueAnimatior.start();
    }


}
