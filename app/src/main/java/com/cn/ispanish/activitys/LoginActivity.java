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
import com.mob.tools.utils.UIHandler;

import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

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
public class LoginActivity extends BaseActivity {

    public static final int RequestCode = 12002;


    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.login_mobileInput)
    private EditText mobileInput;
    @ViewInject(R.id.login_passwordInput)
    private EditText passwordInput;
    @ViewInject(R.id.login_loginButton)
    private TextView loginButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.login_registerButton)
    public void onRegister(View view) {
        PassagewayHandler.jumpActivity(context, RegisterVerificationActivity.class);
    }

    @OnClick(R.id.login_loginButton)
    public void onLogin(View view) {
        login();
    }

    @OnClick(R.id.login_weiboLoginButton)
    public void onWeiboLogin(View view) {
        weiboLogin();
    }

    @OnClick(R.id.login_qqLoginButton)
    public void onQQLogin(View view) {
        qqLogin();
    }

    @OnClick(R.id.login_wechatButton)
    public void onWechatLogin(View view) {
        wechatLogin();
    }

    private void initActivity() {
        titleText.setText("登录");

        setInputListener(mobileInput);
        setInputListener(passwordInput);
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
                setLoginButtonBg(isHaveMobile() && isHavePassword());
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

    private boolean isHaveMobile() {
        return TextHandler.getText(mobileInput).length() > 0;
    }

    public void setLoginButtonBg(boolean b) {
        if (b) {
            loginButton.setBackgroundResource(R.drawable.blue_blue_rounded_5);
            loginButton.setClickable(true);
        } else {
            loginButton.setBackgroundResource(R.drawable.gray_gray_rounded_5);
            loginButton.setClickable(false);
        }
    }

    private void login() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("dlmz", TextHandler.getText(mobileInput));
        params.addBodyParameter("yhmm", TextHandler.getText(passwordInput));


        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getLogin(), params,
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
                                User.saveUser(context, new User(json));
                                close();
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void close() {
        setResult(RequestCode);
        finish();
    }

    private void qqLogin() {
        authorizeLogin(QQ.NAME);
    }

    private void weiboLogin() {
        authorizeLogin(SinaWeibo.NAME);
    }

    private void wechatLogin() {
        authorizeLogin(Wechat.NAME);
    }

    private void authorizeLogin(String name) {
        Platform platform = ShareSDK.getPlatform(name);
        if (platform.isValid()) {
            platform.getDb().removeAccount();
        }
//回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        platform.setPlatformActionListener(new PlatformAction(name));
//authorize与showUser单独调用一个即可
        platform.authorize();//单独授权,OnComplete返回的hashmap是空的
        platform.showUser(null);//授权并获取用户信息
    }

    private void login(final Platform platform, String url, RequestParams params) {
        progress.setVisibility(View.VISIBLE);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, url, params,
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
//                                User.saveKey(context, JsonHandle.getString(json, "key"));
//                                User.saveName(context, platform.getDb().getUserName());
//                                User.savePortrait(context, platform.getDb().getUserIcon());
                                JSONObject memres = JsonHandle.getJSON(json, "memres");
                                if (memres != null) {
                                    User user = new User(memres);
                                    user.setKey(JsonHandle.getString(json, "key"));
                                    User.saveUser(context, user);
                                    close();
                                }
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    class PlatformAction implements PlatformActionListener {

        String p;

        PlatformAction(String platform) {
            this.p = platform;
            Log.e("", "----- PlatformAction -----");

        }

        @Override
        public void onComplete(Platform platform, int action,
                               HashMap<String, Object> res) {
            Log.e("", "----- onComplete -----");
            Log.e("PlatformActionListener", platform.getDb().getUserName() + "  |+|   " + platform.getDb().getUserId());
            int what = -1;
            if (p.equals(QQ.NAME)) {
                what = 1;
            } else if (p.equals(SinaWeibo.NAME)) {
                what = 2;
            } else if (p.equals(Wechat.NAME)) {
                what = 3;
            }
            Message.obtain(handler, what, platform).sendToTarget();
        }

        @Override
        public void onError(Platform platform, int action, Throwable t) {
            Log.e("", "----- onError -----");
            t.printStackTrace();
        }

        @Override
        public void onCancel(Platform platform, int action) {
            Log.e("", "----- onCancel -----");
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Platform platform = (Platform) msg.obj;

            String nickname = platform.getDb().getUserName();
            String avatar = platform.getDb().getUserIcon();
            String openid = platform.getDb().getUserId();
            String access_token = platform.getDb().getToken();

            RequestParams params = HttpUtilsBox.getRequestParams(context);

            switch (msg.what) {
                case 1:
                    params.addBodyParameter("url", platform.getDb().getToken());
                    login(platform, UrlHandle.getQjogg(), params);
                    break;
                case 2:
                    params.addBodyParameter("token", platform.getDb().getToken());
                    login(platform, UrlHandle.getWbdl(), params);
                    break;
                case 3:
                    params.addBodyParameter("token", platform.getDb().getToken());
                    params.addBodyParameter("openid", platform.getDb().getUserId());
                    login(platform, UrlHandle.getWxdl(), params);
                    break;
            }
        }
    };

}
