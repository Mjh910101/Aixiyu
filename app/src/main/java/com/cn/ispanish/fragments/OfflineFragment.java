package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.DownloadVideoListActivity;
import com.cn.ispanish.adapters.IndexBlockListAdapter;
import com.cn.ispanish.adapters.OfflineIndexBlockListAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
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
import java.util.HashMap;
import java.util.List;
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
 * Created by Hua on 17/3/6.
 */
public class OfflineFragment extends BaseFragment {

    @ViewInject(R.id.offline_dataList)
    private ListView dataList;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private PolyvDBservice service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_offline,
                container, false);
        ViewUtils.inject(this, contactsLayout);
        initLayout();
        return contactsLayout;
    }


    @Override
    public void onResume() {
        super.onResume();

        downloadData();
//        initData();
    }


    private void initLayout() {
        service = new PolyvDBservice(context);
    }

    private void initData() {
        try {
            List<IndexBlock> list = DBHandler.getDbUtils(context).findAll(Selector.from(IndexBlock.class).where(
                    "is_download", "=", true));
            setDataList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDataList(List<IndexBlock> list) {
        List<IndexBlock> dataArray = new ArrayList<>();
        if (list != null) {
            for (IndexBlock obj : list) {
                List<VideoDownloadInfo> videoList = service.getDownloadFilesForCourseid(obj.getCourseid());
                isCompete(dataArray, obj, videoList);
            }
            dataList.setAdapter(new OfflineIndexBlockListAdapter(context, dataArray));
        }
    }

    private boolean isCompete(List<IndexBlock> dataArray, IndexBlock obj, List<VideoDownloadInfo> videoList) {
        for (VideoDownloadInfo video : videoList) {
            Log.e("VideoDownloadInfo", video.toString());
            if (video.isComplete()) {
                dataArray.add(obj);
                return true;
            }
        }
        return false;
    }

    @OnClick(R.id.offline_downloadListButton)
    public void onDownloadList(View view) {
        PassagewayHandler.jumpActivity(context, DownloadVideoListActivity.class);
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getMyVedioList(context), params,
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
                            initDataForArray(JsonHandle.getArray(json, "arr"));
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataForArray(JSONArray array) {
        if (array == null) {
            return;
        }

        Map<String, IndexBlock> map = new HashMap<>();
        for (int i = 0; i < array.length(); i++) {
            IndexBlock obj = new IndexBlock(JsonHandle.getJSON(array, i));
            map.put(obj.getCourseid(), obj);
        }
        checkData(map);
    }

    private void checkData(Map<String, IndexBlock> map) {

        try {
            List<IndexBlock> offLineList = DBHandler.getDbUtils(context).findAll(Selector.from(IndexBlock.class).where(
                    "is_download", "=", true));

            List<IndexBlock> dataList = new ArrayList<>();

            for (IndexBlock obj : offLineList) {
                if (obj.isFree() || map.containsKey(obj.getCourseid())) {
                    dataList.add(obj);
                }
            }
            setDataList(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
