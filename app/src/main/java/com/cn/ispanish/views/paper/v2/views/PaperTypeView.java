package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.InsideListView;
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
 * Created by Hua on 17/7/27.
 */
public class PaperTypeView extends LinearLayout {

    @ViewInject(R.id.paperType_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperType_questionTypeText)
    private TextView typeText;
    @ViewInject(R.id.paperType_questionTypeLayout)
    private LinearLayout questionTypeLayout;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private String questionType;
    private int num;

    public PaperTypeView(Context context) {
        super(context);
        initView(context);
    }

    public PaperTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperTypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_type, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void initQuestion(String questionType, int num) {
        this.questionType = questionType;
        this.num = num;
        initViewData();
    }

    private void initViewData() {
        questionNumText.setText(String.valueOf(num));
        typeText.setText(questionType);
    }

    public void goneTypeLayout() {
        questionTypeLayout.setVisibility(GONE);
    }


}
