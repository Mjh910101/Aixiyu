package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperGridAdapter;
import com.cn.ispanish.box.question.Paper;
import com.cn.ispanish.box.User;
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
public class PaperGridActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.paperGrid_dataGrid)
    private HeaderGridView dataGrid;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paperTypeList_typeLayout)
    private RelativeLayout typeLayout;
    @ViewInject(R.id.paperTypeList_typeList)
    private ListView typeList;

    private PaperTitle type;
    private PaperGridAdapter adapter;

    private String typeId = "", thrId = "";

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
        uploadData(typeId, thrId);
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.title_titleText)
    public void onTitle(View view) {
        typeLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.paperTypeList_closeTypeLayoutButton)
    public void onCloseTypeLayoutButton(View view) {
        typeLayout.setVisibility(View.GONE);
    }

    private List<String> getTypeData() {
        List<String> data = new ArrayList<>();
        for (PaperTitle obj : type.getTypeList()) {
            data.add(obj.getName());
        }
        return data;
    }

    private void initActivity() {
        titleText.setText("题库");

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }

        type = (PaperTitle) b.getSerializable(NewOldPaperFragment.TYPE_OBJ_KEY);

        if (type.isHaveChild()) {
            typeId = type.getId();
            thrId = type.getTypeList().get(0).getId();
            titleText.setText(type.getTypeList().get(0).getName() + " ﹀");
            typeList.setAdapter(new ArrayAdapter<String>(this, R.layout.view_paper_title_item, getTypeData()));
            typeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    thrId = type.getTypeList().get(i).getId();
                    titleText.setText(type.getTypeList().get(i).getName() + " ﹀");
                    if (adapter != null) {
                        adapter.removeAll();
                    }
                    uploadAllData(typeId, thrId);
                    typeLayout.setVisibility(View.GONE);
                }
            });
            titleText.setClickable(true);
            downloadData(typeId, thrId);
        } else {
            typeId = type.getId();
            titleText.setText(type.getName());
            downloadData(typeId, "");
            titleText.setClickable(false);
        }

    }

    private void downloadData(String type, String thr) {
        downloadData(type, thr, new RequestCallBack<String>() {

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

    private void uploadData(String type, String thr) {
        downloadData(type, thr, new RequestCallBack<String>() {

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

    private void uploadAllData(String type, String thr) {
        downloadData(type, thr, new RequestCallBack<String>() {

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
                    uploadAllDataAdapter(dataArray);
                }
                progress.setVisibility(View.GONE);
            }
        });
    }

    private void downloadData(String type, String thr, RequestCallBack<String> callBack) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("is", "1");
        params.addBodyParameter("type", type);
        params.addBodyParameter("thr", thr);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getQusetionBack(context), params,
                callBack);
    }

    private void uploadAllDataAdapter(JSONArray array) {
        if (array == null) {
            return;
        }

        List<Paper> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Paper p = new Paper(JsonHandle.getJSON(array, i));
            list.add(p);
        }
        if (adapter != null) {
            adapter.uploadAllData(list);
        }
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

    View headView;

    public View getHeadView() {
        if (headView == null) {
            headView = new View(context);
            headView.setLayoutParams(new AbsListView.LayoutParams(5, 5));
        }
        return headView;
    }
}
