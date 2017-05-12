package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.cn.ispanish.R;
import com.cn.ispanish.views.PaperContentView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Hua on 17/5/9.
 */
public class PaperActivity extends BaseActivity {

    @ViewInject(R.id.paper_paperLayout)
    private ViewPager pagerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper);

        ViewUtils.inject(this);

        setTestList();
    }

    private void setTestList() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("TEST : " + i);
        }
        pagerLayout.setAdapter(new PagerContentAdapter(list));
        pagerLayout.setOffscreenPageLimit(1);
        pagerLayout.setCurrentItem(0);
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    class PagerContentAdapter extends PagerAdapter {

        private List<String> list;

        public PagerContentAdapter(List<String> list) {
            this.list = list;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PaperContentView view = new PaperContentView(context);
            view.setTestView(list.get(position));

            container.addView(view, 0);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
