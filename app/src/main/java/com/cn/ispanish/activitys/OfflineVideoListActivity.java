package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.OfflineVideoAdapter;
import com.cn.ispanish.adapters.VideoHistoryAdapter;
import com.cn.ispanish.adapters.VideoTextAdapter;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.dao.DBHandler;
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
 * Created by Hua on 17/3/23.
 */
public class OfflineVideoListActivity extends BaseActivity {

    public final static String ID = "c_id";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.offlineVideoList_dataList)
    private ListView dataList;

    private PolyvDBservice service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_video_list);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("视频");
        service = new PolyvDBservice(context);

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        initData(b.getString(ID));

    }

    private void initData(String id) {
        try {
//            List<VideoInfo> list = DBHandler.getDbUtils(context).findAll(Selector.from(VideoInfo.class).where(
//                    "is_download", "=", true).and("course_id", "=", id));

            List<VideoDownloadInfo> list = service.getDownloadFilesForCourseid(id);
            if (list != null) {
                setDataList(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setDataList(List<VideoDownloadInfo> list) {
        List<VideoDownloadInfo> dataArray = new ArrayList<>();
        for (VideoDownloadInfo obj : list) {
            if (obj.isComplete()) {
                dataArray.add(obj);
            }
        }
        dataList.setAdapter(new OfflineVideoAdapter(context, dataArray));

    }

}
