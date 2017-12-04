package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.WinHandler;
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
 * Created by Hua on 17/7/27.
 */
public class PaperExplainView extends LinearLayout {

    @ViewInject(R.id.paperExplain_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperExplain_questionAnswerText)
    private TextView questionAnswerText;
    @ViewInject(R.id.paperExplain_questionAnswerImage)
    private ImageView questionAnswerImage;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private Question question;
    private int num;

    public PaperExplainView(Context context) {
        super(context);
        initView(context);
    }

    public PaperExplainView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperExplainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperExplainView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_explain, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void initQuestion(Question question, int num) {
        this.question = question;
        this.num = num;
        initViewData();
    }

    private void initViewData() {
        String answer = question.getRightText();

        if (!answer.equals("")) {
            if (answer.contains("http://") || answer.contains("https://")) {
                questionAnswerImage.setVisibility(VISIBLE);

                double w = WinHandler.getWinWidth(context) / 3 * 2;
                double h = w / 16 * 9;
                questionAnswerImage.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
                questionAnswerImage.setScaleType(ImageView.ScaleType.CENTER);
                DownloadImageLoader.loadImage(questionAnswerImage, answer);
            } else {
                questionAnswerText.setVisibility(VISIBLE);
                questionAnswerText.setText(question.getRightText());
            }
        }

        if (!question.getExplain().equals("")) {
//            questionExplainText.setText("解析：\n" + question.getExplain());
            questionExplainText.setText(Html.fromHtml("<font color=\"#333333\">" + "解析" + "</font>" + "<br>" +
                    "<font color=\"#565656\">" + question.getExplain() + "</font>"));

        }

    }


}
