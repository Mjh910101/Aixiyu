package com.cn.ispanish.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.Live;
import com.cn.ispanish.box.OneByOne;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.fragments.IndexFragment;
import com.cn.ispanish.fragments.MinClassCommentsFragment;
import com.cn.ispanish.fragments.OneByOneCommentsFragment;
import com.cn.ispanish.fragments.OneByOneDataFragment;
import com.cn.ispanish.fragments.OneByOneOutLineFragment;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
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

import org.json.JSONObject;

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
 * Created by Hua on 2017/10/9.
 */

public class OneByOneContentActivity extends BaseActivity {

    public final static String OBJ_KEY = "one";

    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.one2OneContent_pic)
    private ImageView contentPic;
    @ViewInject(R.id.one2OneContent_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.one2OneContent_teacherName)
    private TextView teacherName;
    @ViewInject(R.id.one2OneContent_titleText)
    private TextView titleText;
    @ViewInject(R.id.one2OneContent_classSumText)
    private TextView classSumText;
    @ViewInject(R.id.one2OneContent_infoText)
    private TextView infoText;
    @ViewInject(R.id.one2OneContent_dataText)
    private TextView dataText;
    @ViewInject(R.id.one2OneContent_classContentText)
    private TextView classContentText;
    @ViewInject(R.id.one2OneContent_commentText)
    private TextView commentText;
    @ViewInject(R.id.one2OneContent_dataLine)
    private View dataLine;
    @ViewInject(R.id.one2OneContent_classContentLine)
    private View classContentLine;
    @ViewInject(R.id.one2OneContent_commentLine)
    private View commentLine;
    @ViewInject(R.id.one2OneContent_collIcon)
    private ImageView collIcon;

    @ViewInject(R.id.one2OneContent_sendLayout)
    private RelativeLayout sendLayout;
    @ViewInject(R.id.one2OneContent_buttonLayout)
    private LinearLayout buttonLayout;
    @ViewInject(R.id.one2OneContent_inputView)
    private EditText inputView;
    @ViewInject(R.id.one2OneContent_sendButton)
    private TextView sendButton;

    private OneByOne oneObj;

    private FragmentManager fragmentManager;

    private OneByOneDataFragment oneByOneDataFragment;
    private OneByOneOutLineFragment oneByOneOutLineFragment;
    private OneByOneCommentsFragment oneByOneCommentsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_by_one_content);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick({R.id.one2OneContent_dataButton, R.id.one2OneContent_classContentButton, R.id.one2OneContent_commentButton})
    public void onClickButton(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initButton();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.one2OneContent_dataButton:
                onData(transaction);
                break;
            case R.id.one2OneContent_classContentButton:
                onClassContent(transaction);
                break;
            case R.id.one2OneContent_commentButton:
                onComment(transaction);
                break;
        }
        transaction.commit();
    }

    @OnClick(R.id.one2OneContent_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.one2OneContent_collButton)
    public void onCollButton(View view) {
        uploadColl();
    }

    @OnClick({R.id.one2OneContent_signUpButton, R.id.one2OneContent_consultingButton, R.id.one2OneContent_listenButton})
    public void onSignUp(View view) {
        PassagewayHandler.jumpQQ(context, "2907243207");
    }

    private void onComment(FragmentTransaction transaction) {
        commentText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        commentLine.setVisibility(View.VISIBLE);
        sendLayout.setVisibility(View.VISIBLE);

        if (oneByOneCommentsFragment == null) {
            oneByOneCommentsFragment = new OneByOneCommentsFragment(oneObj, inputView, sendButton);
            transaction.add(R.id.one2OneContent_contentLayout, oneByOneCommentsFragment);
        } else {
            transaction.show(oneByOneCommentsFragment);
        }
    }

    private void onClassContent(FragmentTransaction transaction) {
        classContentText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        classContentLine.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);

        if (oneByOneOutLineFragment == null) {
            oneByOneOutLineFragment = new OneByOneOutLineFragment(oneObj);
            transaction.add(R.id.one2OneContent_contentLayout, oneByOneOutLineFragment);
        } else {
            transaction.show(oneByOneOutLineFragment);
        }
    }

    private void onData(FragmentTransaction transaction) {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dataLine.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);

        if (oneByOneDataFragment == null) {
            oneByOneDataFragment = new OneByOneDataFragment(oneObj);
            transaction.add(R.id.one2OneContent_contentLayout, oneByOneDataFragment);
        } else {
            transaction.show(oneByOneDataFragment);
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (oneByOneDataFragment != null) {
            transaction.hide(oneByOneDataFragment);
        }
        if (oneByOneOutLineFragment != null) {
            transaction.hide(oneByOneOutLineFragment);
        }
        if (oneByOneCommentsFragment != null) {
            transaction.hide(oneByOneCommentsFragment);
        }
    }

    private void initButton() {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        classContentText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        commentText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));

        dataLine.setVisibility(View.INVISIBLE);
        classContentLine.setVisibility(View.INVISIBLE);
        commentLine.setVisibility(View.INVISIBLE);

        sendLayout.setVisibility(View.INVISIBLE);
        buttonLayout.setVisibility(View.INVISIBLE);
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        fragmentManager = getFragmentManager();

        oneObj = (OneByOne) b.getSerializable(OBJ_KEY);

        dowmloadData(oneObj.getId());
    }

    private void dowmloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("id", id);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getOneByOneContent(context), params,
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

                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                setData(JsonHandle.getJSON(json, "data"));
                            }
                        }
                    }
                });
    }

    private void setData(JSONObject data) {
        DownloadImageLoader.loadImage(contentPic, oneObj.getImgindex());

        DownloadImageLoader.loadImage(teacherPic, "http://www.ispanish.cn" + JsonHandle.getString(data, "portrait"), WinHandler.dipToPx(context, 25));
        teacherName.setText(JsonHandle.getString(data, "tname"));

        titleText.setText(oneObj.getTitle());
        classSumText.setText("总课时：" + oneObj.getClasstime());
        infoText.setText("今日免费领取试听名额：" + JsonHandle.getString(data, "opennum") + "  剩余：" + JsonHandle.getString(data, "lastnum"));

        oneObj.setContent(JsonHandle.getString(data, "content"));
        oneObj.setOutLine(JsonHandle.getArray(data, "outline"));
        oneObj.setColl(JsonHandle.getInt(data, "coll"));

        onClickButton(findViewById(R.id.one2OneContent_dataButton));

        setColl();
    }

    private void setColl() {
        if (oneObj.isColl()) {
            collIcon.setImageResource(R.drawable.live_collection_1);
        } else {
            collIcon.setImageResource(R.drawable.live_collection_0);
        }
    }

    private void uploadColl() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("lid", oneObj.getId());
        params.addBodyParameter("type", oneObj.getCollectType());

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLiveCollect(context), params,
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

                        progress.setVisibility(View.GONE);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null) {
                            if (JsonHandle.getInt(json, "code") == 1) {
                                oneObj.setColl(!oneObj.isColl());
                                setColl();
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                    }
                });
    }

}
