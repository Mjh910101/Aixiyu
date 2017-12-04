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
import com.cn.ispanish.activitys.MinClassContentActivity;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.MinClass;
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
 * Created by Hua on 2017/10/10.
 */

public class MinClassAdapter extends BaseAdapter {

    Context context;
    List<MinClass> itemList;

    public MinClassAdapter(Context context, List<MinClass> itemList) {
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
            view = View.inflate(context, R.layout.layout_min_class_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        MinClass obj = itemList.get(i);
        setData(holder, obj);
//        setOnClick(view, obj);

        return view;
    }

    private void setOnClick(View view, final MinClass obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable(MinClassContentActivity.OBJ_KEY, obj);
                PassagewayHandler.jumpActivity(context, MinClassContentActivity.class, b);
            }
        });
    }

    private void setData(Holder holder, MinClass obj) {
        double w = (double) WinHandler.getWinWidth(context) / 3d;
        double h = w / 16d * 9d;
        holder.pic.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));

        DownloadImageLoader.loadImage(holder.pic, obj.getImgindex());


        holder.titleText.setText(obj.getTitle());
        holder.infoText.setText(obj.getContent());
        holder.teacherText.setText(obj.getTeacherInfo());

        holder.priceText.setText(obj.getPrice());
        holder.originalText.setText(obj.getOriginal());
        holder.originalText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    class Holder {

        ImageView pic;
        TextView titleText;
        TextView infoText;
        TextView teacherText;
        TextView priceText;
        TextView originalText;

        Holder(View view) {
            pic = (ImageView) view.findViewById(R.id.minClass_pic);
            titleText = (TextView) view.findViewById(R.id.minClass_titleText);
            infoText = (TextView) view.findViewById(R.id.minClass_infoText);
            teacherText = (TextView) view.findViewById(R.id.minClass_teacherText);
            priceText = (TextView) view.findViewById(R.id.minClass_priceText);
            teacherText = (TextView) view.findViewById(R.id.minClass_teacherText);
            originalText = (TextView) view.findViewById(R.id.minClass_originalText);
        }
    }
}
