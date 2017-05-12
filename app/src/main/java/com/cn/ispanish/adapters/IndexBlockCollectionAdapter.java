package com.cn.ispanish.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.IndexBlockListActivity;
import com.cn.ispanish.box.IndexBlockCollection;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.views.InsideGridView;

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
public class IndexBlockCollectionAdapter extends BaseAdapter {

    private Context context;
    private List<IndexBlockCollection> itemDataList;

    public IndexBlockCollectionAdapter(Context context) {
        this.context = context;
        this.itemDataList = new ArrayList<>();
    }

    public void addItem(List<IndexBlockCollection> list) {
        for (IndexBlockCollection obj : list) {
            addItem(obj);
        }
        notifyDataSetChanged();
    }

    private void addItem(IndexBlockCollection obj) {
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
            view = View.inflate(context, R.layout.layout_index_block_collection_item, null);
            holder = new Holder(view);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        IndexBlockCollection item = itemDataList.get(i);

        setDate(holder, item);
        setOnClickMoren(holder.morenButton, item);

        return view;
    }

    private void setOnClickMoren(TextView view, final IndexBlockCollection item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b = new Bundle();
                b.putString(IndexBlockListActivity.ID_KEY, item.getId());
                b.putString(IndexBlockListActivity.TITLE_KEY, item.getName());
                PassagewayHandler.jumpActivity(context, IndexBlockListActivity.class, b);
            }
        });
    }

    private void setDate(Holder holder, IndexBlockCollection item) {
        holder.titleText.setText(item.getName());
        holder.dataGrid.setAdapter(new IndexBlockGirdAdapter(context, item.getBodyListTopFour()));
    }

    class Holder {

        TextView titleText;
        TextView morenButton;
        InsideGridView dataGrid;


        Holder(View view) {
            titleText = (TextView) view.findViewById(R.id.indexBlockCollection_titleText);
            morenButton = (TextView) view.findViewById(R.id.indexBlockCollection_morenButton);
            dataGrid = (InsideGridView) view.findViewById(R.id.indexBlockCollection_dataGrid);
        }

    }
}
