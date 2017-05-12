package com.cn.ispanish.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.VideoMaxTextAdapter;
import com.cn.ispanish.adapters.VideoTextAdapter;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.views.InsideGridView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 17/4/18.
 */
public class VideoListFragment extends BaseFragment {

    static int MAX = 12;

    @ViewInject(R.id.videoPlayContent_videoList_dataList)
    private ListView dataList;

    private VideoInfo info;
    private List<VideoInfo> videoList;
    private VideoMaxTextAdapter.OnVideoListener listener;

    public VideoListFragment() {

    }

    public VideoListFragment(VideoInfo info, List<VideoInfo> videoList, VideoMaxTextAdapter.OnVideoListener listener) {
        this.info = info;
        this.videoList = videoList;
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_video_paly_content_video_list,
                container, false);
        ViewUtils.inject(this, contactsLayout);

//        dataList.addHeaderView(new HeadView(context, info.getName(), videoList));
        setDataList();

        return contactsLayout;
    }

    private void setDataList() {
        if (dataList == null || videoList == null || info == null) {
            return;
        }

        dataList.setAdapter(new VideoMaxTextAdapter(context, videoList, info.getCourseid(), listener));
    }

    private void setDataInGrid(int n) {
        List<VideoInfo> list = new ArrayList<>(MAX);
        for (int i = 0; i < MAX; i++) {
            try {
                int j = n * MAX + i;
                list.add(videoList.get(j));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        dataList.setAdapter(new VideoMaxTextAdapter(context, list, info.getCourseid(), listener));
    }

    class HeadView extends LinearLayout {

        View view;
        LayoutInflater inflater;

        InsideGridView blockGrid;
        TextView titleText;

        public HeadView(Context context, String title, List<VideoInfo> videoList) {
            super(context);

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.layout_video_paly_content_video_list_head, null);

            blockGrid = (InsideGridView) view.findViewById(R.id.videoPlayContent_videoList_hreadView_blockGrid);
            titleText = (TextView) view.findViewById(R.id.videoPlayContent_videoList_hreadView_titleText);

            setData(title, videoList);

            addView(view);
        }

        private void setData(String title, List<VideoInfo> videoList) {
            titleText.setText(title);
            blockGrid.setAdapter(new HeadAdapter(videoList));
        }
    }

    class HeadAdapter extends BaseAdapter {

        int p = 8;

        int now = 0;
        int sum = 0;

        List<VideoInfo> videoList;

        HeadAdapter(List<VideoInfo> videoList) {
            this.videoList = videoList;
            sum = videoList.size();
        }

        @Override
        public int getCount() {

            if (sum % MAX == 0) {
                return sum / MAX;
            }
            return sum / MAX + 1;

        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int num, View view, ViewGroup viewGroup) {
            final int i = num;
            String text = String.valueOf(i * MAX + 1) + " - " + String.valueOf((i + 1) * MAX > sum ? sum : (i + 1) * MAX);

            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextSize(12);
            textView.setPadding(p, p, p, p);
            textView.setGravity(Gravity.CENTER);

            if (now != i) {
                textView.setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
                textView.setBackgroundResource(R.drawable.gray_transparent_rounded_5);
            } else {
                textView.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
                textView.setBackgroundResource(R.drawable.blue_transparent_rounded_5);
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    now = i;
                    notifyDataSetChanged();
                    setDataInGrid(i);
                }
            });

            return textView;
        }
    }

}
