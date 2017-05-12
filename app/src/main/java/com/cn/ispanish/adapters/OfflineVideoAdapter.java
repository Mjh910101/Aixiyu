package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.OfflineVideoPlayActivity;
import com.cn.ispanish.activitys.VideoPlayActivity;
import com.cn.ispanish.activitys.VideoPlayContentActivity;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.box.VideoHistory;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.cn.ispanish.video.server.PolyvDLNotificationService;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;

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
public class OfflineVideoAdapter extends BaseAdapter {

    Context context;
    List<VideoDownloadInfo> itemList;
    private PolyvDBservice service;

    public OfflineVideoAdapter(Context context, List<VideoDownloadInfo> itemList) {
        this.service = new PolyvDBservice(context);
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
            view = View.inflate(context, R.layout.layout_offline_video_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VideoDownloadInfo videoInfo = itemList.get(i);
        initScroll(view, holder);
        setData(holder, videoInfo);
        setOnClick(holder.dataLayout, videoInfo);
        setOnDelete(holder.deleteButton, videoInfo, i);
        return view;
    }

    private void setOnDelete(TextView view, final VideoDownloadInfo info, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(info.getVid(), info.getBitrate(),
                        Video.HlsSpeedType.getHlsSpeedType(info.getSpeed()));
                PolyvDownloaderManager.clearPolyvDownload(info.getVid(), info.getBitrate());
                if (downloader != null) {
                    downloader.deleteVideo(info.getVid(), info.getBitrate(),
                            Video.HlsSpeedType.getHlsSpeedType(info.getSpeed()));
                }
                int id = PolyvDLNotificationService.getId(info.getVid(), info.getBitrate(), info.getSpeed());
                service.deleteDownloadFile(info);
                itemList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    private void setOnClick(View view, final VideoDownloadInfo obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle b = new Bundle();
//                b.putString(OfflineVideoPlayActivity.VID_KEY, obj.getVid());
//                b.putString(OfflineVideoPlayActivity.TITLE_KEY, obj.getTitle());
//                b.putString(OfflineVideoPlayActivity.COURSE_ID_KEY, obj.getCourseid());
//                PassagewayHandler.jumpActivity(context, OfflineVideoPlayActivity.class, b);
                Bundle b = new Bundle();
                b.putString(VideoPlayContentActivity.VID_KEY, obj.getVid());
                b.putString(VideoPlayContentActivity.TITLE_KEY, obj.getTitle());
                b.putString(VideoPlayContentActivity.COURSE_ID_KEY, obj.getCourseid());
                b.putBoolean(VideoPlayContentActivity.OFFLINE, true);
                PassagewayHandler.jumpActivity(context, VideoPlayContentActivity.class, b);
            }
        });
    }

    private void setData(Holder holder, VideoDownloadInfo videoInfo) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.dataLayout.getLayoutParams();
        lp.width = WinHandler.getWinWidth(context);

        setVideoPic(holder.videoPic, videoInfo.getPic());

        holder.titleText.setText(videoInfo.getTitle());

    }


    private void setVideoPic(ImageView imageView, String images) {
        //250:140
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));

        DownloadImageLoader.loadImage(imageView, images, 5);
    }

    private void initScroll(View convertView, final Holder holder) {
        if (holder.scroll.getScrollX() != 0) {
            holder.scroll.scrollTo(0, 0);
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = holder.scroll.getScrollX();
                        //获得操作区域的长度
                        int actionW = holder.deleteButton.getWidth();
                        //注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
                        //如果水平方向的移动值<操作区域的长度的一半,就复原
                        if (scrollX < actionW / 2) {
                            holder.scroll.smoothScrollTo(0, 0);
                        } else {//否则的话显示操作区域
                            holder.scroll.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });

    }

    class Holder {

        ImageView videoPic;
        TextView titleText;
        TextView deleteButton;
        HorizontalScrollView scroll;
        RelativeLayout dataLayout;

        Holder(View view) {
            videoPic = (ImageView) view.findViewById(R.id.offlineVideo_videoPic);
            titleText = (TextView) view.findViewById(R.id.offlineVideo_titleText);
            deleteButton = (TextView) view.findViewById(R.id.offlineVideo_deleteButton);
            scroll = (HorizontalScrollView) view.findViewById(R.id.offlineVideo_scroll);
            dataLayout = (RelativeLayout) view.findViewById(R.id.offlineVideo_dataLayout);
        }

    }
}
