package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperGridAdapter;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.question.Paper;
import com.cn.ispanish.box.question.PaperTitle;
import com.cn.ispanish.fragments.NewOldPaperFragment;
import com.cn.ispanish.fragments.PaperFragment;
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
 * Created by Hua on 17/5/12.
 */
public class PaperGridOldActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.paperGrid_dataGrid)
    private HeaderGridView dataGrid;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private int type;
    private PaperGridAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_grid);

        ViewUtils.inject(this);

//        dataGrid.setAdapter(new TestA());
        initActivity();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        uploadData(type);
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
        downloadData(type, new RequestCallBack<String>() {

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

    private void uploadData(int type) {
        downloadData(type, new RequestCallBack<String>() {

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
                    uploadDataAdapter(dataArray);
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void downloadData(int type, RequestCallBack<String> callBack) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("is", "2");
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getQusetionBack(context), params,
                callBack);
    }

    private void uploadDataAdapter(JSONArray array) {
        if (array == null) {
            return;
        }

        List<Paper> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Paper p = new Paper(JsonHandle.getJSON(array, i));
            list.add(p);
        }
        if (adapter != null) {
            adapter.uploadData(list);
        }
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
        dataGrid.addHeaderView(getHeadView());
        adapter = new PaperGridAdapter(context, list);
        dataGrid.setAdapter(adapter);
    }

    public View getHeadView() {
        View headView = new View(context);
        headView.setLayoutParams(new AbsListView.LayoutParams(5, 5));
        return headView;
    }
}
