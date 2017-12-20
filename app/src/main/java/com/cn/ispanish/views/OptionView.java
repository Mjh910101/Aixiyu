package com.cn.ispanish.views;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.LineQuestion;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
 * Created by Hua on 2017/12/8.
 */

public class OptionView extends LinearLayout {

    @ViewInject(R.id.option_textView)
    private TextView textView;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    LineQuestion.Option obj;

    public OptionView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_option, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void setText(LineQuestion.Option obj) {
        if (obj == null) {
            return;
        }
        this.obj = obj;
        textView.setText(obj.getText());
    }

    public int getOptionID() {
        return obj.getId();
    }

    @Override
    public void setBackgroundResource(@DrawableRes int resid) {
//        super.setBackgroundResource(resid);
        textView.setBackgroundResource(resid);
    }

    public String getText() {
        return obj.getText();
    }
}
