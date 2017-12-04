package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.PaperActivity;
import com.cn.ispanish.box.question.Paper;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.PassagewayHandler;

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
public class PaperGridAdapter extends BaseAdapter {

    Context context;
    List<Paper> itemList;

    public PaperGridAdapter(Context context, List<Paper> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    public void removeAll() {
        itemList.removeAll(itemList);
        notifyDataSetChanged();
    }

    public void uploadAllData(List<Paper> list) {
        for (int i = 0; i < list.size(); i++) {
            Paper p = list.get(i);
            itemList.add(p);
        }
        notifyDataSetChanged();
    }

    public void uploadData(List<Paper> list) {
        for (int i = 0; i < list.size(); i++) {
            Paper p = list.get(i);
            for (int j = 0; j < itemList.size(); j++) {
                Paper itemObj = itemList.get(j);
                if (itemObj.equals(p)) {
                    itemObj.upload(p);
                }
            }
        }
        notifyDataSetChanged();
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
            view = View.inflate(context, R.layout.layout_paper_grid_item, null);
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
                if (!User.isLoginAndShowMessage(context)) {
                    return;
                }
                Bundle b = new Bundle();
                b.putString(PaperActivity.KID_KEY, paper.getId());
                b.putString(PaperActivity.PAPER_TITLE_KEY, paper.getName());
                b.putBoolean(PaperActivity.COLL_KEY, paper.isColl());

                PassagewayHandler.jumpActivity(context, PaperActivity.class, b);
            }
        });
    }

    private void initView(Holder holder, Paper paper) {
        holder.titleText.setText(paper.getName());
        holder.sumText.setText("本卷共" + paper.getTitlenum() + "题");
    }


    class Holder {

        TextView titleText;
        TextView sumText;

        Holder(View view) {
            titleText = (TextView) view.findViewById(R.id.paperGrid_titleText);
            sumText = (TextView) view.findViewById(R.id.paperGrid_sumText);
        }

    }
}
