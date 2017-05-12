package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.VideoPlayActivity;
import com.cn.ispanish.box.VideoHistory;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.VideoTitleView;

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
 * Created by Hua on 17/3/10.
 */
public class VideoHistoryAdapter extends BaseAdapter {

    Context context;
    List<VideoHistory> itemList;

    public VideoHistoryAdapter(Context context, List<VideoHistory> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_video_history_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VideoHistory history = itemList.get(i);
        setData(holder, history);
        setOnClick(view, history);
        return view;
    }

    private void setOnClick(View view, final VideoHistory obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString(VideoPlayActivity.VID_KEY, obj.getVid());
                b.putString(VideoPlayActivity.TITLE_KEY, obj.getTitle());
                b.putString(VideoPlayActivity.COURSE_ID_KEY, obj.getCourseId());
                PassagewayHandler.jumpActivity(context, VideoPlayActivity.class, b);
            }
        });
    }

    private void setData(Holder holder, VideoHistory history) {
        setVideoPic(holder.videoPic, history.getImageUrl());

        holder.titleText.setText(history.getTitle());
        holder.lastTime.setText(history.getLastTimeForText());

    }


    private void setVideoPic(ImageView imageView, String images) {
        //250:140
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));

        DownloadImageLoader.loadImage(imageView, images, 5);
    }

    class Holder {

        ImageView videoPic;
        TextView titleText;
        TextView lastTime;

        Holder(View view) {
            videoPic = (ImageView) view.findViewById(R.id.videoHistory_videoPic);
            titleText = (TextView) view.findViewById(R.id.videoHistory_titleText);
            lastTime = (TextView) view.findViewById(R.id.videoHistory_lastTimeText);
        }

    }
}
