package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.PaperActivity;
import com.cn.ispanish.box.Banner;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.banner.BannerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
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
 * Created by Hua on 17/5/8.
 */
public class PaperFragment extends BaseFragment {

    @ViewInject(R.id.mainPaper_bannerLayout)
    private RelativeLayout bannerLayout;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private BannerView bannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_paper,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();

        return contactsLayout;
    }

    private void initActivity() {
        downloadBanner();
    }

    private void downloadBanner() {
        progress.setVisibility(View.VISIBLE);
        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.GET, UrlHandle.getBanner(),
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
                            JSONArray dataArray = JsonHandle.getArray(json, "data");
                            initBanner(dataArray);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initBanner(JSONArray array) {
        if (array == null) {
            return;
        }
        List<Banner> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Banner(JsonHandle.getJSON(array, i)));
        }
        bannerView = new BannerView(context, list);
        bannerLayout.addView(bannerView);
    }

    @OnClick(R.id.mainPaper_orderPaperButton)
    public void onOrderPaper(View view) {
        PassagewayHandler.jumpActivity(context, PaperActivity.class);
    }

}
