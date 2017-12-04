package com.cn.ispanish.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.MessageHandler;

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
 * Created by Hua on 17/7/13.
 */
public class PaperIndexAdapter extends BaseAdapter {

    private List<Question> list;
    private Context context;
    private LayoutInflater infater;

    public PaperIndexAdapter(Context context, List<Question> list) {
        this.context = context;
        this.list = list;
        infater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = infater.inflate(R.layout.layout_grid_dailog_item, null);
        }

        TextView item = (TextView) convertView
                .findViewById(R.id.dialog_item_text);

        Question q = list.get(position);
        item.setText(String.valueOf(position + 1));
        if (q.isDoing()) {
            if (q.isRight()) {
                Log.e("paper index", (position + 1) + " : " + "right");
                item.setBackgroundResource(R.drawable.bluck_dialog_bg);
            } else {
                Log.e("paper index", (position + 1) + " : " + "un right");

                item.setBackgroundResource(R.drawable.red_dialog_bg);
            }
        } else {
            Log.e("paper index", (position + 1) + " : " + "no do");

            item.setBackgroundResource(R.drawable.grid_dialog_bg);
        }

        return convertView;
    }
}
