package com.cn.ispanish.activitys;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.fragments.PaperFragment;
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
 * Created by Hua on 17/5/17.
 */
public class QuestionBankActivity extends BaseActivity {

    private PaperFragment paperFragment;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        ViewUtils.inject(this);

        titleText.setText("题库");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        paperFragment = new PaperFragment();
        transaction.add(R.id.questionBank_content, paperFragment);
        transaction.commit();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

}
