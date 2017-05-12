package com.cn.ispanish.activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.topup.weixin.MD5;
import com.cn.ispanish.topup.zhifubao.AuthResult;
import com.cn.ispanish.topup.zhifubao.PayResult;
import com.cn.ispanish.topup.zhifubao.ZhiFuBao;
import com.cn.ispanish.wxapi.WXPayEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.util.Map;

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
 * Created by Hua on 17/4/5.
 */
public class PlayOrderActivity extends BaseActivity {

    public final static String TOPUP_MONEY = "money";
    public final static String TOPUP_TITLE = "title";
    public final static String TOPUP_BLOCK = "block_id";
    public final static String Result_Key = "result_code";
    public final static int Result_Code = 22202;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    @ViewInject(R.id.playOrder_confirm_zhifubaoRadio)
    private RadioButton zhifubaoRadio;
    @ViewInject(R.id.playOrder_confirm_weixinRadio)
    private RadioButton weixinRadio;
    @ViewInject(R.id.playOrder_confirm_topUpManey)
    private TextView topUpManeyText;
    @ViewInject(R.id.playOrder_confirm_titleText)
    private TextView orderTitleText;

    private String money, cID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_order);

        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case WXPayEntryActivity.WEIXIN_RESULT:
                if (data == null) {
                    return;
                }
                if (data.getBooleanExtra(WXPayEntryActivity.Result_Key, false)) {
                    close();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick({R.id.playOrder_confirm_zhifubaoLayout, R.id.playOrder_confirm_weixinLayout})
    public void onPlayType(View view) {
        switch (view.getId()) {
            case R.id.playOrder_confirm_zhifubaoLayout:
                setZhiFuBaoRadio();
                break;
            case R.id.playOrder_confirm_weixinLayout:
                setWeixinRadio();
                break;
        }
    }

    @OnClick(R.id.playOrder_confirm_topUpBtm)
    public void onPlay(View view) {
        if (zhifubaoRadio.isChecked()) {
            downloadOrderIdToZhifubao();
        } else if (weixinRadio.isChecked()) {
            downloadOrderIdToWeixin();
        }

    }

    private void initActivity() {
        titleText.setText("确认购买");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        User.isLoginAndShowMessage(context, new MessageDialog.CallBackListener() {
            @Override
            public void callback() {
                finish();
            }
        });

        cID = b.getString(TOPUP_BLOCK);
        money = b.getString(TOPUP_MONEY);
        orderTitleText.setText(b.getString(TOPUP_TITLE));
        topUpManeyText.setText(money);

    }

    public void setZhiFuBaoRadio() {
        initRadio();
        zhifubaoRadio.setChecked(true);
    }

    private void initRadio() {
        zhifubaoRadio.setChecked(false);
        weixinRadio.setChecked(false);
    }

    public void setWeixinRadio() {
        initRadio();
        weixinRadio.setChecked(true);
    }

    private void downloadOrderIdToZhifubao() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", cID);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getAndpayali(), params,
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
                                playOrderInZhufubao(JsonHandle.getString(json, "indent"));
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void playOrderInZhufubao(String indent) {
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = ZhiFuBao.getOrderInfo(TextHandler.getText(orderTitleText), money, indent);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PlayOrderActivity.this);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(payInfo, true);

                Message msg = new Message();
                msg.what = ZhiFuBao.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ZhiFuBao.SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        MessageHandler.showToast(context, "支付成功");
                        close();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        MessageHandler.showToast(context, "支付失败");
                    }
                    break;
                }
            }
        }
    };

    private void downloadOrderIdToWeixin() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("cid", cID);
        params.addBodyParameter("yhkey", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getWxpay(), params,
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
                                playOrderInWeixin(json);
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private static String appId = "wx8d4dd63a75e1154b";
    private IWXAPI msgApi;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
//        msgApi.handleIntent(intent, wxHandler);
    }

    private void playOrderInWeixin(JSONObject json) {
        String timeStamp = String.valueOf(DateHandle.getTime());
        msgApi = WXAPIFactory.createWXAPI(context, appId, true);
//        msgApi.handleIntent(getIntent(), wxHandler);
        msgApi.registerApp(appId);
        PayReq request = new PayReq();
        request.appId = appId;
        request.partnerId = JsonHandle.getString(json, "partnerid");
        request.prepayId = JsonHandle.getString(json, "prepayid");
        request.packageValue = "Sign=WXPay";
        request.nonceStr = JsonHandle.getString(json, "noncestr");
        request.timeStamp = timeStamp;
        request.sign = getSign(json, timeStamp);
//        request.sign = JsonHandle.getString(json, "sign");

        Log.d("", getSign(json, timeStamp));

        if (msgApi.isWXAppInstalled()) {
            msgApi.sendReq(request);
        } else {
            MessageHandler.showToast(context, "请先安装微信");
        }
    }

    private String getSign(JSONObject json, String t) {
        String id = "appid=" + appId;
        String partnerId = "partnerid=" + JsonHandle.getString(json, "partnerid");
        String prepayId = "prepayid=" + JsonHandle.getString(json, "prepayid");
        String packageValue = "package=" + "Sign=WXPay";
        String nonceStr = "noncestr=" + JsonHandle.getString(json, "noncestr");
        String timeStamp = "timestamp=" + t;

        String stringA = id + "&" + nonceStr + "&" + packageValue + "&" + partnerId + "&" + prepayId + "&" + timeStamp;
        String stringSignTemp = stringA + "&key=u1I8ujsUW7uqjs91kxnLWqi8Olskx7q2";
//        String stringSignTemp = stringA + "&key=b500fd6c3855f6efaed3c8a624bf1c13";
        Log.d("", stringSignTemp);
        return MD5.getMessageDigest(stringSignTemp.getBytes()).toUpperCase();
    }

    private IWXAPIEventHandler wxHandler = new IWXAPIEventHandler() {

        @Override
        public void onReq(BaseReq baseReq) {
            Log.d("", "wxHandler onPayFinish, errStr = " + 100000);
        }

        @Override
        public void onResp(BaseResp baseResp) {
            Log.d("", "wxHandler onPayFinish, errCode = " + baseResp.errCode);
            Log.d("", "wxHandler onPayFinish, errStr = " + baseResp.errStr);
            switch (baseResp.errCode) {
                case 1:
                    MessageHandler.showToast(context, "支付成功");
                    close();
                    break;
                default:
                    MessageHandler.showToast(context, "支付失败 ：" + baseResp.errStr);
                    break;
            }
        }
    };

    private void close() {
        Intent i = new Intent();
        i.putExtra(Result_Key, true);
        setResult(Result_Code, i);
        finish();
    }

}
