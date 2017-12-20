package com.cn.ispanish.views;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

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
 * Created by Hua on 2017/12/6.
 */

public class LineLayout extends View {

//    String pointText[] = { "一", "二", "三", "四" };
//    String reaAnswer[] = { "13", "24" };

    List<MyLine> list;
    List<Point> beginList;
    private int selectLine;// 当前画的第几条线
    private String mTitleText = "123";
    private int mTitleTextColor;
    private int mTitleTextSize;

    Paint linePaint = new Paint();
    private float width, height;

    private Rect mBound;
    private Paint mPaint;

    private int pointSize = 0;

    private Context context;

    public LineLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineLayout(Context context) {
        this(context, null);
    }

    /**
     * 获得我自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public LineLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        this.context = context;
        /**
         * 获得我们所定义的自定义样式属性
         */
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.paper_line, defStyle, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.paper_line_titleText:
                    mTitleText = a.getString(attr);
                    break;
                case R.styleable.paper_line_titleTextColor:
                    // 默认颜色设置为黑色
                    mTitleTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.paper_line_titleTextSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,
                                    getResources().getDisplayMetrics()));
                    break;

            }

        }
        a.recycle();

        initData();

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        // mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);


    }

    /**
     * 初始化数据
     */
    private void initData() {
        initLinePaint();
        list = new ArrayList<MyLine>();
        // 初始化
        // 根据答案个数 将自定义view分成几块 然后获取每个选项的x y 点

    }

    private void initLinePaint() {
        linePaint.setColor(Color.RED);
        linePaint.setStrokeWidth(5);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeCap(Cap.ROUND);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        /**
         * 设置宽度
         */
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                width = getPaddingLeft() + getPaddingRight() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                width = getPaddingLeft() + getPaddingRight() + mBound.width();
                break;
        }

        /**
         * 设置高度
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (specMode) {
            case MeasureSpec.EXACTLY:// 明确指定了
                height = getPaddingTop() + getPaddingBottom() + specSize;
                break;
            case MeasureSpec.AT_MOST:// 一般为WARP_CONTENT
                height = getPaddingTop() + getPaddingBottom() + mBound.height();
                break;
        }

        setMeasuredDimension(width, height);

        initAnswerPoint();

    }

    private void initAnswerPoint() {
        beginList = new ArrayList<Point>();
        // 根据答案个数 将自定义view分成几块 然后初始化每个选项的x y 点
        Log.d("", "宽 = " + getMeasuredWidth() + "  高 = " + getMeasuredHeight());
        int size = pointSize;
        for (int i = 0; i < size; i++) {
            beginList.add(getPoint(i));
        }

    }

    // 根据传入的答案返回返回线的点
    private Point getPoint(final int select) {
        Point point = null;
        int size = pointSize;
        Log.d("", "==宽度  = " + getMeasuredWidth() + "      高度  = "
                + getMeasuredHeight());
        int item = getMeasuredHeight() / size;
        int x = 0, y = 0;
        if (select % 2 == 0) {
            x = 0;
            y = item * (select + 1);
        } else {
            x = getMeasuredWidth();
            y = item * (select);
        }
        point = new Point(x, y);
        return point;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("", "觸摸事件");
        Log.d("", "宽 = " + getMeasuredWidth() + "  高 = " + getMeasuredHeight());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("", "按下事件  x = " + event.getX() + " y= " + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d("", "移動事件  x = " + event.getX() + " y= " + event.getY());
                break;
            case MotionEvent.ACTION_UP:
                Log.d("", "移動事件  x = " + event.getX() + " y= " + event.getY());
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(mTitleTextColor);

        if (list != null && list.size() > 0) {

            for (int i = 0; i < list.size(); i++) {
                drawLine(list.get(i), canvas);
            }
        }

    }

    /**
     * 根据答案 判断当前选择的是那一条线 并進行設置
     *
     * @param answer1
     * @param answer2
     */
    public void setLine(int answer1, int answer2, int color) {
        // list.get(location)
        // 如果没满 或者不存在 那么给他添加 否则 进行设置
        String id = getLineId(answer1, answer2, color);
        Log.d("", "线 Id = " + id);

        if (!isExists(answer1, answer2, color)) {
            if (isSameSide(answer1, answer2)) {
                return;
            }
            // 或者
            MyLine myLine = new MyLine(getPoint(answer1), getPoint(answer2),
                    id, color);
            list.add(myLine);
        } else {
            if (isSameSide(answer1, answer2)) {
                return;
            }
            for (int i = 0; i < list.size(); i++) {
                MyLine myLine = list.get(i);
                if (myLine.appear(answer1, answer2, color)) {
                    list.set(
                            i,
                            new MyLine(getPoint(answer1), getPoint(answer2), id, color));
                }
            }
        }

        invalidate();
    }

    public void setCorrectLine(int answer1, int answer2, int color) {
        String id = getLineId(answer1, answer2, color);
        MyLine myLine = new MyLine(getPoint(answer1), getPoint(answer2),
                id, color);
        list.add(myLine);
        invalidate();
    }

    /**
     * 是否是同一侧
     *
     * @param answer1
     * @param answer2
     * @return
     */
    private boolean isSameSide(int answer1, int answer2) {
        if (answer1 % 2 == 0 && answer2 % 2 == 0) {
            return true;
        }
        if (answer1 % 2 != 0 && answer2 % 2 != 0) {
            return true;
        }
        return false;


    }

    /**
     * 根据传入的答案判断 是否已经存入数据
     */
    public boolean isExists(int answer1, int answer2, int color) {
        if (list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            MyLine myLine = list.get(i);
            if (myLine.getId() != null && myLine.appear(answer1, answer2, color)) {
                return true;
            }
        }
        return false;
    }
    public boolean isAppear(int answer1, int answer2, int color) {
        if (list.size() <= 0) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            MyLine myLine = list.get(i);
            if (myLine.getId() != null && myLine.equals(answer1, answer2, color)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 画线
     *
     * @param myLine
     * @param canvas
     */
    private void drawLine(MyLine myLine, Canvas canvas) {
        linePaint.setColor(myLine.getColor());
        canvas.drawLine(myLine.getStartx(), myLine.getStarty(),
                myLine.getEndx(), myLine.getEndy(), linePaint);
    }

    public void setPointSize(int pointSize) {
        this.pointSize = pointSize;
    }


    class MyLine {

        int startx;
        int starty;
        int endx;
        int endy;
        String id;
        int color;

        public MyLine(Point point1, Point point2, String id, int color) {
            this.startx = point1.x;
            this.starty = point1.y;
            this.endx = point2.x;
            this.endy = point2.y;
            this.id = id;
            this.color = ColorHandle.getColorForID(context, color);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getStartx() {
            return startx;
        }

        public void setStartx(int startx) {
            this.startx = startx;
        }

        public int getStarty() {
            return starty;
        }

        public void setStarty(int starty) {
            this.starty = starty;
        }

        public int getEndx() {
            return endx;
        }

        public void setEndx(int endx) {
            this.endx = endx;
        }

        public int getEndy() {
            return endy;
        }

        public void setEndy(int endy) {
            this.endy = endy;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        @Override
        public String toString() {
            return "MyLine [startx =" + startx + ", starty =" + starty
                    + ", endx =" + endx + ", endy =" + endy + ", id =" + id
                    + "]";
        }

        public boolean appear(int answer1, int answer2, int color) {
            return (getId().contains(String.valueOf(answer1))
                    || getId().contains(String.valueOf(answer2)));
        }

        public boolean equals(int answer1, int answer2, int color) {
            String i = getLineId(answer1, answer2, color);
            return getId().equals(i);
        }
    }


    public String getLineId(int answer1, int answer2, int color) {
//        + String.valueOf(color)
        return String.valueOf(answer1) + String.valueOf(answer2);
    }


}
