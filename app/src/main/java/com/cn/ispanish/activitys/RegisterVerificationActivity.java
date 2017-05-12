package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
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
 * Created by Hua on 17/3/7.
 */
public class RegisterVerificationActivity extends BaseActivity {

    private final static int M = 60;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.registerVerification_mobileInput)
    private EditText mobileInput;
    @ViewInject(R.id.registerVerification_codeInput)
    private EditText codeInput;
    @ViewInject(R.id.registerVerification_getCodeButton)
    private TextView getCodeButton;
    @ViewInject(R.id.registerVerification_nextButton)
    private TextView nextButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_verification);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.registerVerification_getCodeButton)
    public void onGetCode(View view) {
        if (isHaveMobile()) {
            getCode();
            startRun();
        }
    }

    @OnClick(R.id.registerVerification_nextButton)
    public void onNext(View view) {
        Bundle b = new Bundle();
        b.putString(RegisterActivity.Mobile_Key, TextHandler.getText(mobileInput));
        b.putString(RegisterActivity.Code_Key, TextHandler.getText(codeInput));
        PassagewayHandler.jumpActivity(context, RegisterActivity.class, b);
    }

    private void startRun() {
        getCodeButton.setClickable(false);
        setMobileInputBg(false);
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
                getCodeButton.setText("发送验证码");
                getCodeButton.setClickable(true);
                setMobileInputBg(true);
            }
        }

    };

    private boolean isHaveMobile() {
        return TextHandler.getText(mobileInput).length() == 11;
    }

    private boolean isHaveCode() {
        return TextHandler.getText(codeInput).length() > 0;
    }

    public void setMobileInputBg(boolean b) {
        if (b) {
            getCodeButton.setBackgroundResource(R.color.blue_5e);
        } else {
            getCodeButton.setBackgroundResource(R.color.gray_aa);
        }
    }

    public void setNextButtonBg(boolean b) {
        if (b) {
            nextButton.setBackgroundResource(R.color.blue_5e);
            nextButton.setClickable(true);
        } else {
            nextButton.setBackgroundResource(R.color.gray_aa);
            nextButton.setClickable(false);
        }
    }

    private void initActivity() {
        titleText.setText("注册");

        setMobileInputListener();
        setCodeInputListener();
    }

    private void setCodeInputListener() {
        codeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setNextButtonBg(isHaveCode() && isHaveMobile());
            }
        });
    }

    private void setMobileInputListener() {
        mobileInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (getCodeButton.isClickable()) {
                    setMobileInputBg(isHaveMobile());
                }
                setNextButtonBg(isHaveCode() && isHaveMobile());
            }
        });
    }

    public void getCode() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("to", TextHandler.getText(mobileInput));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getSendNote(), params,
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
                            if (JsonHandle.getInt(json, "status") == 1) {
                                MessageHandler.showToast(context, "发送成功");
                            }else{
                                MessageHandler.showToast(context, "发送失败");
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}
