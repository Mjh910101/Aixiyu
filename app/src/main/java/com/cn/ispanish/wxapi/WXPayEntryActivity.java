package com.cn.ispanish.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.BaseActivity;
import com.cn.ispanish.handlers.MessageHandler;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    public final static int WEIXIN_RESULT = 10021;

    public final static String Result_Key = "result_code";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, "wx8d4dd63a75e1154b");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
        Log.d(TAG, "onPayFinish, errCode = " + 100000);
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
        Intent i = new Intent();
        switch (resp.errCode) {
            case 0:
                MessageHandler.showToast(context, "支付成功");
                i.putExtra(Result_Key, true);
                break;
            case -2:
                MessageHandler.showToast(context, "已取消支付");
                i.putExtra(Result_Key, false);
                break;
            default:
                MessageHandler.showToast(context, "支付失败 : " + resp.errStr);
                i.putExtra(Result_Key, false);
                break;
        }
        setResult(WEIXIN_RESULT,i);
        finish();
    }
}