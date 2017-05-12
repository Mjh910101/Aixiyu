package com.cn.ispanish.adapters;

import android.Manifest;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cn.ispanish.R;
import com.cn.ispanish.box.VideoDownloadInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.video.dao.PolyvDBservice;
import com.cn.ispanish.video.server.PolyvDLNotificationService;
import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloader;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;
import com.easefun.polyvsdk.PolyvDownloaderManager;
import com.easefun.polyvsdk.Video;

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
public class DownloadVideoListAdapter extends BaseAdapter {

    private static final String TAG = "PolyvDownloadListAdapter";

    private static final int REFRESH_PROGRESS = 1;
    private static final int SUCCESS = 2;
    private static final int FAILURE = 3;

    private LinkedList<VideoDownloadInfo> data;
    private ArrayList<MyDownloadListener> listener;
    private Context context;
    private LayoutInflater inflater;
    private PolyvDBservice service;
    private ViewHolder holder;

    private PolyvDownloader downloader;

    private ListView listView;

    private static final int NO_WRITE_PERMISSION = 12;
    private PolyvDLNotificationService notificationService;
    private ServiceConnection serconn;
    // 每个id的progress
    private SparseIntArray id_progress = new SparseIntArray();
    // 完成任务的key集合(key=vid+"_"+bit)
    private List<String> finishKeys;

    public ServiceConnection getSerConn() {
        return serconn;
    }

    public DownloadVideoListAdapter(Context context, LinkedList<VideoDownloadInfo> data, ListView listView) {
        serconn = PolyvDLNotificationService.bindDownloadService(context, new PolyvDLNotificationService.BindListener() {

            @Override
            public void bindSuccess(PolyvDLNotificationService downloadService) {
                notificationService = downloadService;
            }
        });
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.service = new PolyvDBservice(context);
        listener = new ArrayList<MyDownloadListener>();
        finishKeys = new ArrayList<String>();
        this.listView = listView;
        initDownloaders();
    }

    private class MyDownloadListener implements PolyvDownloadProgressListener {
        private int position;
        private VideoDownloadInfo info;
        private long total;
        private int id;

        public MyDownloadListener(int position, VideoDownloadInfo info) {
            this.position = position;
            this.info = info;
            this.id = PolyvDLNotificationService.getId(info.getVid(), info.getBitrate(), info.getSpeed());
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onDownloadSuccess() {
            addFinishKeyToList(info.getVid(), info.getBitrate(), info.getSpeed());
            if (notificationService != null)
                notificationService.updateFinishNF(id);
            Message msg = handler.obtainMessage();
            msg.arg1 = position;
            msg.what = SUCCESS;
            service.updatePercent(info, total, total);
            handler.sendMessage(msg);
        }

        @Override
        public void onDownloadFail(PolyvDownloaderErrorReason errorReason) {
            if (notificationService != null)
                notificationService.updateErrorNF(id, false);
            Message msg = handler.obtainMessage();
            msg.arg1 = position;
            msg.what = FAILURE;
            msg.obj = errorReason.getType();
            handler.sendMessage(msg);
        }

        @Override
        public void onDownload(long count, long total) {
            int progress = (int) (count * 100 / total);
            id_progress.put(id, progress);
            if (notificationService != null)
                notificationService.updateDownloadingNF(id, progress, false);
            this.total = total;
            Message msg = handler.obtainMessage();
            msg.arg1 = position;
            Bundle bundle = new Bundle();
            bundle.putLong("count", count);
            bundle.putLong("total", total);
            msg.setData(bundle);
            msg.what = REFRESH_PROGRESS;
            service.updatePercent(info, count, total);
            handler.sendMessage(msg);
        }
    }

