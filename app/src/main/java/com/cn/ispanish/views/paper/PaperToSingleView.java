package com.cn.ispanish.views.paper;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.Question;
import com.cn.ispanish.handlers.ColorHandle;
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
 * Created by Hua on 17/5/24.
 */
public class PaperToSingleView extends PaperContentView {


    @ViewInject(R.id.paperSingle_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperSingle_questionExplainText)
    private TextView questionExplainText;
    @ViewInject(R.id.paperSingle_questionExplainLayout)
    private LinearLayout questionExplainLayout;
    @ViewInject(R.id.paperSingle_answerList)
    private InsideListView answerList;
    @ViewInject(R.id.paperSingle_questionPic)
    private ImageView questionPic;

    private AnswerAdapter answerAdapter;

    public PaperToSingleView(Context context, Question question, int position) {
        super(context, question, position);

        view = inflater.inflate(R.layout.layout_paper_single, null);

        view.setLayoutParams(new ViewPager.LayoutParams());

        ViewUtils.inject(this, view);
        addView(view);

        initViewData();
    }

    private void initViewData() {
        setTitle(questionTitle);
        setExplain(questionExplainText);
        setPic(questionPic);
//        questionTitle.setText("题目" + position + "(" + question.getQuestionType() + ") : " + question.getName());
//        questionExplainText.setText(question.getExplain());

        answerAdapter = new AnswerAdapter();
        answerList.setAdapter(answerAdapter);
    }

    @Override
    public void showExplain() {
        questionExplainLayout.setVisibility(VISIBLE);
//        if (answerAdapter != null) {
//            if (!answerAdapter.isChoose()) {
//                answerAdapter.setChoose(question.getTrueIndex());
//            }
//            answerAdapter.notifyDataSetChanged();
//        }
    }


    class AnswerAdapter extends BaseAdapter {

        List<String> answerList;
        int choose;

        AnswerAdapter() {
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
                setOnClick(view, i);
                view.setClickable(true);
            } else {
                view.setClickable(false);
            }

            return view;
        }

        private void setData(Holder holder, String answer, int i) {
            holder.answerText.setText(answer);
            if (choose == -1) {
                holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
            } else {
                if (question.isTrue(answer)) {
                    holder.answerIcon.setImageResource(R.drawable.single_true_icon);
                    holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
                } else {
                    if (choose == i) {
                        holder.answerIcon.setImageResource(R.drawable.single_flse_icon);
                        holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
                    } else {
                        holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                        holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
                    }
                }
            }
        }

        public void setOnClick(View view, final int i) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    choose = i;
                    showExplain();
                    notifyDataSetChanged();
                }
            });
        }

        class Holder {

            ImageView answerIcon;
            TextView answerText;

            Holder(View view) {
                answerIcon = (ImageView) view.findViewById(R.id.singleAnswer_singleIcon);
                answerText = (TextView) view.findViewById(R.id.singleAnswer_singleText);
            }

        }
    }


}


