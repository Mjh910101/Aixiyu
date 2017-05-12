package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.VideoPlayActivity;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.PassagewayHandler;
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
public class VideoTextAdapter extends BaseAdapter {

    Context context;
    List<VideoInfo> itemList;
    String courseId;
    OnVideoListener listener;

    public interface OnVideoListener {
        void callback(VideoInfo video);
    }

    public VideoTextAdapter(Context context, List<VideoInfo> itemList, String courseId, OnVideoListener listener) {
        this.context = context;
        this.itemList = itemList;
        this.courseId = courseId;
        this.listener = listener;
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
            view = View.inflate(context, R.layout.layout_video_text_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VideoInfo obj = itemList.get(i);
        setData(holder, obj);
        setOnClick(view, obj);

        return view;
    }

    private void setOnClick(View view, final VideoInfo obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obj.isFree() || obj.isBuy()) {
//                    Bundle b = new Bundle();
//                    b.putString(VideoPlayActivity.VID_KEY, obj.getVid());
//                    b.putString(VideoPlayActivity.TITLE_KEY, obj.getName());
//                    b.putString(VideoPlayActivity.COURSE_ID_KEY, courseId);
//                    PassagewayHandler.jumpToActivity(context, VideoPlayActivity.class, b);
                    if (listener != null) {
                        listener.callback(obj);
                    }
                }
            }
        });
    }

    private void setData(Holder holder, VideoInfo obj) {
        holder.titleText.setText(obj.getName());
        if (obj.isFree() || obj.isBuy()) {
            holder.titleText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_36));
            if (obj.isFree()) {
                holder.freeIcon.setVisibility(View.VISIBLE);
            } else {
                holder.freeIcon.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.titleText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
            holder.freeIcon.setVisibility(View.INVISIBLE);
        }
    }

    class Holder {

        TextView titleText;
        TextView freeIcon;

        Holder(View view) {
            titleText = (TextView) view.findViewById(R.id.videoText_tieleText);
            freeIcon = (TextView) view.findViewById(R.id.videoText_freeIcon);
        }

    }
}