    private void initDownloaders() {
        for (int i = 0; i < data.size(); i++) {
            final VideoDownloadInfo info = data.get(i);
            final String _vid = info.getVid();
            final int p = i;
            downloader = PolyvDownloaderManager.getPolyvDownloader(_vid, info.getBitrate(),
                    Video.HlsSpeedType.getHlsSpeedType(info.getSpeed()));
            MyDownloadListener downloadListener = new MyDownloadListener(p, info);
            listener.add(downloadListener);
            downloader.setPolyvDownloadProressListener(downloadListener);
        }
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup group) {
        final int i = position;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_download_video_item, null);
            holder = new ViewHolder();
            holder.pic = (ImageView) convertView.findViewById(R.id.downloadVideoItem_pic);
            holder.titleText = (TextView) convertView.findViewById(R.id.downloadVideoItem_titleText);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.downloadVideoItem_progressBar);
            holder.tv_rate = (TextView) convertView.findViewById(R.id.downloadVideoItem_rate);
            holder.dataLayout = (RelativeLayout) convertView.findViewById(R.id.downloadVideoItem_dataLayout);
            holder.scroll = (HorizontalScrollView) convertView.findViewById(R.id.downloadVideoItem_scroll);
            holder.deleteButton = (TextView) convertView.findViewById(R.id.downloadVideoItem_deleteButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        data = service.getDownloadFiles();
        final VideoDownloadInfo info = data.get(position);
        String duration = info.getDuration();
        long filesize = info.getFilesize();
        long percent = info.getPercent();
        long total = info.getTotal();
        initScroll(convertView, holder);
        setVideoPic(holder.pic, info.getPic());
        holder.titleText.setText(info.getTitle());
        holder.progressBar.setTag("" + position);
        holder.progressBar.setMax((int) total);
        holder.progressBar.setProgress((int) percent);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.dataLayout.getLayoutParams();
        lp.width = WinHandler.getWinWidth(context);

        // 初始化progress
        int id = PolyvDLNotificationService.getId(info.getVid(), info.getBitrate(), info.getSpeed());
        int progress = 0;
        if (total != 0)
            progress = (int) (percent * 100 / total);
        if (id_progress.get(id, -1) == -1)
            id_progress.put(id, progress);

        if (total != 0) {
            holder.tv_rate.setText(percent * 100 / total + "%");
        } else {
            holder.tv_rate.setText(0 + "%");
        }

        if (total != 0 && total == percent) {
            addFinishKeyToList(info.getVid(), info.getBitrate(), info.getSpeed());// 已经完成的任务，把其key放到集合中，当下载全部的时候可以不让其开始
        } else {
            if (!PolyvDownloaderManager.getPolyvDownloader(info.getVid(), info.getBitrate(),
                    Video.HlsSpeedType.getHlsSpeedType(info.getSpeed())).isDownloading()) {
                holder.tv_rate.setText("暂停中");
            }
        }

        holder.dataLayout.setOnClickListener(new DownloadListener(info.getVid(), info.getSpeed(), info.getBitrate(), convertView, info.getTitle()));
        holder.deleteButton.setOnClickListener(new DeleteListener(info, position));

        return convertView;
    }

