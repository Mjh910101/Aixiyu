package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
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
 * Created by Hua on 17/3/7.
 */
public class RegisterActivity extends BaseActivity {

    public static final String Mobile_Key = "mobile";
    public static final String Code_Key = "code";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.register_nikeNameInput)
    private EditText nikeNameInput;
    @ViewInject(R.id.register_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.register_uploadButton)
    private TextView uploadButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private String mobile = "", code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.register_uploadButton)
    public void onUpload(View view) {
        regiser();
    }

    private void initActivity() {
        titleText.setText("注册");

        setInputListener(nikeNameInput);
        setInputListener(passwordInput);

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        mobile = b.getString(Mobile_Key);
        code = b.getString(Code_Key);

    }

    private void setInputListener(EditText input) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setUploadButtonBg(isHaveNikeName() && isHavePassword());
            }
        });
    }

    private boolean isHavePassword() {
        int l = TextHandler.getText(passwordInput).length();
        if (l >= 6 && l <= 16) {
            return true;
        }
        return false;
    }

    private boolean isHaveNikeName() {
        return TextHandler.getText(nikeNameInput).length() > 0;
    }

    public void setUploadButtonBg(boolean b) {
        if (b) {
            uploadButton.setBackgroundResource(R.color.blue_5e);
            uploadButton.setClickable(true);
        } else {
            uploadButton.setBackgroundResource(R.color.gray_aa);
            uploadButton.setClickable(false);
        }
    }

    private void regiser() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("to", mobile);
        params.addBodyParameter("zczh", TextHandler.getText(nikeNameInput));
        params.addBodyParameter("dlmm", TextHandler.getText(passwordInput));
        params.addBodyParameter("yzma", code);
        params.addBodyParameter("zctype", "手机号");

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getRegister(), params,
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
                            if (!MessageHandler.showException(context, json)) {
                                String appkey = JsonHandle.getString(json, "appkey");
                                if (appkey != null) {
                                    PassagewayHandler.jumpToActivity(context, LoginActivity.class);
                                }
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }


}
