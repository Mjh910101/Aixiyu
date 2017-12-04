package com.cn.ispanish.activitys.play;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.cn.ispanish.R;
import com.cn.ispanish.activitys.BaseActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dialog.MessageDialog;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.interfaces.CallbackForString;
import com.cn.ispanish.topup.weixin.MD5;
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

import java.text.DecimalFormat;
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
public class PlayPaperOrderActivity extends BaseActivity {

    public final static String TOPUP_MONEY = "money";
    public final static String TOPUP_TITLE = "title";
    public final static String TOPUP_BLOCK = "kid";
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
    @ViewInject(R.id.playOrder_confirm_couponButton)
    private RelativeLayout couponButton;
    @ViewInject(R.id.playOrder_confirm_balanceButton)
    private RelativeLayout balanceButton;
    @ViewInject(R.id.playOrder_confirm_balanceLine)
    private View balanceLine;
    @ViewInject(R.id.playOrder_confirm_couponLine)
    private View couponLine;
    @ViewInject(R.id.playOrder_confirm_balanceManey)
    private TextView balanceManey;
    @ViewInject(R.id.playOrder_confirm_balanceRadio)
    private RadioButton balanceRadio;
    @ViewInject(R.id.playOrder_confirm_payLayout)
    private LinearLayout payLayout;
    @ViewInject(R.id.playOrder_confirm_topUpBtm)
    private TextView topUpBtm;

    private String money, kid;
    private double topUpMoney, balanceMoney, priceMoney;

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

    @OnClick({R.id.playOrder_confirm_zhifubaoLayout, R.id.playOrder_confirm_weixinLayout, R.id.playOrder_confirm_balanceButton})
    public void onPlayType(View view) {
        switch (view.getId()) {
            case R.id.playOrder_confirm_zhifubaoLayout:
                setBalanceRadio(false);
                setZhiFuBaoRadio();
                break;
            case R.id.playOrder_confirm_weixinLayout:
                setBalanceRadio(false);
                setWeixinRadio();
                break;
            case R.id.playOrder_confirm_balanceButton:
                initRadio();
                setBalanceRadio();
                break;
        }
        setBalanceButton();
    }

    @OnClick(R.id.playOrder_confirm_topUpBtm)
    public void onPlay(View view) {
        if (isBalance() && isMeet()) {
            buy4Balance();
        } else {
            if (zhifubaoRadio.isChecked()) {
                downloadOrderIdToZhifubao();
            } else if (weixinRadio.isChecked()) {
                downloadOrderIdToWeixin();
            }
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

        kid = b.getString(TOPUP_BLOCK);
        money = b.getString(TOPUP_MONEY);
        orderTitleText.setText(b.getString(TOPUP_TITLE));
        topUpManeyText.setText(money);

        couponButton.setVisibility(View.GONE);
//        balanceButton.setVisibility(View.GONE);
//        balanceLine.setVisibility(View.GONE);
        couponLine.setVisibility(View.GONE);

        try {
            topUpMoney = Double.valueOf(money);
        } catch (Exception e) {
            e.printStackTrace();
        }

        selectBalance();
    }

    private void selectBalance() {
        User.selectBalance(context, new CallbackForString() {
            @Override
            public void callback(String data) {
//                data="30000";
                balanceManey.setText("￥" + data);
                try {
                    balanceMoney = Double.valueOf(data);
                    setBalanceRadio(true);
//                    MessageHandler.showToast(context, "" + d);
                    setBalanceButton();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean isMeet() {
//        return balanceMoney + priceMoney >= topUpMoney;
        return balanceMoney >= topUpMoney;
    }

    private boolean isBalance() {
        return balanceRadio.isChecked();
    }

    private boolean isChooes() {
        return weixinRadio.isChecked() || zhifubaoRadio.isChecked();
    }

    private void setBalanceButton() {
        if (isBalance()) {
            if (isMeet()) {
                topUpBtm.setText("确定支付");
                topUpBtm.setBackgroundResource(R.drawable.orange_orange_rounded_5);
            } else {
                topUpBtm.setText("确定支付 ￥" + getTopUpMoney4Text());
                if (isChooes()) {
                    topUpBtm.setBackgroundResource(R.drawable.orange_orange_rounded_5);
                } else {
                    topUpBtm.setBackgroundResource(R.drawable.gray_gray_rounded_5);
                }
            }
        } else {
            topUpBtm.setText("确定支付 ￥" + getTopUpMoney4Text());
            if (isChooes()) {
                topUpBtm.setBackgroundResource(R.drawable.orange_orange_rounded_5);
            } else {
                topUpBtm.setBackgroundResource(R.drawable.gray_gray_rounded_5);
            }
        }
    }

    private double getTopUpMoney() {
//        if (isBalance()) {
//            return topUpMoney - (balanceMoney + priceMoney);
//        } else {
//            return topUpMoney - priceMoney;
//        }
        return topUpMoney;
    }

    private String getTopUpMoney4Text() {
        double d = getTopUpMoney();
//        d = ((int) (d * 100)) / 100;
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    private void setBalanceRadio() {
        boolean b = balanceRadio.isChecked();
        setBalanceRadio(!b);

    }

    private void setBalanceRadio(boolean b) {
        balanceRadio.setChecked(b);
        if (balanceRadio.isChecked()) {
            if (isMeet()) {
                payLayout.setVisibility(View.GONE);
            } else {
                payLayout.setVisibility(View.VISIBLE);
            }
        } else {
            payLayout.setVisibility(View.VISIBLE);
        }
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

    private void buy4Balance() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", kid);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getPointBuyPage(context), params,
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

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            if (!MessageHandler.showException(context, json)) {
                                close();
                            }
                        }
                    }
                });
    }

    private void downloadOrderIdToZhifubao() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("kid", kid);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getBankAlipay(context), params,
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
                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
                            JSONObject dataJson = JsonHandle.getJSON(json, "data");
                            if (dataJson != null) {
                                playOrderInZhufubao(JsonHandle.getString(dataJson, "indent"));
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
        final String payInfo = ZhiFuBao.getPaperOrderInfo(TextHandler.getText(orderTitleText), money, indent);

        new Thread(new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PlayPaperOrderActivity.this);
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
        params.addBodyParameter("sid", kid);
        params.addBodyParameter("yhkey", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getWxpay2Page(context), params,
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
                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
                            playOrderInWeixin(JsonHandle.getJSON(json, "data"));
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