    private void initScroll(View convertView, final ViewHolder holder) {
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

    private void setVideoPic(ImageView imageView, String images) {
        //250:140
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        DownloadImageLoader.loadImage(imageView, images, 5);
    }

    // 把完成的任务加入到key集合中
    private void addFinishKeyToList(String vid, int bit, String speed) {
        String key = vid + "_" + bit + "_" + speed;
        if (!finishKeys.contains(key))
            finishKeys.add(key);
    }

    // 从集合中移除完成的任务key
    private void removeFinishKeyToList(String vid, int bit, String speed) {
        String key = vid + "_" + bit + "_" + speed;
        if (finishKeys.contains(key))
            finishKeys.remove(key);
    }

    public boolean downloadAllFile() {
        if (!hasPermission())
            return false;
        if (notificationService != null)
            notificationService.updateUnfinishedNF(data, finishKeys);
        PolyvDownloaderManager.startUnfinished(finishKeys);
        return true;
    }

    public void updateAllButton(boolean isStop) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            TextView rate = (TextView) listView.getChildAt(i).findViewById(R.id.downloadVideoItem_rate);
            if (!isStop) {
                rate.setText("暂停中");
            } else {
                rate.setText("0%");
            }
        }
    }

    private class ViewHolder {
        ImageView pic;
        TextView titleText, tv_rate, deleteButton;
        //        TextView tv_duration, tv_filesize;
        ProgressBar progressBar;
        RelativeLayout dataLayout;
        HorizontalScrollView scroll;
//        Button btn_download, btn_delete;
    }

    // 判断是否有写入sd卡的权限
    private boolean hasPermission() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，无法开始下载
            Message msg = handler.obtainMessage();
            msg.arg2 = NO_WRITE_PERMISSION;
            handler.sendMessage(msg);
            return false;
        }
        return true;
    }

    class DownloadListener implements View.OnClickListener {
        private final String vid;
        private final int bitRate;
        private final String speed;
        private View view;
        // 视频的标题
        private String title;

        public DownloadListener(String vid, String speed, int bitRate, View view, String title) {
            this.vid = vid;
            this.speed = speed;
            this.bitRate = bitRate;
            this.view = view;
            this.title = title;
        }

        @Override
        public void onClick(View v) {
            if (!hasPermission())
                return;
            TextView rate = (TextView) view.findViewById(R.id.downloadVideoItem_rate);
            int id = PolyvDLNotificationService.getId(vid, bitRate, speed);
            if (rate.getText().equals("暂停中")) {
                rate.setText("0%");
                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitRate,
                        Video.HlsSpeedType.getHlsSpeedType(speed));
                // 先执行
                if (notificationService != null) {
                    notificationService.updateStartNF(id, vid, bitRate, speed, title, id_progress.get(id));
                }
                if (downloader != null) {
                    downloader.start();
                }
            } else if (rate.getText().equals("100%")) {

            } else {
                rate.setText("暂停中");
                PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(vid, bitRate,
                        Video.HlsSpeedType.getHlsSpeedType(speed));
                // 先执行
                if (notificationService != null) {
                    notificationService.updatePauseNF(id);
                }
                if (downloader != null) {
                    downloader.stop();
                }
            }
        }

    }

    class DeleteListener implements View.OnClickListener {
        private VideoDownloadInfo info;
        private int position;

        public DeleteListener(VideoDownloadInfo info, int position) {
            this.info = info;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            removeFinishKeyToList(info.getVid(), info.getBitrate(), info.getSpeed());
            PolyvDownloader downloader = PolyvDownloaderManager.getPolyvDownloader(info.getVid(), info.getBitrate(),
                    Video.HlsSpeedType.getHlsSpeedType(info.getSpeed()));
            PolyvDownloaderManager.clearPolyvDownload(info.getVid(), info.getBitrate());
            if (downloader != null) {
                downloader.deleteVideo(info.getVid(), info.getBitrate(),
                        Video.HlsSpeedType.getHlsSpeedType(info.getSpeed()));
            }
            int id = PolyvDLNotificationService.getId(info.getVid(), info.getBitrate(), info.getSpeed());
            if (notificationService != null) {
                notificationService.updateDeleteNF(id);
            }
            service.deleteDownloadFile(info);
            data.remove(position);
            listener.remove(position);
            for (int i = 0; i < listener.size(); i++) {
                listener.get(i).setPosition(i);
            }
            notifyDataSetChanged();
        }
    }

    public void stopAll() {
        if (notificationService != null)
            notificationService.updateAllPauseNF(data);
        PolyvDownloaderManager.stopAll();
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg2 == NO_WRITE_PERMISSION) {
                showToast(context, "权限被拒绝，无法开始下载");
                return;
            }
            int position = (int) msg.arg1;
            int offset = position - listView.getFirstVisiblePosition();
            int endset = position - listView.getLastVisiblePosition();
            if (offset < 0 || endset > 0)
                return;
            View view = (View) listView.getChildAt(offset);
            switch (msg.what) {
                case REFRESH_PROGRESS:
                    long downloaded = msg.getData().getLong("count");
                    long total = msg.getData().getLong("total");
                    ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.downloadVideoItem_progressBar);
                    progressBar.setMax((int) total);
                    progressBar.setProgress((int) downloaded);
                    TextView tv = (TextView) view.findViewById(R.id.downloadVideoItem_rate);
                    tv.setText(downloaded * 100 / total + "%");
                    break;
                case SUCCESS:
                    break;
                case FAILURE:
                    PolyvDownloaderErrorReason.ErrorType errorType = (PolyvDownloaderErrorReason.ErrorType) msg.obj;
                    showMessageTose(errorType, position);
                    break;
            }
        }

        ;
    };

    private void showMessageTose(PolyvDownloaderErrorReason.ErrorType errorType, int position) {
        switch (errorType) {
            case VID_IS_NULL:
                showToast(context, "第" + (position + 1) + "个任务vid错误，请重试");
                break;
            case NOT_PERMISSION:
                showToast(context, "第" + (position + 1) + "个任务没有权限访问视频,请重试");
                break;
            case RUNTIME_EXCEPTION:
                showToast(context, "第" + (position + 1) + "个任务运行时异常，请重试");
                break;
            case VIDEO_STATUS_ERROR:
                showToast(context, "第" + (position + 1) + "个任务视频状态错误，请重试");
                break;
            case M3U8_NOT_DATA:
                showToast(context, "第" + (position + 1) + "个任务m3u8没有数据，请重试");
                break;
            case QUESTION_NOT_DATA:
                showToast(context, "第" + (position + 1) + "个任务问答没有数据，请重试");
                break;
            case MULTIMEDIA_LIST_EMPTY:
                showToast(context, "第" + (position + 1) + "个任务ts下载列表为空，请重试");
                break;
            case CAN_NOT_MKDIR:
                showToast(context, "第" + (position + 1) + "个任务不能创建文件夹，请重试");
                break;
            case DOWNLOAD_TS_ERROR:
                showToast(context, "第" + (position + 1) + "个任务下载ts错误，请重试");
                break;
            case MULTIMEDIA_EMPTY:
                showToast(context, "第" + (position + 1) + "个任务mp4下载地址为空，请重试");
                break;
            case NOT_CREATE_DIR:
                showToast(context, "第" + (position + 1) + "个任务不能创建目录，请重试");
                break;
            case VIDEO_LOAD_FAILURE:
                showToast(context, "第" + (position + 1) + "个任务Video加载失败，请重试");
                break;
            case VIDEO_NULL:
                showToast(context, "第" + (position + 1) + "个任务video取得为null，请重试");
                break;
            case DIR_SPACE_LACK:
                showToast(context, "第" + (position + 1) + "个任务存储空间不足，请清除存储空间重试");
                break;
            case DOWNLOAD_DIR_IS_NUll:
                showToast(context, "第" + (position + 1) + "个任务下载文件夹未设置");
                break;
            case HLS_15X_URL_ERROR:
                showToast(context, "第" + (position + 1) + "个任务1.5倍速下载地址错误");
                break;
            case HLS_SPEED_TYPE_IS_NULL:
                showToast(context, "第" + (position + 1) + "个任务未设置视频播放速度，请设置");
                break;
            case HLS_15X_ERROR:
                showToast(context, "第" + (position + 1) + "个任务不支持1.5倍速下载");
                break;
            case GET_VIDEO_INFO_ERROR:
                showToast(context, "第" + (position + 1) + "个任务获取视频信息错误");
                break;
            default:
                break;
        }
    }

    private void showToast(Context context, String msg) {
        MessageHandler.showToast(context, msg);
    }

}
