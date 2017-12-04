package com.cn.ispanish.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.CompetitionRankingAdapter;
import com.cn.ispanish.adapters.RankingAdapter;
import com.cn.ispanish.box.Ranking;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.CircleView;
import com.cn.ispanish.views.InsideListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 17/6/23.
 */
public class MyselfRankingActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.ranking_circleView)
    private CircleView mCircleView;
    @ViewInject(R.id.ranking_dataList)
    private InsideListView dataList;
    @ViewInject(R.id.ranking_erroeText)
    private TextView erroeText;
    @ViewInject(R.id.ranking_rightText)
    private TextView rightText;
    @ViewInject(R.id.ranking_sumText)
    private TextView sumText;
    @ViewInject(R.id.ranking_sumView)
    private TextView sumView;
    @ViewInject(R.id.ranking_topText)
    private TextView topText;
    @ViewInject(R.id.ranking_userIcon)
    private ImageView userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_ranking);

        ViewUtils.inject(this);

        initActivity();
    }

    @Override
    protected void onDestroy() {
        mCircleView.stopWave();
        mCircleView = null;
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        downloatData();
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
                            downloatData();
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
        titleText.setText("我的成绩");
        // 设置多高，float，0.1-1F
        mCircleView.setmWaterLevel(0.1F);
        // 开始执行
        mCircleView.startWave();

        DownloadImageLoader.loadImage(userIcon, User.getPortrait(context), WinHandler.dipToPx(context, 22));

        downloatData();

    }

    private void downloatData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getPersonalscore(context), params,
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
                            JSONObject dataJson = JsonHandle.getJSON(json, "data");
                            if (dataJson != null) {
                                setDataList(JsonHandle.getArray(dataJson, "conten"));
                                setData(dataJson);
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }


                });
    }

    private void setData(JSONObject json) {
        int sum = JsonHandle.getInt(json, "donum");
        int right = JsonHandle.getInt(json, "right");
        int error = sum - right;

        sumView.setText(String.valueOf(sum));
        rightText.setText(String.valueOf(right));
        erroeText.setText(String.valueOf(error));

        mCircleView.setmWaterLevel(0.67F);
//        mCircleView.setmWaterLevel((float) right / (float) sum);
    }

    private void setDataList(JSONArray array) {
        if (array == null) {
            return;
        }

        sumText.setText(String.valueOf(array.length()));

        double max = 0;
        List<Ranking> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Ranking obj = new Ranking(JsonHandle.getJSON(array, i));
            list.add(obj);
            try {
                double m = Double.valueOf(obj.getAverage());
                if (m > max) {
                    max = m;
                }
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }

        dataList.setFocusable(false);

        topText.setText("您的历史最高成绩是：" + max);

        dataList.setAdapter(new RankingAdapter(context, list));
    }

}
