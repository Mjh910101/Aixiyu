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
import com.cn.ispanish.box.PushInfo;
import com.cn.ispanish.box.VideoHistory;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;

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
public class PushHistoryAdapter extends BaseAdapter {

    Context context;
    List<PushInfo> itemList;

    public PushHistoryAdapter(Context context, List<PushInfo> itemList) {
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
            view = View.inflate(context, R.layout.layout_push_history_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        PushInfo info = itemList.get(i);
        setData(holder, info);
        setOnClick(view, info);
        return view;
    }

    private void setOnClick(View view, final PushInfo obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(obj.getJumpIntent(context));
            }
        });
    }

    private void setData(Holder holder, PushInfo pushInfo) {
        setVideoPic(holder.videoPic, pushInfo.getBriefImg());
        holder.titleText.setText(pushInfo.getContent());
    }


    private void setVideoPic(ImageView imageView, String images) {
        //250:140
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        DownloadImageLoader.loadImage(imageView, images, 5);
    }

    class Holder {
        ImageView videoPic;
        TextView titleText;
        TextView lastTime;

        Holder(View view) {
            videoPic = (ImageView) view.findViewById(R.id.pushHistory_videoPic);
            titleText = (TextView) view.findViewById(R.id.pushHistory_titleText);
            lastTime = (TextView) view.findViewById(R.id.pushHistory_lastTimeText);
        }

    }
}
