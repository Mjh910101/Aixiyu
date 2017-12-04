package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cn.ispanish.R;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.views.ZoomImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 17/8/29.
 */
public class FirstActivity extends BaseActivity {

    @ViewInject(R.id.first_dataView)
    private ViewPager dataView;

    private final static int[] IMAGE_LIST = new int[]{R.mipmap.first_1, R.mipmap.first_2, R.mipmap.first_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ViewUtils.inject(this);

        initActivity();

    }

    private void initActivity() {
        SystemHandle.setFirst(context);
        dataView.setAdapter(new ContentPagerAdapter());
    }

    class ContentPagerAdapter extends PagerAdapter {

        private Map<Integer, ImageView> imageMap;

        public ContentPagerAdapter() {
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
            return IMAGE_LIST.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView v;
            if (imageMap.containsKey(position)) {
                v = imageMap.get(position);
            } else {
                v = getImageView(IMAGE_LIST[position]);
                imageMap.put(position, v);
                if (position == IMAGE_LIST.length - 1) {
                    v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                }
            }
            container.addView(v);
            return v;
        }

        private ImageView getImageView(int id) {
            ImageView image = new ImageView(context);
            image.setImageResource(id);
            image.setScaleType(ImageView.ScaleType.FIT_CENTER);
            return image;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }


}
