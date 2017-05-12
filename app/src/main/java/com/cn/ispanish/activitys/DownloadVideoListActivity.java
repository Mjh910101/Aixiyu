package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.DownloadVideoListAdapter;
import com.cn.ispanish.adapters.IndexBlockListAdapter;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.LinkedList;
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
 * Created by Hua on 17/3/21.
 */
public class DownloadVideoListActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.title_toolButton)
    private TextView toolButton;
    @ViewInject(R.id.downloaList_dataList)
    private ListView dataList;
    @ViewInject(R.id.downloaList_emptyView)
    private TextView emptyView;

    private LinkedList<VideoDownloadInfo> infos;

    private PolyvDBservice service;

    private boolean isStop = false;
    private DownloadVideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_video_list);

        ViewUtils.inject(this);
        initActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(adapter.getSerConn());
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.title_toolButton)
    public void onTool(View view) {
        if (adapter == null) {
            return;
        }
        if (!isStop) {
            toolButton.setText("暂停全部");
            boolean issuccess = adapter.downloadAllFile();
            if (issuccess) {
                adapter.updateAllButton(true);
            }
            isStop = !isStop;
        } else {
            toolButton.setText("下载全部");
            adapter.stopAll();
            adapter.updateAllButton(false);
            isStop = !isStop;
        }
    }

    private void initActivity() {
        titleText.setText("下载列表");
        toolButton.setText("全部下载");
        toolButton.setVisibility(View.VISIBLE);

        dataList.setEmptyView(emptyView);

        initDate();

    }

    private void initDate() {
        service = new PolyvDBservice(context);
        infos = service.getDownloadFiles();
        if (infos != null) {
            setDataList(infos);
        }
    }

    private void setDataList(LinkedList<VideoDownloadInfo> list) {
        LinkedList<VideoDownloadInfo> infoList = new LinkedList<>();
        for (VideoDownloadInfo info : list) {
            Log.i("video", "******* : "+info.toString());
            if (info.getTotal() == 0 || info.getTotal() != info.getPercent()) {
                Log.i("video", "@@@@@@ : "+info.toString());
                infoList.add(info);
            }
        }

        adapter = new DownloadVideoListAdapter(context, infoList, dataList);
        dataList.setAdapter(adapter);
    }

}
