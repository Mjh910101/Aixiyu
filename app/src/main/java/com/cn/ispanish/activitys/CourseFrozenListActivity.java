package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.IndexBlockListAdapter;
import com.cn.ispanish.adapters.MyselfBlockListAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
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
 * Created by Hua on 17/4/10.
 */
public class CourseFrozenListActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.courseFronzen_allButton)
    private TextView allButton;
    @ViewInject(R.id.courseFronzen_unFinishedButton)
    private TextView unFinishedButton;
    @ViewInject(R.id.courseFronzen_finishedButton)
    private TextView finishedButton;
    @ViewInject(R.id.courseFronzen_allLine)
    private View allLine;
    @ViewInject(R.id.courseFronzen_unFinishedLine)
    private View unFinishedLine;
    @ViewInject(R.id.courseFronzen_finishedLine)
    private View finishedLine;
    @ViewInject(R.id.courseFronzen_dataList)
    private ListView dataList;

    private MyselfBlockListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_frozen_list);

        ViewUtils.inject(this);
        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick({R.id.courseFronzen_all, R.id.courseFronzen_unFinished, R.id.courseFronzen_finished})
    public void onTap(View view) {
        initView();
        switch (view.getId()) {
            case R.id.courseFronzen_all:
                onAllButton();
                break;
            case R.id.courseFronzen_unFinished:
                onUnFinisButton();
                break;
            case R.id.courseFronzen_finished:
                onFinisButton();
                break;
        }
    }

    private void onAllButton() {
        allButton.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        allLine.setVisibility(View.VISIBLE);
        downloadData();
    }

    private void onUnFinisButton() {
        unFinishedButton.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        unFinishedLine.setVisibility(View.VISIBLE);
        downloadData();
    }

    private void onFinisButton() {
        finishedButton.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
        finishedLine.setVisibility(View.VISIBLE);
        downloadData();
    }

    private void initView() {
        allButton.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        unFinishedButton.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        finishedButton.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));

        allLine.setVisibility(View.INVISIBLE);
        unFinishedLine.setVisibility(View.INVISIBLE);
        finishedLine.setVisibility(View.INVISIBLE);
    }

    private void initActivity() {
        titleText.setText("我的课程");

        onTap(findViewById(R.id.courseFronzen_all));
    }

    public View getHeadView() {
        View headView = new View(context);
        headView.setLayoutParams(new AbsListView.LayoutParams(10, 5));
        return headView;
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getMyVedioList(context), params,
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

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            initDataForArray(JsonHandle.getArray(json, "arr"));
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataForArray(JSONArray array) {
        if (array == null) {
            return;
        }
        List<IndexBlock> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new IndexBlock(JsonHandle.getJSON(array, i)));
        }

        listAdapter = new MyselfBlockListAdapter(context);
        listAdapter.addAllItem(list);

        dataList.setAdapter(listAdapter);
    }
}
