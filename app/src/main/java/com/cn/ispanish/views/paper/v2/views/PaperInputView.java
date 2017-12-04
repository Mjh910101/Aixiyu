package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.interfaces.CallbackForBoolean;
import com.cn.ispanish.views.InsideViewFlipper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

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
 * Created by Hua on 17/7/25.
 */
public class PaperInputView extends LinearLayout {

    @ViewInject(R.id.paper_inputViewLayout)
    private LinearLayout inputViewLayout;
    @ViewInject(R.id.paper_inputView_dataInput)
    private EditText dataInput;
    @ViewInject(R.id.paper_inputView_checkButton)
    private TextView checkButton;
    @ViewInject(R.id.paper_inputView_checkLayout)
    private RelativeLayout checkLayout;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private InputMethodManager imm = null;

    private Question question;
    private CallbackForBoolean checkListener;

    public PaperInputView(Context context) {
        super(context);
        initView(context);
    }

    public PaperInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }


    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_input, null);

        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void initQuestion(Question question) {
        this.question = question;
    }

    @Override
    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
        if (!isNull()) {
            inputViewLayout.setVisibility(visibility);
        } else {
            inputViewLayout.setVisibility(GONE);
        }
    }

    public boolean isNull() {
        return question == null;
    }

    @OnClick(R.id.paper_inputView_checkButton)
    public void onCheck(View view) {
        boolean b = checkData(TextHandler.getText(dataInput));
        closeInput();
        question.setDoingRight(TextHandler.getText(dataInput));
        if (checkListener != null) {
            checkListener.callback(b);
        }
    }

    private void closeInput() {
//        checkLayout.setVisibility(GONE);
        checkLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1));
        dataInput.setFocusable(false);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(dataInput.getApplicationWindowToken(),
                    0);
        }
    }

    public void setOnCheckListener(CallbackForBoolean checkListener) {
        this.checkListener = checkListener;
    }

    private boolean checkData(String answer) {
        if (answer == null) {
            return false;
        }
        answer = answer.trim();
        if (answer.equals("")) {
            dataInput.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));
            return false;
        }
        if (!isNull()) {
            if (question.isTrue(answer)) {
                dataInput.setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
                return true;
            }
            dataInput.setTextColor(ColorHandle.getColorForID(context, R.color.red_text_c7));
            return false;
        }
        return false;
    }

    public void showDoing() {
        String str = question.getDoingRight();
        dataInput.setText(str);
        checkData(str);
        closeInput();
        if (checkListener != null) {
            checkListener.callback(false);
        }
    }
}
