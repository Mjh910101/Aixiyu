package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperCollectionGridAdapter;
import com.cn.ispanish.adapters.PaperGridAdapter;
import com.cn.ispanish.box.question.Paper;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.HeaderGridView;
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
 * Created by Hua on 17/6/10.
 */
public class CollectionPaperActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.collectionPaper_dataGrid)
    private HeaderGridView dataGrid;
    @ViewInject(R.id.collectionPaper_emptyView)
    private TextView emptyView;

    private String id;
    PaperCollectionGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_paper_grid);

        ViewUtils.inject(this);
        initActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        downloadData();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("我的收藏");

        dataGrid.setEmptyView(emptyView);

        downloadData();

    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBackCollBanksel(context), params,
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
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                JSONArray array = JsonHandle.getArray(json, "data");
                                if (array != null) {
                                    initDataForArray(array);
                                }
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }


    private void initDataForArray(JSONArray array) {
        List<Paper> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Paper p = new Paper(JsonHandle.getJSON(array, i));
            list.add(p);
        }

        if (adapter == null) {
            dataGrid.addHeaderView(getHeadView());
        }
        adapter = new PaperCollectionGridAdapter(context, list);
        dataGrid.setAdapter(adapter);
    }

    public View getHeadView() {
        View headView = new View(context);
        headView.setLayoutParams(new AbsListView.LayoutParams(5, 5));
        return headView;
    }

}
