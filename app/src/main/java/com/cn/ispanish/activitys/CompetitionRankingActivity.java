package com.cn.ispanish.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CompetitionRankingAdapter;
import com.cn.ispanish.box.RankingTap;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.CompetitionRankingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 17/6/10.
 */
public class CompetitionRankingActivity extends BaseActivity {

    private IndicatorViewPager indicatorViewPager;

    @ViewInject(R.id.competitionRanking_titlePage)
    private ScrollIndicatorView titleIndicator;
    @ViewInject(R.id.competitionRanking_viewPage)
    private ViewPager viewPager;
    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_ranking);

        ViewUtils.inject(this);

        initActivity();

        downloadData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SharePaperActivity.RequestCode:
                if (data != null) {
                    Bundle b = data.getExtras();
                    if (b != null) {
                        if (b.getBoolean(SharePaperActivity.Request_TEXT)) {
                            downloadData();
                        }
                    }
                }
                break;
        }
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {

        titleText.setText("历届排名");

        titleIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY));
        titleIndicator.setScrollBar(new ColorBar(context, 0xFF2196F3, 4));

        indicatorViewPager = new IndicatorViewPager(titleIndicator, viewPager);

    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getAllTestPaper(context),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
                            JSONArray array = JsonHandle.getArray(json, "data");
                            if (array != null) {
                                setTitleData(array);
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }

                });
    }

    private void setTitleData(JSONArray array) {
        List<RankingTap> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(new RankingTap(JsonHandle.getJSON(array, i)));
        }

        indicatorViewPager.setAdapter(new RankingTapIndicatorPagerAdapter(list));

    }

    class RankingTapIndicatorPagerAdapter extends IndicatorViewPager.IndicatorViewPagerAdapter {
        List<RankingTap> list;

        RankingTapIndicatorPagerAdapter(List<RankingTap> list) {
            this.list = list;
        }

        /**
         * 获取tab
         */
        @Override
        public View getViewForTab(int position, View convertView,
                                  ViewGroup container) {
            int p = 20;
            TextView view = new TextView(context);
            view.setText(list.get(position).getName());
            view.setTextSize(13);
            view.setGravity(Gravity.CENTER_HORIZONTAL);
            view.setPadding(p, 0, p, 0);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        /**
         * 获取每一个界面
         */
        @Override
        public View getViewForPage(int position, View convertView,
                                   ViewGroup container) {
//            if (convertView == null) {
//                convertView = View.inflate(context, R.layout.layout_comoetiton_ranking_content, null);
//            }
//
//            ListView listView = (ListView) convertView.findViewById(R.id.competitionRanking_viewPage_contentItem);
//            listView.setAdapter(new CompetitionRankingAdapter(context, list.get(position).getId()));

            CompetitionRankingView view= new CompetitionRankingView(context,list.get(position));
            return view;
        }

        /**
         * 获取界面数量
         */
        @Override
        public int getCount() {
            return list.size();
        }

    }

}
