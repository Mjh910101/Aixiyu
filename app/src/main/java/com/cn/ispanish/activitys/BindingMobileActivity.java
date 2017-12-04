package com.cn.ispanish.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.TextHandler;
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
 * Created by Hua on 17/7/11.
 */
public class BindingMobileActivity extends BaseActivity {

    private final static int M = 60;
    private final static String NUM_CONTENT = "190335";

    @ViewInject(R.id.title_backIcon)
    public ImageView backIcon;
    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.bindingMobile_telInput)
    private EditText mobileInput;
    @ViewInject(R.id.bindingMobile_codeButton)
    private TextView getCodeButton;
    @ViewInject(R.id.bindingMobile_codeInput)
    private EditText codeInput;

    boolean isCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binding_mobile);

        ViewUtils.inject(this);

        initActivity();
    }

    private void initActivity() {
        titleText.setText("绑定手机");
        backIcon.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        close();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            close();
        }
        return false;
    }

    private void close() {
        if (User.isLogin(context)) {
            User.logout(context);
            PassagewayHandler.jumpToActivity(context, MainActivity.class);
        }
    }

    @OnClick(R.id.bindingMobile_codeButton)
    public void onGetCode(View view) {
        if (isHaveMobile()) {
            getCode();
            startRun();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mobileInput.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.bindingMobile_uploadButton)
    public void onUpload(View view) {
        if (isHaveMobile() && isHaveCode()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mobileInput.getWindowToken(), 0);
            uploadMobile();
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//            showCloseDialog();
//        }
//        return false;
//    }
//
//    private void showCloseDialog() {
//
//    }

    private boolean isHaveCode() {
        return TextHandler.getText(codeInput).length() > 0;
    }

    private boolean isHaveMobile() {
        return TextHandler.getText(mobileInput).length() == 11;
    }


    private void startRun() {
        getCodeButton.setClickable(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = M; i >= 0; i--) {
                    Message.obtain(clockHandler, i).sendToTarget();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler clockHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            int time = msg.what;
            getCodeButton.setText(time + "秒后重新获取");
            if (time == 0) {
                getCodeButton.setText("点击获取验证码");
                getCodeButton.setClickable(true);
            }
        }

    };

    public void getCode() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("to", TextHandler.getText(mobileInput));
        params.addBodyParameter("num", NUM_CONTENT);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getSendMessage(context), params,
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
                                MessageHandler.showToast(context, "发送成功");
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                    }
                });
    }

    private void uploadMobile() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("to", TextHandler.getText(mobileInput));
        params.addBodyParameter("yzma", TextHandler.getText(codeInput));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getPhoneBinding(context), params,
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
                                MessageHandler.showToast(context, "绑定成功");
                                finish();
                            } else {
                                MessageHandler.showException(context, json);
//                                MessageHandler.showToast(context,"该号码已注册过，请直接登录或找回密码");
                            }
                        }
                    }
                });
    }

}
