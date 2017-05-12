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
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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
 * Created by Hua on 17/3/15.
 */
public class CollectionListActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.indexBlockList_dataList)
    private ListView dataList;

    private String id;

    private IndexBlockListAdapter indexBlockListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_block_list);

        ViewUtils.inject(this);
        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("我的收藏");

        dataList.addHeaderView(getHeadView());
        indexBlockListAdapter = new IndexBlockListAdapter(context);
        dataList.setAdapter(indexBlockListAdapter);

        initDataForDB();
        downloadData();

    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getCollection(), params,
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

                        JSONArray array = JsonHandle.getArray(result);
                        if (array != null) {
                            initDataForArray(array);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataForArray(JSONArray array) {
        List<IndexBlock> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new IndexBlock(JsonHandle.getJSON(array, i)));
        }
        indexBlockListAdapter.addAllItem(list);
    }

    private void initDataForDB() {
        try {
            List<IndexBlock> list = DBHandler.getDbUtils(context).findAll(IndexBlock.class);
            if (list != null) {
                indexBlockListAdapter.addAllItem(list);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public View getHeadView() {
        View headView = new View(context);
        headView.setLayoutParams(new AbsListView.LayoutParams(10, 5));
        return headView;
    }

}
