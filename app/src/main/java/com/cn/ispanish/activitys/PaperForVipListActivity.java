package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.PaperForVipAdapter;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VipInformation;
import com.cn.ispanish.dialog.MessageDialog;
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
public class PaperForVipListActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.paperList_dataList)
    private ListView dataList;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_list);

        ViewUtils.inject(this);

        setFlagSecure(true);
        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (User.isLogin(context)) {
            downloadData(type);
        }
    }

    private void initActivity() {
        titleText.setText("独家资料");

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }
        type = b.getString(PaperFragment.TYPE_KEY);
        if (User.isLoginAndShowMessage(context, new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                finish();
            }
        })) {
            downloadData(type);
        }
    }

    private void downloadData(String type) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("type", type);
        params.addBodyParameter("classify", type);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getImgInforMation(context), params,
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
                        if (json != null & JsonHandle.getInt(json, "code") == 1) {
                            JSONArray array = JsonHandle.getArray(json, "data");
                            initDataAdapter(array);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataAdapter(JSONArray array) {
        if (array == null) {
            return;
        }

        List<VipInformation> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            VipInformation p = new VipInformation(JsonHandle.getJSON(array, i));
            list.add(p);
        }

        initDataAdapter(list);

    }

    private void initDataAdapter(List<VipInformation> list) {

        PaperForVipAdapter adapter = new PaperForVipAdapter(context, list);
        dataList.setAdapter(adapter);

    }

}
