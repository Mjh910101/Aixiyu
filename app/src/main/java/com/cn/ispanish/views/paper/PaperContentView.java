package com.cn.ispanish.views.paper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.adapters.QuestionCommentAdapter;
import com.cn.ispanish.box.question.ListenQuestion;
import com.cn.ispanish.box.question.MultiSelectQuestion;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.question.ReadingQuestion;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
import com.cn.ispanish.views.paper.v1.PaperToTranslationView;
import com.cn.ispanish.views.paper.v2.layouts.PaperToClozeLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToInputDataLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToLineLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToListenInputLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToListenLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToListenSpokenLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToMultiSelectLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToMultiSelectZuHeLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToRadioLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToReadingLayout;
import com.cn.ispanish.views.paper.v2.layouts.PaperToSpokenLayout;

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
 * Created by Hua on 17/5/9.
 */
public abstract class PaperContentView extends LinearLayout {


    public Context context;
    public View view;
    public LayoutInflater inflater;

    public AnswerAdapter answerAdapter;

    public Question question;

    public int position;

    public boolean isShow;

    public OnQuestionListener onQuestion;
    public QuestionCommentAdapter.CallbackForComment callbackForComment;

    public PaperContentView(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        super(context);

        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.position = position;
        this.question = question;
        this.onQuestion = onQuestion;
        this.callbackForComment = callbackForComment;
        this.isShow = true;
    }


    public void setNum(TextView numView) {
        if (numView == null) {
            return;
        }
        numView.setText(String.valueOf(position));
    }

    public void setType(TextView typeView) {
        if (typeView == null) {
            return;
        }
        typeView.setText(question.getQuestionType());
    }

    public void setTitle(TextView titleView) {
        if (titleView == null) {
            return;
        }
        titleView.setText(question.getName());
//        titleView.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getName());
    }

    public void setExplain(TextView explainView) {
        if (explainView == null) {
            return;
        }
        explainView.setText(question.getExplain());
    }

    public void setExplain(TextView explainView, RelativeLayout explainButton) {
        if (explainView == null || explainButton == null) {
            return;
        }
        if (question.getExplain().equals("")) {
            explainButton.setVisibility(GONE);
        } else {
            explainView.setText(question.getExplain());
            explainButton.setVisibility(VISIBLE);
        }
    }

    public Question getQuestion() {
        return question;
    }

    public void setAnswerList(InsideListView answerList) {
        List<String> datalist = question.getAnswerList();

        if (answerList == null) {
            return;
        }
        if (datalist == null) {
            return;
        }
        if (datalist.isEmpty()) {
            return;
        }

        answerAdapter = new AnswerAdapter();
        answerList.setAdapter(answerAdapter);
    }

    public void setPic(ImageView picView) {
        String picUrl = question.getImg();
        if (picView == null) {
            return;
        }
        if (picUrl == null) {
            return;
        }
        if (picUrl.equals("")) {
            return;
        }
        if (picUrl.equals("null")) {
            return;
        }

        DownloadImageLoader.loadImage(picView, picUrl);

    }

    public abstract void initViewData();

    public abstract void showExplain();

//    public abstract void showExplainButton();
//    public abstract void closeExplainButton();

    public abstract void onStop();

    public void goneExplain() {
        isShow = false;
    }

    public abstract void showDoing();

    public void onUploadComment() {

    }

    public class AnswerAdapter extends BaseAdapter {

        List<String> answerList;
        int choose;

        public AnswerAdapter() {
            answerList = question.getAnswerList();
            choose = -1;
        }

        public void setChoose(int p) {
            choose = p;
        }

        public boolean isChoose() {
            return choose != -1;
        }

        @Override
        public int getCount() {
            return answerList.size();
        }

