package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.VideoPlayActivity;
import com.cn.ispanish.adapters.VideoHistoryAdapter;
import com.cn.ispanish.box.LivePlayBack;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoHistory;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 2017/9/29.
 */

public class PlayBackFragment extends BaseFragment {

    @ViewInject(R.id.playBack_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.playBack_teacherName)
    private TextView teacherName;
    @ViewInject(R.id.playBack_noDataText)
    private TextView noDataText;
    @ViewInject(R.id.playBack_dataList)
    private ListView dataList;

    private OnClickCallback onClickCallback;

    User teacher;
    List<LivePlayBack> playBackList;

    public PlayBackFragment(User teacher, List<LivePlayBack> playBackList) {
        this.teacher = teacher;
        this.playBackList = playBackList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_play_back,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initData();

        return contactsLayout;
    }

    private void initData() {
        DownloadImageLoader.loadImage(teacherPic, "http://www.ispanish.cn" + teacher.getPortrait(), WinHandler.dipToPx(context, 15));
        teacherName.setText(teacher.getName());

        dataList.setEmptyView(noDataText);
        dataList.setAdapter(new PlayBackAdapter());
    }

    public void setOnClick(OnClickCallback clickCallback) {
        this.onClickCallback = clickCallback;
    }

    public interface OnClickCallback {
        void callback(String vid);
    }

    class PlayBackAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return playBackList.size();
        }

        @Override
        public Object getItem(int i) {
            return playBackList.get(i);
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

            LivePlayBack history = playBackList.get(i);
            setData(holder, history);
            setOnClick(view, history);
            return view;
        }

        private void setOnClick(View view, final LivePlayBack history) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onClickCallback != null) {
                        onClickCallback.callback(history.getVid());
                    }
                }
            });
        }


        private void setData(Holder holder, LivePlayBack history) {
            setVideoPic(holder.videoPic, history.getImg());

            holder.titleText.setText(history.getTitle());
            holder.lastTime.setText(history.getTime());

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
}
