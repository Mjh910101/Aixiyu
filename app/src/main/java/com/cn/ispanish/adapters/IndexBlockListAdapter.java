package com.cn.ispanish.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.VideoPlayContentNoStartActivity;
import com.cn.ispanish.activitys.VideoPlayListActivity;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;

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
 * Created by Hua on 17/3/6.
 */
public class IndexBlockListAdapter extends BaseAdapter {

    private Context context;
    private List<IndexBlock> itemDataList;

    public IndexBlockListAdapter(Context context) {
        this.context = context;
        this.itemDataList = new ArrayList<>();
    }

    public IndexBlockListAdapter(Context context, List<IndexBlock> itemDataList) {
        this.context = context;
        this.itemDataList = itemDataList;
    }

    public void addAllItem(List<IndexBlock> list) {
        if (list == null) {
            return;
        }
        for (IndexBlock obj : list) {
            addItem(obj);
        }
        notifyDataSetChanged();
    }

    private void addItem(IndexBlock obj) {
        itemDataList.add(obj);
    }

    @Override
    public int getCount() {
        return itemDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (view == null) {
            view = View.inflate(context, R.layout.layout_index_block_list_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        IndexBlock item = itemDataList.get(i);
        setDate(holder, item);
        setOnClick(view, item);

        return view;
    }

    private void setOnClick(View view, final IndexBlock item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
//                b.putString(VideoPlayListActivity.COURSE_ID_KEY, item.getCourseid());
//                PassagewayHandler.jumpActivity(context, VideoPlayListActivity.class, b);
                b.putString(VideoPlayContentNoStartActivity.TITLE_KEY, item.getName());
                b.putString(VideoPlayContentNoStartActivity.COURSE_ID_KEY, item.getCourseid());
                PassagewayHandler.jumpActivity(context, VideoPlayContentNoStartActivity.class, b);
            }
        });
    }

    private void setDate(Holder holder, IndexBlock item) {
        setBlockPic(holder.blockPic, item.getImages());

        holder.titleText.setText(item.getName());
        holder.priceText.setText(item.getPrice());

        holder.originalText.setText(item.getOriginal());
        holder.originalText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setBlockPic(ImageView imageView, String images) {
        //250:140
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        DownloadImageLoader.loadImage(imageView, images, 5);
    }

    class Holder {

        ImageView blockPic;
        TextView titleText;
        TextView priceText;
        TextView originalText;

        Holder(View view) {
            blockPic = (ImageView) view.findViewById(R.id.indexBlock_blockPic);
            titleText = (TextView) view.findViewById(R.id.indexBlock_blockTitleText);
            priceText = (TextView) view.findViewById(R.id.indexBlock_blockPriceText);
            originalText = (TextView) view.findViewById(R.id.indexBlock_blockOriginalText);
        }

    }
}
