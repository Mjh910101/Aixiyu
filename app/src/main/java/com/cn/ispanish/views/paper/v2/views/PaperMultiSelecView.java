package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.interfaces.OnQuestionListener;
import com.cn.ispanish.views.InsideListView;
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
 * Created by Hua on 17/7/31.
 */
public class PaperMultiSelecView extends LinearLayout {

    @ViewInject(R.id.paperRadio_answerList)
    private InsideListView answerList;
    @ViewInject(R.id.paperRadio_checkLayout)
    private RelativeLayout checkLayout;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private Question question;
    private boolean isGame, isShow;

    private AnswerAdapter answerAdapter;

    public OnQuestionListener onQuestion;

    private CallbackForBoolean checkListener;

    public PaperMultiSelecView(Context context) {
        super(context);
        initView(context);
    }

    public PaperMultiSelecView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperMultiSelecView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperMultiSelecView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_radio, null);

        isGame = false;
        isShow = false;

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void show() {
        setShow(true);
        if (answerAdapter != null) {
            List<String> chooseList = answerAdapter.chooseList;
            answerAdapter = new AnswerAdapter();
            answerAdapter.setChooseList(chooseList);
            answerList.setAdapter(answerAdapter);
        }
//        answerAdapter.notifyDataSetChanged();
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

    public void initQuestion(Question question) {
        this.question = question;
        initViewData();
    }

    private void initViewData() {
        setAnswerList(answerList);
        setShowCheck();
    }

    public void setAnswerList(InsideListView answerList) {

        if (answerList == null) {
            return;
        }
        if (!isHaveAnswer()) {
            return;
        }
        answerAdapter = new AnswerAdapter();
        answerList.setAdapter(answerAdapter);
        MessageHandler.showToast(context, "Show Answer Adapter ");
    }

    public boolean isHaveAnswer() {
        List<String> datalist = question.getAnswerList();
        if (datalist == null) {
            return false;
        }
        if (datalist.isEmpty()) {
            return false;
        }
        if (datalist.size() == 1) {
            String s = datalist.get(0);
            if (s.equals("")) {
                return false;
            }
        }
        return true;
    }

    public void setShowCheck() {
        checkLayout.setVisibility(VISIBLE);
    }

    public void setOnCheckListener(CallbackForBoolean checkListener) {
        this.checkListener = checkListener;
    }


    @OnClick(R.id.paperRadio_checkButton)
    public void onCheck(View view) {
        if (checkListener != null) {
            checkListener.callback(true);
        }
        isShow = true;
        if (answerAdapter != null) {
            answerAdapter.notifyDataSetChanged();
        }
    }

    class AnswerAdapter extends BaseAdapter {

        List<String> answerList;
        List<String> chooseList;

        AnswerAdapter() {
            answerList = question.getAnswerList();
            chooseList = new ArrayList<>();
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

            if (!isShow) {
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

            if (chooseList.isEmpty()) {
                holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
            } else {
                if (!isShow) {
                    if (chooseList.contains(answer)) {
                        holder.answerIcon.setImageResource(R.drawable.single_true_icon);
                    } else {
                        holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                    }
                    holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
                } else {
                    if (question.isTrue(answer)) {
                        holder.answerIcon.setImageResource(R.drawable.single_true_icon);
                        holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
                    } else {
                        if (chooseList.contains(answer)) {
                            holder.answerIcon.setImageResource(R.drawable.single_flse_icon);
                            holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
                        } else {
                            holder.answerIcon.setImageResource(R.drawable.single_init_icon);
                            holder.answerText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_66));
                        }
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
                    if (chooseList.contains(answer)) {
                        chooseList.remove(answer);
                    } else {
                        chooseList.add(answer);
                    }
                    notifyDataSetChanged();
                }
            });
        }

        public void setChooseList(List<String> chooseList) {
            this.chooseList = chooseList;
            notifyDataSetChanged();
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

}
