package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.DownloadVideoListActivity;
import com.cn.ispanish.adapters.IndexBlockListAdapter;
import com.cn.ispanish.adapters.OfflineIndexBlockListAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.dao.DBHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
public class OfflineFragment extends BaseFragment {

    @ViewInject(R.id.title_backIcon)
    private ImageView bickIcon;
    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.offline_dataList)
    private ListView dataList;

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

        initData();
    }


    private void initLayout() {
        bickIcon.setVisibility(View.GONE);
        titleText.setText("离线");

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
                isCompete(dataArray,obj,videoList);
            }
            dataList.setAdapter(new OfflineIndexBlockListAdapter(context, dataArray));
        }
    }

    private boolean isCompete(List<IndexBlock> dataArray, IndexBlock obj, List<VideoDownloadInfo> videoList) {
        for (VideoDownloadInfo video : videoList) {
            Log.e("VideoDownloadInfo",video.toString());
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

}
