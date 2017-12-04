package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperListAdapter;
import com.cn.ispanish.box.question.Paper;
import com.cn.ispanish.box.User;
import com.cn.ispanish.fragments.PaperFragment;
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
 * Created by Hua on 17/5/12.
 */
public class PaperListActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.paperList_dataList)
    private ListView dataList;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("题库");

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }

        type = b.getInt(PaperFragment.TYPE_KEY);

        downloadData(type);

    }

    private void downloadData(int type) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("is", "1");
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getQusetionBack(context), params,
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
                            JSONArray dataArray = JsonHandle.getArray(json, "conten");
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

        List<Paper> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Paper p = new Paper(JsonHandle.getJSON(array, i));
            list.add(p);
        }

        initDataAdapter(list);

    }

    private void initDataAdapter(List<Paper> list) {

        PaperListAdapter adapter = new PaperListAdapter(context, list);
        dataList.setAdapter(adapter);

    }

}
