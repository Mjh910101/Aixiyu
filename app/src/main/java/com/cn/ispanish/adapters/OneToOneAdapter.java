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
import com.cn.ispanish.activitys.OneByOneContentActivity;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.OneByOne;
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
public class OneToOneAdapter extends BaseAdapter {

    Context context;
    List<OneByOne> itemList;

    public OneToOneAdapter(Context context, List<OneByOne> itemList) {
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
            view = View.inflate(context, R.layout.layout_one_to_one_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        OneByOne obj = itemList.get(i);
        setData(holder, obj);
//        setOnClick(view, obj);

        return view;
    }

    private void setOnClick(View view, final OneByOne obj) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putSerializable(OneByOneContentActivity.OBJ_KEY, obj);
                PassagewayHandler.jumpActivity(context, OneByOneContentActivity.class, b);
            }
        });
    }

    private void setData(Holder holder, OneByOne obj) {
        double w = (double) WinHandler.getWinWidth(context) / 3d;
        double h = w / 16d * 9d;
        holder.pic.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));

        DownloadImageLoader.loadImage(holder.pic, obj.getImgindex());


        holder.titleText.setText(obj.getTitle());
        holder.infoText.setText(obj.getContent());
        holder.teacherText.setText(obj.getTeacherInfo());
    }

    class Holder {

        ImageView pic;
        TextView titleText;
        TextView infoText;
        TextView teacherText;

        Holder(View view) {
            pic = (ImageView) view.findViewById(R.id.one2One_pic);
            titleText = (TextView) view.findViewById(R.id.one2One_titleText);
            infoText = (TextView) view.findViewById(R.id.one2One_infoText);
            teacherText = (TextView) view.findViewById(R.id.one2One_teacherText);
        }

    }

}
