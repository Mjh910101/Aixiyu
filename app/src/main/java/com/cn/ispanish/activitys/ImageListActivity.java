package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.HackyViewPager;
import com.cn.ispanish.views.ZoomImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by Hua on 16/12/21.
 */
public class ImageListActivity extends BaseActivity {

    public final static String POSITION_KEY = "position";
    public final static String LIST_KEY = "list";
    public final static String MESSAGE_KEY = "msg";

    private int position;
    private List<String> imageList;
    private List<ImageView> ballList;
    private String info;

    @ViewInject(R.id.imageList_infoText)
    private TextView infoText;
    @ViewInject(R.id.imageList_dataViewPager)
    private HackyViewPager dataPager;
    @ViewInject(R.id.imageList_ballLayout)
    private LinearLayout ballLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        ViewUtils.inject(this);

        setFlagSecure(true);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            position = b.getInt(POSITION_KEY);
            imageList = b.getStringArrayList(LIST_KEY);
            info = b.getString(MESSAGE_KEY);
            setImageList(imageList, position);
            setContentBoxListener();
        }
    }

    private void setImageList(List<String> list, int position) {
        dataPager.setAdapter(new ContentPagerAdapter(list));
        dataPager.setOffscreenPageLimit(1);
        dataPager.setCurrentItem(position);
        setInfoText(position + 1);
        setBallList(list.size(), position);
    }

    private void setContentBoxListener() {
        dataPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setInfoText(position + 1);
                setOnBall(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private ZoomImageView getImageView(String pic) {
        final ZoomImageView photoView = new ZoomImageView(context);
        DownloadImageLoader.loadImage(photoView, pic);
        return photoView;
    }

    public void setInfoText(int i) {
        infoText.setText(i + "/" + imageList.size() + " : " + info);
    }

    private void setBallList(int size, int position) {
        if (size > 1) {
            int w = WinHandler.pxToDip(context, 40);
            ballList = new ArrayList<ImageView>(size);
            for (int i = 0; i < size; i++) {
                ImageView v = new ImageView(context);
//                v.setImageResource(R.drawable.white_ball);
                v.setLayoutParams(new LinearLayout.LayoutParams(w, w));

                View l = new View(context);
                l.setLayoutParams(new LinearLayout.LayoutParams(w, w));

                ballLayout.addView(v);
                ballLayout.addView(l);

                ballList.add(v);
            }
            setOnBall(position);
        }
    }

    private void setOnBall(int p) {
//        for (ImageView v : ballList) {
//            v.setImageResource(R.drawable.white_ball);
//        }
//        ballList.get(p).setImageResource(R.drawable.green_ball);
    }

    class ContentPagerAdapter extends PagerAdapter {

        private List<String> iamgePhatList;
        private Map<Integer, ZoomImageView> imageMap;

        public ContentPagerAdapter(List<String> iamgePhatList) {
            this.iamgePhatList = iamgePhatList;
            imageMap = new HashMap<>();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            try {
                container.removeView(imageMap.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return iamgePhatList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ZoomImageView v;
            if (imageMap.containsKey(iamgePhatList.get(position))) {
                v = imageMap.get(position);
            } else {
                v = getImageView(iamgePhatList.get(position));
                imageMap.put(position, v);
            }
            Log.e("", iamgePhatList.get(position));
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
