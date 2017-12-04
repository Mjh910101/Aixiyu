package com.cn.ispanish.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
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
public class ForgetActivity extends BaseActivity {

    private final static int M = 60;
    private final static String NUM_CONTENT = "190205";

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.forget_telInput)
    private EditText mobileInput;
    @ViewInject(R.id.forget_codeButton)
    private TextView getCodeButton;
    @ViewInject(R.id.forget_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.forget_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.forget_passwordAgainInput)
    private EditText passwordAgainInput;
    @ViewInject(R.id.forget_passwordJudgeIcon)
    private ImageView judgeIcon;

    boolean isCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.forget_codeButton)
    public void onGetCode(View view) {
        if (isHaveMobile()) {
            getCode();
            startRun();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mobileInput.getWindowToken(), 0);
        }
    }

    @OnClick(R.id.forget_forgetButton)
    public void onForget(View view) {
        if (isHaveMobile() && isHaveCode() && isHavePassword() && isCorrect) {
            forgetPassword();
        }
    }

    private void initActivity() {
        titleText.setText("忘记密码");

        setTextChangedListener(passwordInput);
        setTextChangedListener(passwordAgainInput);
    }

    private void setTextChangedListener(EditText input) {
        input.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                judgeIcon.setVisibility(View.INVISIBLE);
                if (!TextHandler.getText(passwordAgainInput).equals("")) {
                    judgeIcon.setVisibility(View.VISIBLE);
                    if (TextHandler.getText(passwordInput).equals(TextHandler.getText(passwordAgainInput))) {
                        judgeIcon.setImageResource(R.drawable.judge_true_icon);
                        isCorrect = true;
                    } else {
                        judgeIcon.setImageResource(R.drawable.judge_flase_icon);
                        isCorrect = false;
                    }
                }
            }
        });
    }

    private boolean isHaveCode() {
        return TextHandler.getText(codeInput).length() > 0;
    }

    private boolean isHaveMobile() {
        return TextHandler.getText(mobileInput).length() == 11;
    }

    private boolean isHavePassword() {
        int l = TextHandler.getText(passwordInput).length();
        if (l >= 6) {
//        if (l >= 6 && l <= 16) {
            return true;
        }
        return false;
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

    private void forgetPassword() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("to", TextHandler.getText(mobileInput));
        params.addBodyParameter("yzma", TextHandler.getText(codeInput));
        params.addBodyParameter("newpwd", TextHandler.getText(passwordInput));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getLostPaseword(context), params,
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
                                MessageHandler.showToast(context, "修改成功");
                                finish();
                            } else {
                                MessageHandler.showException(context, json);
                            }
                        }
                    }
                });
    }

}
