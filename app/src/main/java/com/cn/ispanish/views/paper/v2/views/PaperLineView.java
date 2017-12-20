package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.LineQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.LineLayout;
import com.cn.ispanish.views.OptionView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 2017/12/7.
 */

public class PaperLineView extends LinearLayout {

    @ViewInject(R.id.paperLian_lineLayout)
    private LineLayout lineLayout;
    @ViewInject(R.id.paperLian_questionLayout)
    private LinearLayout questionLayout;
    @ViewInject(R.id.paperLian_answerLayout)
    private LinearLayout answerLayout;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private LineQuestion question;
    private boolean isGame, isShow;

    public OnQuestionListener onQuestion;
    private CallbackForBoolean checkListener;

    int lineColor = R.color.zircon;

    int[] arr = {-1, -1};

    private OptionView beforTextview;

    List<OptionView> questionList = new ArrayList<>();
    List<OptionView> answerList = new ArrayList<>();
    List<OptionView> optionList = new ArrayList<>();

    public PaperLineView(Context context) {
        super(context);
        initView(context);
    }

    public PaperLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_line_view, null);

        isGame = false;
        isShow = false;

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void setShow(boolean b) {
        isShow = b;
    }

    public void setGame(boolean b) {
        isGame = b;
    }

    public boolean isGame() {
        return isGame;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setOnQuestion(OnQuestionListener onQuestion) {
        this.onQuestion = onQuestion;
    }

    public void initQuestion(LineQuestion question) {
        this.question = question;
    }

    @OnClick(R.id.paperLine_checkButton)
    public void onCheck(View view) {
        if (checkListener != null) {
            checkListener.callback(true);
        }
        notClick();
        showCheckLine();
    }


    public void show() {
        lineLayout.setPointSize(question.getAnswerOptionList().size() * 2);
        showTextLayout(answerLayout, question.getRightOptionList(), answerList);
        showTextLayout(questionLayout, question.getAnswerOptionList(), questionList);
        setOnClick();
    }

    private void setOnClick() {
        for (int i = 0; i < answerList.size(); i++) {
            optionList.add(questionList.get(i));
            optionList.add(answerList.get(i));
        }

        for (int i = 0; i < optionList.size(); i++) {
            final int index = i;
            final OptionView v = optionList.get(index);
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    v.setBackgroundResource(R.drawable.blue_white_rounded_5);
                    setValues(index);
                    setLine();
                    setTextBackGround(v);
                }
            });
        }
    }


    private void showTextLayout(LinearLayout layout, List<LineQuestion.Option> list, List<OptionView> optionList) {

        for (LineQuestion.Option obj : list) {
            OptionView v = getTextView(obj);
            layout.addView(v);
            optionList.add(v);
        }
    }

    private OptionView getTextView(LineQuestion.Option obj) {
        OptionView view = new OptionView(context);
        view.setText(obj);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, WinHandler.dipToPx(context, 80)));
        return view;
    }

    private void setLine() {
        if (arr[0] != -1 & arr[1] != -1) {
            lineLayout.setLine(arr[0], arr[1], lineColor);
            arr[0] = -1;
            arr[1] = -1;
        }
    }

    private void setTextBackGround(OptionView tv) {
        if (arr[0] == -1 && arr[1] == -1) {
            beforTextview.setBackgroundResource(R.drawable.black_whilt_rounded_5);
            tv.setBackgroundResource(R.drawable.black_whilt_rounded_5);
        } else {
            beforTextview = tv;
        }
    }

    private void setValues(int values) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) {
                arr[i] = values;
                return;
            }
        }
    }

    public void setOnCheckListener(CallbackForBoolean checkListener) {
        this.checkListener = checkListener;
    }

    private void notClick() {
        for (OptionView view : optionList) {
            view.setClickable(false);
        }
    }

    private void showCheckLine() {
        for (int i = 0; i < questionList.size(); i++) {
            OptionView a = questionList.get(i);
            for (int j = 0; j < answerList.size(); j++) {
                OptionView q = answerList.get(j);
                if (q.getOptionID() == a.getOptionID()) {
                    int answer1 = (i * 2);
                    int answer2 = (j * 2 + 1);
                    if (lineLayout.isAppear(answer1, answer2, lineColor)) {
                        lineLayout.setCorrectLine(answer1, answer2, R.color.united_nations_blue);
                    } else {
                        lineLayout.setCorrectLine(answer1, answer2, R.color.flame);
                    }
//                    MessageHandler.showToast(context, a.getText() + " " + (i * 2) + " + " + q.getText() + " " + (j * 2 + 1));
                    break;
                }
            }
        }
    }

}
