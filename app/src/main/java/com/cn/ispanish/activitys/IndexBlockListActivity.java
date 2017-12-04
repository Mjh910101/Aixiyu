package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.IndexBlockGirdAdapter;
import com.cn.ispanish.adapters.IndexBlockListAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
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
 * Created by Hua on 17/3/7.
 */
public class IndexBlockListActivity extends BaseActivity {

    public static String ID_KEY = "id_key";
    public static String TITLE_KEY = "title_key";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.indexBlockList_dataList)
    private ListView dataList;

    private String id;

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
        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        id = b.getString(ID_KEY);
        titleText.setText(b.getString(TITLE_KEY));

        downloadData(id);
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("type", id);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getClasstype(context), params,
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
                            JSONArray dataArray = JsonHandle.getArray(json, "body");
                            initDataAdapter(dataArray);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataAdapter(JSONArray array) {
        if (array == null) {
            return;
        }

        List<IndexBlock> list = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            list.add(new IndexBlock(JsonHandle.getJSON(array, i)));
        }

        dataList.addHeaderView(getHeadView());
        dataList.setAdapter(new IndexBlockListAdapter(context, list));
    }

    public View getHeadView() {
        View headView = new View(context);
        headView.setLayoutParams(new AbsListView.LayoutParams(10, 5));
        return headView;
    }
}
