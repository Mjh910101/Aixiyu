package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.DragAdapter;
import com.cn.ispanish.adapters.OtherAdapter;
import com.cn.ispanish.box.channel.ChannelItem;
import com.cn.ispanish.views.channel.DragGrid;
import com.cn.ispanish.views.channel.OtherGridView;

import java.util.ArrayList;

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
 * Created by Hua on 2017/11/15.
 */

public class ChannelActivity extends GestureDetectorActivity implements AdapterView.OnItemClickListener {

    public static String TAG = "ChannelActivity";
    public static int CHANNELRESULT = 1001023;
    /**
     * 用户栏目的GRIDVIEW
     */
    private DragGrid userGridView;
    /**
     * 其它栏目的GRIDVIEW
     */
    private OtherGridView otherGridView;
    private OtherGridView otherGridView2;
    /**
     * 用户栏目对应的适配器，可以拖动
     */
    DragAdapter userAdapter;
    /**
     * 其它栏目对应的适配器
     */
    OtherAdapter otherAdapter;
    OtherAdapter otherAdapter2;
    /**
     * 其它栏目列表
     */
    ArrayList<ChannelItem> otherChannelList = new ArrayList<ChannelItem>();
    ArrayList<ChannelItem> otherChannelList2 = new ArrayList<ChannelItem>();
    /**
     * 用户栏目列表
     */
    ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();
    /**
     * 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
     */
    boolean isMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        userChannelList = new ArrayList<>();
        otherChannelList = new ArrayList<>();
        otherChannelList2 = new ArrayList<>();
        ininTestList(userChannelList, otherChannelList, otherChannelList2);
        userAdapter = new DragAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);

        otherAdapter = new OtherAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);

        otherAdapter2 = new OtherAdapter(this, otherChannelList2);
        otherGridView2.setAdapter(otherAdapter2);

        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        otherGridView2.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    private void ininTestList(ArrayList<ChannelItem> userChannelList, ArrayList<ChannelItem> otherChannelList, ArrayList<ChannelItem> otherChannelList2) {
        userChannelList.add(new ChannelItem(1, 1, "推荐", 1, 1));
        userChannelList.add(new ChannelItem(1, 2, "热点", 2, 1));
        userChannelList.add(new ChannelItem(1, 3, "杭州", 3, 1));
        userChannelList.add(new ChannelItem(1, 4, "时尚", 4, 1));
        userChannelList.add(new ChannelItem(1, 5, "科技", 5, 1));
        userChannelList.add(new ChannelItem(2, 6, "体育", 6, 1));
        userChannelList.add(new ChannelItem(2, 7, "军事", 7, 1));

        otherChannelList.add(new ChannelItem(1, 8, "财经", 1, 0));
        otherChannelList.add(new ChannelItem(1, 9, "汽车", 2, 0));
        otherChannelList.add(new ChannelItem(1, 10, "房产", 3, 0));
        otherChannelList.add(new ChannelItem(1, 11, "社会", 4, 0));
        otherChannelList.add(new ChannelItem(1, 12, "情感", 5, 0));
        otherChannelList.add(new ChannelItem(1, 13, "女人", 6, 0));

        otherChannelList2.add(new ChannelItem(2, 14, "旅游", 7, 0));
        otherChannelList2.add(new ChannelItem(2, 15, "健康", 8, 0));
        otherChannelList2.add(new ChannelItem(2, 16, "美女", 9, 0));
        otherChannelList2.add(new ChannelItem(2, 17, "游戏", 10, 0));
        otherChannelList2.add(new ChannelItem(2, 18, "数码", 11, 0));
        otherChannelList2.add(new ChannelItem(2, 19, "娱乐", 12, 0));
    }

    /**
     * 初始化布局
     */
    private void initView() {
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
        otherGridView2 = (OtherGridView) findViewById(R.id.otherGridView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.channel, menu);
        return true;
    }

    /**
     * GRIDVIEW对应的ITEM点击监听接口
     */
    @Override
    public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0，1 的不可以进行任何操作
                if (position != 0 && position != 1) {
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final ChannelItem channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        if (channel.getFatherId() == 1) {
                            otherAdapter.setVisible(false);
                            //添加到最后一个
                            otherAdapter.addItem(channel);
                        } else if (channel.getFatherId() == 2) {
                            otherAdapter2.setVisible(false);
                            //添加到最后一个
                            otherAdapter2.addItem(channel);
                        }

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    if (channel.getFatherId() == 1) {
                                        otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    } else if (channel.getFatherId() == 2) {
                                        otherGridView2.getChildAt(otherGridView2.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    }
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView:
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            case R.id.otherGridView2:
                final ImageView moveImageView2 = getView(view);
                if (moveImageView2 != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final ChannelItem channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView2, startLocation, endLocation, channel, otherGridView2);
                                otherAdapter2.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final ChannelItem moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断2边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    otherAdapter2.setVisible(true);
                    otherAdapter2.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    if (moveChannel.getFatherId() == 1) {
                        otherAdapter.remove();
                    } else if (moveChannel.getFatherId() == 2) {
                        otherAdapter2.remove();
                    }
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后数据库的设置
     */
    private void saveChannel() {

    }

    @Override
    public void onBackPressed() {
        saveChannel();
        if (userAdapter.isListChanged()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            setResult(CHANNELRESULT, intent);
            finish();
            Log.d(TAG, "数据发生改变");
        } else {
            super.onBackPressed();
        }
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
