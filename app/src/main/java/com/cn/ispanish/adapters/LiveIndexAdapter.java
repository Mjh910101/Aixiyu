package com.cn.ispanish.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.LiveRoomActivity;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.Live;
import com.cn.ispanish.box.User;
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
public class LiveIndexAdapter extends BaseAdapter {

    Context context;
    List<Live> itemList;

    public LiveIndexAdapter(Context context, List<Live> itemList) {
        this.itemList = itemList;
        this.context = context;
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
            view = View.inflate(context, R.layout.layout_live_index_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Live live = itemList.get(i);

        setData(holder, live);
//        setOnClick(holder.image, live);
        return view;
    }

    private void setOnClick(View view, final Live live) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(User.isLoginAndShowMessage(context)){
                    Bundle b = new Bundle();
                    b.putSerializable(LiveRoomActivity.LIVE_KEY, live);
                    PassagewayHandler.jumpActivity(context, LiveRoomActivity.class, b);
                }
            }
        });
    }

    private void setData(Holder holder, Live live) {
        holder.title.setText(live.getTitle());
        holder.day.setText(live.getDay());
        holder.time.setText(live.getLengthTime());
        holder.people.setText(String.valueOf(live.getReadNum()));

        live.setStatusText(context, holder.status);

        holder.stateIcon.setVisibility(View.GONE);
        holder.stateBg.setVisibility(View.GONE);
        switch (live.getStatus()) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                holder.stateIcon.setVisibility(View.VISIBLE);
                holder.stateBg.setVisibility(View.VISIBLE);
                break;
        }

        DownloadImageLoader.loadImage(holder.image, live.getImgindex());
    }

    class Holder {

        TextView title;
        TextView day;
        TextView time;
        TextView people;
        TextView status;
        ImageView image;
        ImageView stateIcon;
        View stateBg;

        Holder(View view) {
            title = (TextView) view.findViewById(R.id.liveIndex_titleText);
            day = (TextView) view.findViewById(R.id.liveIndex_dayText);
            time = (TextView) view.findViewById(R.id.liveIndex_timeText);
            people = (TextView) view.findViewById(R.id.liveIndex_peopleSumText);
            status = (TextView) view.findViewById(R.id.liveIndex_statusText);
            image = (ImageView) view.findViewById(R.id.liveIndex_pic);
            stateIcon = (ImageView) view.findViewById(R.id.liveIndex_stateIcon);
            stateBg = view.findViewById(R.id.liveIndex_stateBg);
        }

    }

}
