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
import com.cn.ispanish.activitys.PaperActivity;
import com.cn.ispanish.box.question.Paper;
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
 * Created by Hua on 17/5/22.
 */
public class PaperListAdapter extends BaseAdapter {

    Context context;
    List<Paper> itemList;

    public PaperListAdapter(Context context, List<Paper> itemList) {
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
            view = View.inflate(context, R.layout.layout_paper_list_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        Paper paper = itemList.get(i);

        initView(holder, paper);
        setOnClick(view, paper);

        return view;
    }

    private void setOnClick(View view, final Paper paper) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString(PaperActivity.KID_KEY, paper.getId());
                b.putString(PaperActivity.PAPER_TITLE_KEY, paper.getName());
                b.putBoolean(PaperActivity.COLL_KEY, paper.isColl());

                PassagewayHandler.jumpActivity(context, PaperActivity.class, b);
            }
        });
    }

    private void initView(Holder holder, Paper paper) {
        setPic(holder.picView, paper.getImages());
        holder.titleText.setText(paper.getName());
        holder.infoTetx.setText(paper.getDetail());
        holder.sumText.setText(paper.getBuynum() + "人正在学习");
        holder.discountText.setText("￥" + paper.getDiscount());

        holder.priceText.setText("￥" + paper.getPrice());
        holder.priceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void setPic(ImageView imageView, String image) {
        double w = (double) WinHandler.getWinWidth(context) / 7d * 2d;
        double h = w / 140d * 180d;
        imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        DownloadImageLoader.loadImage(imageView, image, 5);
    }

    class Holder {

        TextView titleText;
        TextView infoTetx;
        TextView priceText;
        TextView discountText;
        TextView sumText;
        ImageView picView;

        Holder(View view) {
            titleText = (TextView) view.findViewById(R.id.paperList_item_titleTetx);
            infoTetx = (TextView) view.findViewById(R.id.paperList_item_infoTetx);
            priceText = (TextView) view.findViewById(R.id.paperList_item_priceText);
            discountText = (TextView) view.findViewById(R.id.paperList_item_discountText);
            sumText = (TextView) view.findViewById(R.id.paperList_item_sumText);
            picView = (ImageView) view.findViewById(R.id.paperList_item_picView);
        }

    }
}
