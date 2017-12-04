package com.cn.ispanish.activitys.play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.BaseActivity;
import com.cn.ispanish.adapters.CouponAdapter;
import com.cn.ispanish.box.Coupon;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
 * Created by Hua on 17/8/25.
 */
public class PayCouponListActivity extends BaseActivity {

    public final static String CID = "cid";
    public final static String MONEY = "monry";
    public final static String COUPON_ID = "id";
    public final static String PRICE = "price";
    public final static int Result_Code = 22302;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.title_toolButton)
    public TextView toolButton;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.couponList_dataList)
    private ListView dataList;
    @ViewInject(R.id.couponList_noDataMessage)
    private TextView noDataMessage;

    private List<Coupon> couponList;
    private String cid, couponId;
    private double money;
    private CouponAdapter couponAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_coupon_list);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick(R.id.title_toolButton)
    public void onToll(View view) {
        if (couponAdapter != null) {
            Coupon clickCpupon = couponAdapter.getOnClickCoupon();
            onClickCoupon(clickCpupon);
        }

    }

    private void onClickCoupon(Coupon coupon) {
        if (coupon == null) {
            Intent i = new Intent();
            i.putExtra(PRICE, "0");
            i.putExtra(COUPON_ID, "");
            setResult(Result_Code, i);
            finish();
            return;
        }

        if (!coupon.isRangeSta(money)) {
            MessageHandler.showToast(context, "您所购买的课程还不满" + coupon.getRangeSta());
            return;
        }
        Intent i = new Intent();
        i.putExtra(PRICE, coupon.getPrice());
        i.putExtra(COUPON_ID, coupon.getId());
        setResult(Result_Code, i);
        finish();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("优惠券");
        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        toolButton.setText("确定");
        toolButton.setVisibility(View.VISIBLE);
        dataList.setEmptyView(noDataMessage);

        couponId = b.getString(COUPON_ID);
        cid = b.getString(CID);
        money = Double.valueOf(b.getString(MONEY));

        downloadData();

    }

    private void setDataList(JSONArray dataArray) {
        if (dataArray == null) {
            return;
        }
        couponList = new ArrayList<>();
        for (int i = 0; i < dataArray.length(); i++) {
            Coupon obj = new Coupon(JsonHandle.getJSON(dataArray, i));
            couponList.add(obj);
        }

        setDataList(couponList);
    }

    private void setDataList(List<Coupon> couponList) {
        couponAdapter = new CouponAdapter(context, couponList);
        dataList.setAdapter(couponAdapter);

        couponAdapter.setClickItem(couponId);
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("cid", cid);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getCoupon(context), params,
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
                        if (json != null) {
                            if (!MessageHandler.showException(context, json)) {
                                JSONArray dataArray = JsonHandle.getArray(json, "data");
                                setDataList(dataArray);
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }

                });
    }
}
