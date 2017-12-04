package com.cn.ispanish.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Paint;
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
import com.cn.ispanish.activitys.play.PlayMinClassActivity;
import com.cn.ispanish.activitys.play.PlayOrderActivity;
import com.cn.ispanish.box.MinClass;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.fragments.MinClassCommentsFragment;
import com.cn.ispanish.fragments.MinClassDataFragment;
import com.cn.ispanish.fragments.MinClassOutLineFragment;
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

public class MinClassContentActivity extends BaseActivity {

    public final static String OBJ_KEY = "min";

    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.minClassContent_pic)
    private ImageView contentPic;
    @ViewInject(R.id.minClassContent_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.minClassContent_teacherName)
    private TextView teacherName;
    @ViewInject(R.id.minClassContent_priceText)
    private TextView priceText;
    @ViewInject(R.id.minClassContent_originalText)
    private TextView originalText;
    @ViewInject(R.id.minClassContent_titleText)
    private TextView titleText;
    @ViewInject(R.id.minClassContent_classSumText)
    private TextView classSumText;
    @ViewInject(R.id.minClassContent_dataText)
    private TextView dataText;
    @ViewInject(R.id.minClassContent_classContentText)
    private TextView classContentText;
    @ViewInject(R.id.minClassContent_commentText)
    private TextView commentText;
    @ViewInject(R.id.minClassContent_dataLine)
    private View dataLine;
    @ViewInject(R.id.minClassContent_classContentLine)
    private View classContentLine;
    @ViewInject(R.id.minClassContent_commentLine)
    private View commentLine;
    @ViewInject(R.id.minClassContent_sendLayout)
    private RelativeLayout sendLayout;
    @ViewInject(R.id.minClassContent_buttonLayout)
    private LinearLayout buttonLayout;
    @ViewInject(R.id.minClassContent_inputView)
    private EditText inputView;
    @ViewInject(R.id.minClassContent_sendButton)
    private TextView sendButton;
    @ViewInject(R.id.minClassContent_buyButton)
    private TextView buyButton;
    @ViewInject(R.id.minClassContent_collectIcon)
    private ImageView collectIcon;

    private MinClass minObj;

    private FragmentManager fragmentManager;

    private MinClassDataFragment minClassDataFragment;
    private MinClassOutLineFragment minClassOutLineFragment;
    private MinClassCommentsFragment minClassCommentsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_min_class_content);

        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        minClassDataFragment = null;
        minObj.removeAllOutLine();

        dowmloadData(minObj.getId());
    }

    @OnClick(R.id.minClassContent_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.minClassContent_collectButton)
    public void onCollButton(View view) {
        uploadColl();
    }

    @OnClick({R.id.minClassContent_consultingButton, R.id.minClassContent_listenButton})
    public void onSignUp(View view) {
        PassagewayHandler.jumpQQ(context, "2907243207");
    }

    @OnClick(R.id.minClassContent_buyButton)
    public void onBuy(View view) {
        if (User.isLoginAndShowMessage(context)) {
            Bundle b = new Bundle();
            b.putString(PlayMinClassActivity.TOPUP_ID, minObj.getId());
            b.putString(PlayMinClassActivity.TOPUP_TITLE, minObj.getTitle());
            b.putString(PlayMinClassActivity.TOPUP_MONEY, minObj.getPrice());
            PassagewayHandler.jumpActivity(context, PlayMinClassActivity.class, PlayMinClassActivity.Result_Code, b);
        }
    }

    @OnClick({R.id.minClassContent_dataButton, R.id.minClassContent_classContentButton, R.id.minClassContent_commentButton})
    public void onClickButton(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initButton();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.minClassContent_dataButton:
                onData(transaction);
                break;
            case R.id.minClassContent_classContentButton:
                onClassContent(transaction);
                break;
            case R.id.minClassContent_commentButton:
                onComment(transaction);
                break;
        }
        transaction.commit();
    }

    private void onComment(FragmentTransaction transaction) {
        commentText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        commentLine.setVisibility(View.VISIBLE);
        sendLayout.setVisibility(View.VISIBLE);

        if (minClassCommentsFragment == null) {
            minClassCommentsFragment = new MinClassCommentsFragment(minObj, inputView, sendButton);
            transaction.add(R.id.minClassContent_contentLayout, minClassCommentsFragment);
        } else {
            transaction.show(minClassCommentsFragment);
        }
    }

    private void onClassContent(FragmentTransaction transaction) {
        classContentText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        classContentLine.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);

        if (minClassOutLineFragment == null) {
            minClassOutLineFragment = new MinClassOutLineFragment(minObj);
            transaction.add(R.id.minClassContent_contentLayout, minClassOutLineFragment);
        } else {
            transaction.show(minClassOutLineFragment);
        }
    }

    private void onData(FragmentTransaction transaction) {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.orange_text_f9));
        dataLine.setVisibility(View.VISIBLE);
        buttonLayout.setVisibility(View.VISIBLE);

        if (minClassDataFragment == null) {
            minClassDataFragment = new MinClassDataFragment(minObj);
            transaction.add(R.id.minClassContent_contentLayout, minClassDataFragment);
        } else {
            transaction.show(minClassDataFragment);
        }
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (minClassDataFragment != null) {
            transaction.hide(minClassDataFragment);
        }
        if (minClassOutLineFragment != null) {
            transaction.hide(minClassOutLineFragment);
        }
        if (minClassCommentsFragment != null) {
            transaction.hide(minClassCommentsFragment);
        }
    }

    private void initButton() {
        dataText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        classContentText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        commentText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));

        dataLine.setVisibility(View.INVISIBLE);
        classContentLine.setVisibility(View.INVISIBLE);
        commentLine.setVisibility(View.INVISIBLE);

        buttonLayout.setVisibility(View.INVISIBLE);
        sendLayout.setVisibility(View.INVISIBLE);
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        fragmentManager = getFragmentManager();

        minObj = (MinClass) b.getSerializable(OBJ_KEY);

        dowmloadData(minObj.getId());
    }

    private void dowmloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("id", id);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getMinClassContent(context), params,
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
        DownloadImageLoader.loadImage(contentPic, minObj.getImgindex());

        DownloadImageLoader.loadImage(teacherPic, "http://www.ispanish.cn" + JsonHandle.getString(data, "portrait"), WinHandler.dipToPx(context, 25));
        teacherName.setText(JsonHandle.getString(data, "tname"));

        titleText.setText(minObj.getTitle());
        classSumText.setText("课时：" + minObj.getClasstime());

        minObj.setContent(JsonHandle.getString(data, "content"));
        minObj.setOutLine(JsonHandle.getArray(data, "outline"));
        minObj.setBuy(JsonHandle.getInt(data, "isbuy") == 1);
        minObj.setColl(JsonHandle.getInt(data, "coll"));

        priceText.setText(minObj.getPrice());
        originalText.setText(minObj.getOriginal());
        originalText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        if (minObj.isBuy()) {
            buyButton.setText("已报名");
            buyButton.setClickable(false);
        } else {
            buyButton.setText("报名");
            buyButton.setClickable(true);
        }

        setColl();

        onClickButton(findViewById(R.id.minClassContent_dataButton));

    }

    private void setColl() {
        if (minObj.isColl()) {
            collectIcon.setImageResource(R.drawable.live_collection_1);
        } else {
            collectIcon.setImageResource(R.drawable.live_collection_0);
        }
    }

    private void uploadColl() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("lid", minObj.getId());
        params.addBodyParameter("type", minObj.getCollectType());

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
                                minObj.setColl(!minObj.isColl());
                                setColl();
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                    }
                });
    }

}
