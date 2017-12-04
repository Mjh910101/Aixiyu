package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.VideoTitleAdapter;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.VideoTitleView;
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
 * Created by Hua on 17/4/6.
 */
public class VideoDataListActivity extends BaseActivity {

    public static final String COURSE_ID_KEY = "courseid";

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.allVideo_dataList)
    private ExpandableListView videoDataList;

    private String courseId;

    private List<VideoInfo> videoList;
    private List<VideoInfo> unFreeVideoList;
    private List<VideoInfo> freeVideoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_data_list);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    private void initActivity() {
        titleText.setText("视频");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        courseId = b.getString(COURSE_ID_KEY);

        downloadData(courseId);

    }

    private void initVideoList(JSONArray array, boolean isBuy) {
        if (array == null) {
            return;
        }
        videoList = new ArrayList<>();
        unFreeVideoList = new ArrayList<>();
        freeVideoList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            VideoInfo video = new VideoInfo(JsonHandle.getJSON(array, i));
            video.setBuy(isBuy);
            videoList.add(video);
            if (video.isFree() || video.isBuy()) {
                freeVideoList.add(video);
            } else {
                unFreeVideoList.add(video);
            }
        }

        initVideoList(freeVideoList, unFreeVideoList);

    }

    private void initVideoList(List<VideoInfo> freeList, List<VideoInfo> unFreeList) {
        videoDataList.setAdapter(new VideoListAdapter(freeList, unFreeList));
        for (int i = 0; i < 2; i++) {
            videoDataList.expandGroup(i);
        }

        videoDataList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                return true;
            }
        });
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", id);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getVideoPlay(context), params,
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
                            boolean isBuy = JsonHandle.getString(json, "buy").equals("YES");

                            initVideoList(JsonHandle.getArray(json, "splist"), isBuy);

                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    class VideoListAdapter extends BaseExpandableListAdapter {

        Map<Integer, List<VideoInfo>> dataMap;
        int p;

        VideoListAdapter(List<VideoInfo> freeList, List<VideoInfo> unFreeList) {
            dataMap = new HashMap<>();
            dataMap.put(0, freeList);
            dataMap.put(1, unFreeList);

            p = WinHandler.dipToPx(context, 6);
        }

        @Override
        public int getGroupCount() {
            return dataMap.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return dataMap.get(i).size();
        }

        @Override
        public Object getGroup(int i) {
            return dataMap.get(i);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return dataMap.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition * 10000;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return groupPosition * 10000 + childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(context);
            textView.setTextSize(14);
            textView.setPadding(p, p, p, p);
            if (groupPosition == 0) {
                textView.setText("可观看视频");
                textView.setTextColor(ColorHandle.getColorForID(context, R.color.module_green));
            } else {
                textView.setText("未拥有的视频");
                textView.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
            }
            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
            return new VideoTitleView(context, dataMap.get(groupPosition).get(childPosition), courseId);
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }
    }

}
