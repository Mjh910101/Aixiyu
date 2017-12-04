package com.cn.ispanish.views;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;

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
 * Created by Hua on 17/8/18.
 */
public class FloatListView extends ListView {

    public FloatListView(Context context) {
        super(context);
        init();
    }

    public FloatListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public FloatListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

    }

    private int lastY;
    private int dowmY;
    private int canScrollDistance;


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (getChildAt(0) == null) {
            return super.onTouchEvent(ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            lastY = (int) ev.getRawY();
            dowmY = (int) ev.getRawY();
            canScrollDistance = ((ViewGroup) getParent()).getChildAt(0).getHeight();
            Log.e("distance", "Distance : " + canScrollDistance);
            return super.onTouchEvent(ev);
        }

        if (ev.getAction() == MotionEvent.ACTION_UP) {
            int y = (int) ev.getRawY();
            canScrollDistance = ((ViewGroup) getParent()).getChildAt(0).getHeight();
            Log.e("distance", "Distance : " + canScrollDistance);
            Log.e("distance", "dowmY : " + dowmY + "upY : " + y);
            if (Math.abs(y - dowmY) < 10) {
                return super.onTouchEvent(ev);
            }

        }

        int currentY = (int) ev.getRawY();
        Log.e("distance", "currentY : " + currentY + " , lastY : " + lastY);
        if (currentY < lastY) {
            View view = ((ViewGroup) getParent());
            if (view.getScrollY() < canScrollDistance) {
                view.scrollBy(0, (int) (lastY - currentY));
                if (view.getScrollY() > canScrollDistance) {
                    view.scrollTo(0, canScrollDistance);
                }
                lastY = currentY;
                return true;
            }
        } else if (getFirstVisiblePosition() == 0) {
            Log.e("distance", "last***************** ");
            if (getChildAt(0).getTop() == 0) {
                Log.e("distance", "last and top == 0 ");
                View view = ((ViewGroup) getParent());
                if (view.getScrollY() > 0) {
                    view.scrollBy(0, (int) (lastY - currentY));
                    Log.e("distance", "scrollBy : 0 ," + (lastY - currentY));
                    lastY = currentY;
                    if (view.getScrollY() < 0) {
                        view.scrollTo(0, 0);
                    }
                    return true;
                }
            }

        }
        lastY = (int) ev.getRawY();
        Log.e("distance", "lastY : " + lastY);
        return super.onTouchEvent(ev);
    }

}
