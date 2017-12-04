package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.box.User;
import com.cn.ispanish.fragments.PaperFragment;
import com.cn.ispanish.handlers.PassagewayHandler;
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
 * Created by Hua on 17/5/22.
 */
public class ClassificationQusetionGridActivity extends BaseActivity {

    @ViewInject(R.id.title_titleText)
    private TextView titleText;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_qusetion_grid);

        ViewUtils.inject(this);

        initActivity();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick({R.id.classificationQusetion_xuanzheButton, R.id.classificationQusetion_panduanButton,
            R.id.classificationQusetion_xiezuoButton, R.id.classificationQusetion_xiyihanButton,
            R.id.classificationQusetion_hanyixiButton, R.id.classificationQusetion_yueduButton,
            R.id.classificationQusetion_wanxingButton, R.id.classificationQusetion_renwenButton,
            R.id.classificationQusetion_kouyuButton, R.id.classificationQusetion_tingliButton,
            R.id.classificationQusetion_tingxieButton, R.id.classificationQusetion_tiankongButton})
    public void onButton(View view) {
        if (!User.isLoginAndShowMessage(context)) {
            return;
        }
        Bundle b = new Bundle();
        switch (view.getId()) {
            case R.id.classificationQusetion_xuanzheButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.XuanZhe));
                break;
            case R.id.classificationQusetion_panduanButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.PanDuan));
                break;
            case R.id.classificationQusetion_xiezuoButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.XieZuo));
                break;
            case R.id.classificationQusetion_xiyihanButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.XiYiHan));
                break;
            case R.id.classificationQusetion_hanyixiButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.HanYiXi));
                break;
            case R.id.classificationQusetion_yueduButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.YueDu));
                break;
            case R.id.classificationQusetion_wanxingButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.WanXingTianKong));
                break;
            case R.id.classificationQusetion_renwenButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.RenWen));
                break;
            case R.id.classificationQusetion_kouyuButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.KouYU));
                break;
            case R.id.classificationQusetion_tingliButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.TingLi));
                break;
            case R.id.classificationQusetion_tingxieButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.TingLiTingXie));
                break;
            case R.id.classificationQusetion_tiankongButton:
                b.putString(PaperForTypeActivity.TYPE_KEY, String.valueOf(Question.TianKong));
                break;
        }
        b.putInt(PaperForTypeActivity.TIT_TYPE_KEY, type);
        PassagewayHandler.jumpActivity(context, PaperForTypeActivity.class, b);
    }

    private void initActivity() {
        titleText.setText("专项练习");

        Bundle b = getIntent().getExtras();
        if (b != null) {
            type = b.getInt(PaperFragment.TYPE_KEY);
        }
    }
}