        @Override
        public Object getItem(int i) {
            return answerList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            Holder holder;
            if (view == null) {
                view = View.inflate(context, R.layout.layout_single_item, null);
                holder = new Holder(view);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            setData(holder, answerList.get(i), i);

            if (choose == -1) {
                setOnClick(view, answerList.get(i), i);
                view.setClickable(true);
            } else {
                view.setClickable(false);
            }

            return view;
        }

        private void setData(Holder holder, String answer, int i) {
            char a = (char) (65 + i);
            holder.singleIconText.setText(String.valueOf(a));

            if (answer.contains("http://") || answer.contains("https://")) {
                holder.answerText.setVisibility(GONE);
                holder.singleImageLayout.setVisibility(VISIBLE);
                initImageView(holder.singleImage, answer);
            } else {
                holder.answerText.setVisibility(VISIBLE);
                holder.singleImageLayout.setVisibility(GONE);
                holder.answerText.setText(answer);
            }

            if (choose == -1) {
                holder.singleIconText.setVisibility(VISIBLE);
                holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
            } else {
                if (question.isTrue(answer)) {
                    holder.singleIconText.setVisibility(INVISIBLE);
                    holder.answerIcon.setImageResource(R.drawable.single_true_icon);
                    holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
                } else {
                    if (choose == i) {
                        holder.singleIconText.setVisibility(INVISIBLE);
                        holder.answerIcon.setImageResource(R.drawable.single_flse_icon);
                        holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
                    } else {
                        holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                        holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
                    }
                }
            }
        }

        private void initImageView(ImageView singleImage, String answer) {
            double w = WinHandler.getWinWidth(context) / 3 * 2;
            double h = w / 16 * 9;
            singleImage.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
            singleImage.setScaleType(ImageView.ScaleType.CENTER);
            DownloadImageLoader.loadImage(singleImage, answer);
        }

        public void setOnClick(View view, final String answer, final int i) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose = i;
                    showExplain();
                    if (onQuestion != null) {
                        onQuestion.onQuestion(question, question.isTrue(answer));
                    }
                    notifyDataSetChanged();
                }
            });
        }

        class Holder {

            ImageView answerIcon;
            ImageView singleImage;
            TextView singleIconText;
            TextView answerText;
            RelativeLayout singleImageLayout;

            Holder(View view) {
                answerIcon = (ImageView) view.findViewById(R.id.singleAnswer_singleIcon);
                singleIconText = (TextView) view.findViewById(R.id.singleAnswer_singleIconText);
                answerText = (TextView) view.findViewById(R.id.singleAnswer_singleText);
                singleImage = (ImageView) view.findViewById(R.id.singleAnswer_singleImage);
                singleImageLayout = (RelativeLayout) view.findViewById(R.id.singleAnswer_singleImageLayout);
            }

        }
    }

    public static PaperContentView getContentView(Context context, Question question, int position, OnQuestionListener onQuestion, QuestionCommentAdapter.CallbackForComment callbackForComment) {
        position = position + 1;
        switch (question.getType()) {
            case Question.XuanZhe:
            case Question.RenWen:
            case Question.XuanZhe_IMAGE:
            case Question.PanDuan:
                return new PaperToRadioLayout(context, question, position, onQuestion, callbackForComment);
//            case Question.PanDuan:
//                return new PaperToJudgmentLayout(context, question, position, onQuestion);
            case Question.XiYiHan:
            case Question.HanYiXi:
            case Question.XieZuo:
                return new PaperToInputDataLayout(context, question, position, onQuestion, callbackForComment);
            case Question.TingLi_XIEZUO:
                return new PaperToListenInputLayout(context, question, position, onQuestion, callbackForComment);
            case Question.TingLiTingXie:
            case Question.TingXIE_WENZHANG:
                return new PaperToListenInputLayout(context, question, position, onQuestion, callbackForComment);
//                return new PaperToDictationView(context, question, position, onQuestion);
            case Question.KouYU:
                return new PaperToSpokenLayout(context, question, position, onQuestion, callbackForComment);
            case Question.WanXingTianKong:
                return new PaperToClozeLayout(context, (ReadingQuestion) question, position, onQuestion, callbackForComment);
            case Question.YueDu:
            case Question.TianKong:
                return new PaperToReadingLayout(context, (ReadingQuestion) question, position, onQuestion, callbackForComment);
            case Question.TingLi:
                return new PaperToListenLayout(context, (ListenQuestion) question, position, onQuestion, callbackForComment);
            case Question.TingLi_DATI:
            case Question.TingLi_XUANZHE:
            case Question.TingLi_PANDUAN:
//                return new PaperToListenView(context, (ListenQuestion) question, position, onQuestion);
                return new PaperToListenLayout(context, (ListenQuestion) question, position, onQuestion, callbackForComment);
            case Question.TingLi_LuYin:
                return new PaperToListenSpokenLayout(context, question, position, onQuestion, callbackForComment);
            case Question.LinaXian:
                return new PaperToLineLayout(context, question, position, onQuestion, callbackForComment);
            case Question.DuoXuan:
                return new PaperToMultiSelectLayout(context, question, position, onQuestion, callbackForComment);
            case Question.DuoXuan_ZuHe:
                return new PaperToMultiSelectZuHeLayout(context, (MultiSelectQuestion) question, position, onQuestion, callbackForComment);
            default:
                return new PaperToTranslationView(context, question, position, onQuestion, callbackForComment);
        }
    }

}
