package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.MinClass;
import com.cn.ispanish.box.OneByOne;
import com.cn.ispanish.handlers.ColorHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 17/4/18.
 */
public class MinClassOutLineFragment extends BaseFragment {

    private MinClass minClass;

    private List<String> outLine;

    @ViewInject(R.id.one2OneOutLine_dataList)
    private ListView dataList;

    public MinClassOutLineFragment(MinClass minClass) {
        this.minClass = minClass;
        outLine = minClass.getOutLine();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_one_by_one_out_line,
                container, false);
        ViewUtils.inject(this, contactsLayout);


        dataList.setAdapter(new Adapter());

        return contactsLayout;
    }

    class Adapter extends BaseAdapter {

        int p = 25;

        @Override
        public int getCount() {
            return outLine.size();
        }

        @Override
        public Object getItem(int i) {
            return outLine.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            TextView textView = new TextView(context);

            textView.setText(outLine.get(i));
            textView.setPadding(p, p, p, p);
            textView.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));

            return textView;
        }
    }

}
