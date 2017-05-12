package com.cn.ispanish.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.IndexBlockCollectionAdapter;
import com.cn.ispanish.box.Banner;
import com.cn.ispanish.box.IndexBlockCollection;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.banner.BannerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 17/3/6.
 */
public class IndexFragment extends BaseFragment {

    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.mainIndex_dataList)
    private ListView dataList;
    @ViewInject(R.id.mainIndex_swipeRefresh)
    private SwipeRefreshLayout swipeRefresh;

    private BannerView headBannerView;
    private IndexBlockCollectionAdapter indexBlockCollectionAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_index,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();

        return contactsLayout;
    }

    private void initActivity() {
        initRefresh();
        setOnRefreshListener();
        downloadBanner();
        downloadData();

    }

    private void initRefresh(){
        swipeRefresh.setColorSchemeColors(Color.BLUE,
                Color.GREEN,
                Color.YELLOW,
                Color.RED);
        // 设置手指在屏幕下拉多少距离会触发下拉刷新
        swipeRefresh.setDistanceToTriggerSync(300);
        // 设定下拉圆圈的背景
        swipeRefresh.setProgressBackgroundColorSchemeColor(Color.WHITE);
        // 设置圆圈的大小
        swipeRefresh.setSize(SwipeRefreshLayout.DEFAULT);
    }

    private void setOnRefreshListener() {
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                downloadData();
            }
        });
    }

    private void closreProgress() {
        closeSwipeRefresh();
        progress.setVisibility(View.GONE);
    }

    private void closeSwipeRefresh() {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    private void downloadBanner() {
        progress.setVisibility(View.VISIBLE);
        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, UrlHandle.getBanner(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        closreProgress();
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
                        closreProgress();
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

        headBannerView = new BannerView(context, list);
        dataList.addHeaderView(headBannerView);
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);
        HttpUtilsBox.getHttpUtil().send(HttpMethod.GET, UrlHandle.getIndexblock(),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        closreProgress();
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            JSONArray dataArray = JsonHandle.getArray(json, "data");
                            initCollectionAdapter(dataArray);
                        }

                        closreProgress();
                    }
                });
    }

    private void initCollectionAdapter(JSONArray array) {
        if (array == null) {
            return;
        }
        List<IndexBlockCollection> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new IndexBlockCollection(JsonHandle.getJSON(array, i)));
        }
        indexBlockCollectionAdapter = new IndexBlockCollectionAdapter(context);
        dataList.setAdapter(indexBlockCollectionAdapter);

        indexBlockCollectionAdapter.addItem(list);
    }

}
