package com.cn.ispanish.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.ColorHandle;

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
public class VideoMaxTextAdapter extends BaseAdapter {

    Context context;
    List<VideoInfo> itemList;
    String courseId;
    OnVideoListener listener;

    public interface OnVideoListener {
        void callback(VideoInfo video, boolean isDown);
    }

    public VideoMaxTextAdapter(Context context, List<VideoInfo> itemList, String courseId, OnVideoListener listener) {
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
            view = View.inflate(context, R.layout.layout_video_max_text_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        VideoInfo obj = itemList.get(i);
        setData(holder, obj);
        setOnClick(holder.downloadButton, obj, true);
        setOnClick(view, obj, false);

        return view;
    }

    private void setOnClick(View view, final VideoInfo obj, final boolean isDown) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obj.isFree() || obj.isBuy()) {
                    if (listener != null) {
                        listener.callback(obj, isDown);
                    }
                }
            }
        });
    }

    private void setData(Holder holder, VideoInfo obj) {
        holder.titleText.setText(obj.getName());
        if (obj.isFree() || obj.isBuy()) {
            holder.downloadButton.setVisibility(View.VISIBLE);
            holder.titleText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_36));
        } else {
            holder.titleText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
            holder.downloadButton.setVisibility(View.GONE);
        }
    }

    class Holder {

        TextView titleText;
        ImageView downloadButton;

        Holder(View view) {
            titleText = (TextView) view.findViewById(R.id.videoText_tieleText);
            downloadButton = (ImageView) view.findViewById(R.id.videoText_downloadButton);
        }

    }
}
