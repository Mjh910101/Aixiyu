package com.cn.ispanish.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.interfaces.CallbackForBoolean;


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
 * Created by Hua on 17/8/28.
 */
public class TimeTextView extends TextView implements Runnable {

    Paint mPaint; //画笔,包含了画几何图形、文本等的样式和颜色信息

    private long[] times;

    private long mday, mhour, mmin, msecond;//天，小时，分钟，秒

    private boolean run = false; //是否启动了

    private CallbackForBoolean endCallback;

    public TimeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);

        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public TimeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mPaint = new Paint();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TimeTextView);

        array.recycle(); //一定要调用，否则这次的设定会对下次的使用造成影响
    }

    public TimeTextView(Context context) {
        super(context);
    }

    public long[] getTimes() {
        return times;
    }

    public void setTimes(long[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];

    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 59;
                    mday--;
                }
            }
        }
        initTime();
    }

    private void initTime() {
        boolean isEnd = true;
        if (mday < 0) {
            mday = 0;
        }
        if (mhour < 0) {
            mhour = 0;
        }
        if (mmin < 0) {
            mmin = 0;
        }
        if (msecond < 0) {
            msecond = 0;
        }

        if (endCallback != null) {
            endCallback.callback(mday == 0 && mhour == 0 && mmin == 0 && msecond == 0);
        }
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        //标示已经启动
        run = true;
        ComputeTime();

        String strTime = "<font color=\"#FF0000\">" + mday + "</font> <pre>" + "天</pre>" +
                "<font color=\"#FF0000\">" + mhour + "</font> <pre>小时</pre> " +
                "<font color=\"#FF0000\">" + mmin + "</font> <pre>分钟</pre>" +
                "<font color=\"#FF0000\">" + msecond + "</font> <pre>秒</pre>";
        this.setText(Html.fromHtml(strTime));

        postDelayed(this, 1000);

    }

    public void setEndCallback(CallbackForBoolean endCallback) {
        this.endCallback = endCallback;
    }

}
