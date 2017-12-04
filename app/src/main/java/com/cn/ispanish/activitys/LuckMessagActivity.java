package com.cn.ispanish.activitys;

import android.os.Bundle;
import android.view.View;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.lidroid.xutils.ViewUtils;
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
 * Created by Hua on 17/9/8.
 */
public class LuckMessagActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luck_message);

        ViewUtils.inject(this);

        isCheckLuck = false;
    }


    @OnClick(R.id.luckMessage_luckBg)
    public void onLuck(View view) {
        finish();
    }

    @OnClick(R.id.luckMessage_luckButton)
    public void onLuckButton(View view) {
        PassagewayHandler.jumpActivity(context, CompetitionRankingActivity.class);
        finish();
    }

}
