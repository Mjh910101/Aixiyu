package com.cn.ispanish.activitys;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.fragments.OfflineFragment;
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
 * Created by Hua on 17/8/8.
 */
public class OffLineActivity extends BaseActivity {

    private OfflineFragment offlineFragment;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_bank);

        ViewUtils.inject(this);

        titleText.setText("我的缓存");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        offlineFragment = new OfflineFragment();
        transaction.add(R.id.questionBank_content, offlineFragment);
        transaction.commit();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

}
