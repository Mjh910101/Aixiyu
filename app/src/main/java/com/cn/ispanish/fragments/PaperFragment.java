package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.ClassificationQusetionActivity;
import com.cn.ispanish.activitys.ClassificationQusetionGridActivity;
import com.cn.ispanish.activitys.CollectionPaperActivity;
import com.cn.ispanish.activitys.CompetitionRankingActivity;
import com.cn.ispanish.activitys.GameContentActivity;
import com.cn.ispanish.activitys.MyselfRankingActivity;
import com.cn.ispanish.activitys.PaperActivity;
import com.cn.ispanish.activitys.PaperForErrorActivity;
import com.cn.ispanish.activitys.PaperForErrorBankActivity;
import com.cn.ispanish.activitys.PaperForVipListActivity;
import com.cn.ispanish.activitys.PaperGridActivity;
import com.cn.ispanish.activitys.PaperGridOldActivity;
import com.cn.ispanish.activitys.PaperListActivity;
import com.cn.ispanish.activitys.ShareActivity;
import com.cn.ispanish.activitys.VipBlocktoListActivity;
import com.cn.ispanish.activitys.WebContentActivity;
import com.cn.ispanish.box.Banner;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.banner.BannerView;
import com.cn.ispanish.webview.MyWebViewClient;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONArray;
import org.json.JSONObject;

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
 * Created by Hua on 17/5/8.
 */
public class PaperFragment extends BaseFragment {

    public final static String TYPE_KEY = "type";

    public final static int FOUR_TYPE = 1;
    public final static int EIGHT_TYPE = 2;
    public final static int DELE_TYPE = 3;
    public final static int INDEX_TYPE = 4;
    public final static int ALL_TYPE = 5;

    @ViewInject(R.id.mainPaper_bannerLayout)
    private RelativeLayout bannerLayout;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paper_4BottomButton)
    private LinearLayout fourBottomButton;
    @ViewInject(R.id.paper_8BottonButton)
    private LinearLayout eightBottomButton;
    @ViewInject(R.id.paper_dellBottomButton)
    private LinearLayout dellBottomButton;
    @ViewInject(R.id.paper_indexBottomButton)
    private LinearLayout indexBottomButton;
    @ViewInject(R.id.mainPaper_orderPaperIcon)
    private ImageView orderPaperIcon;
    @ViewInject(R.id.mainPaper_orderPaperBg)
    private ImageView orderPaperBg;
    @ViewInject(R.id.mainPaper_contestPaperBg)
    private ImageView contestPaperBg;
    @ViewInject(R.id.mainPaper_paperOrdrtLayout)
    private RelativeLayout paperOrdrtLayout;
    @ViewInject(R.id.paper_fourLine)
    private View fourLine;
    @ViewInject(R.id.paper_enghtLine)
    private View enghtLine;
    @ViewInject(R.id.paper_deleLine)
    private View deleLine;
    @ViewInject(R.id.paper_indexLine)
    private View indexLine;

    private BannerView bannerView;
    private int nowType = FOUR_TYPE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_main_paper,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        initActivity();

        return contactsLayout;
    }

    private void initActivity() {
        setAnimation();
        downloadBanner();
    }

    @OnClick({R.id.paper_4Button, R.id.paper_8Button, R.id.paper_dellButton, R.id.paper_indexButton})
    public void onButton(View view) {
        fourLine.setVisibility(View.INVISIBLE);
        enghtLine.setVisibility(View.INVISIBLE);
        deleLine.setVisibility(View.INVISIBLE);
        indexLine.setVisibility(View.INVISIBLE);
        switch (view.getId()) {
            case R.id.paper_4Button:
                startAnimation(fourBottomButton);
                setPaperIcon(R.drawable.four_paper_icon);
                paperOrdrtLayout.setBackgroundResource(R.color.four_bg);
                fourLine.setVisibility(View.VISIBLE);
                nowType = FOUR_TYPE;
//                setPaperBg(R.drawable.four_round_bg);
                break;
            case R.id.paper_8Button:
                startAnimation(eightBottomButton);
                setPaperIcon(R.drawable.eight_paper_icon);
                nowType = EIGHT_TYPE;
                enghtLine.setVisibility(View.VISIBLE);
                paperOrdrtLayout.setBackgroundResource(R.color.enght_bg);
//                setPaperBg(R.drawable.eight_round_bg);
                break;
            case R.id.paper_dellButton:
                startAnimation(dellBottomButton);
                setPaperIcon(R.drawable.dell_paper_icon);
                nowType = DELE_TYPE;
                deleLine.setVisibility(View.VISIBLE);
                paperOrdrtLayout.setBackgroundResource(R.color.dele_bg);
//                setPaperBg(R.drawable.dell_round_bg);
                break;
            case R.id.paper_indexButton:
                startAnimation(indexBottomButton);
                setPaperIcon(R.drawable.index_paper_icon);
                nowType = INDEX_TYPE;
                indexLine.setVisibility(View.VISIBLE);
                paperOrdrtLayout.setBackgroundResource(R.color.index_bg);
//                setPaperBg(R.drawable.index_round_bg);
                break;
        }

    }

    private void setPaperIcon(int paperIcon) {
        orderPaperIcon.setImageResource(paperIcon);
    }

    private void setPaperBg(int paperIcon) {
        orderPaperBg.setImageResource(paperIcon);
//        contestPaperBg.setImageResource(paperIcon);
    }

    private void startAnimation(final View view) {
        view.setVisibility(View.VISIBLE);

        Animation alphaAnim = AnimationUtils.loadAnimation(context, R.anim.paper_altha);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(alphaAnim);
    }

    private void downloadBanner() {
        progress.setVisibility(View.VISIBLE);
        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.GET, UrlHandle.getTitBanner(context),
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        progress.setVisibility(View.GONE);
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(JsonHandle.getJSON(result), "result");
                        if (json != null && JsonHandle.getInt(json, "code") == 1) {
                            initBanner(JsonHandle.getArray(json, "data"));
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initBanner(JSONArray array) {
        if (array == null) {
            return;
        }
        List<Banner> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(new Banner(JsonHandle.getJSON(array, i)));
        }

        double w = WinHandler.getWinWidth(context);
        double h = w / 108d * 35d;

        bannerView = new BannerView(context, list);
//        bannerView.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        bannerLayout.addView(bannerView);
    }

    @OnClick(R.id.mainPaper_orderPaperButton)
    public void onOrderPaper(View view) {
//        PassagewayHandler.jumpActivity(context, PaperActivity.class);
        Bundle b = new Bundle();
        b.putInt(TYPE_KEY, nowType);
        PassagewayHandler.jumpActivity(context, PaperGridOldActivity.class, b);

    }

    @OnClick(R.id.mainPaper_orderSprintButton)
    public void onOrderSprint(View view) {
        Bundle b = new Bundle();
        b.putInt(TYPE_KEY, nowType);
        PassagewayHandler.jumpActivity(context, PaperForVipListActivity.class, b);
    }

    @OnClick(R.id.mainPaper_vipButton)
    public void onVipButton(View view) {
        Bundle b = new Bundle();
        b.putInt(TYPE_KEY, nowType);
        PassagewayHandler.jumpActivity(context, VipBlocktoListActivity.class, b);
    }

    @OnClick(R.id.mainPaper_classificationQusetionButton)
    public void onSprintButton(View view) {
        Bundle b = new Bundle();
        b.putInt(TYPE_KEY, nowType);
        PassagewayHandler.jumpActivity(context, ClassificationQusetionGridActivity.class, b);
    }

    @OnClick(R.id.mainPaper_collectionButton)
    public void onCollectionButton(View view) {
        PassagewayHandler.jumpActivity(context, CollectionPaperActivity.class);
    }

    @OnClick(R.id.mainPaper_paimingButton)
    public void onPaiMingButton(View view) {
        PassagewayHandler.jumpActivity(context, CompetitionRankingActivity.class);
    }

    @OnClick(R.id.mainPaper_gameButton)
    public void onGameButton(View view) {
        PassagewayHandler.jumpActivity(context, GameContentActivity.class);
    }

    @OnClick({R.id.mainPaper_eroorButton, R.id.mainPaper_myEroorButton})
    public void onErrorButton(View view) {
        PassagewayHandler.jumpActivity(context, PaperForErrorBankActivity.class);
//        PassagewayHandler.jumpActivity(context, PaperForErrorActivity.class);
    }

    @OnClick(R.id.mainPaper_shareButton)
    public void onShareButton(View view) {
        Bundle b = new Bundle();
        b.putString(ShareActivity.URL_KEY, "http://www.ispanish.cn/index.php/Home/Api/imgpage");
        PassagewayHandler.jumpActivity(context, ShareActivity.class, b);
    }

    @OnClick(R.id.mainPaper_rankingButton)
    public void onRankingButton(View view) {
        if (User.isLoginAndShowMessage(context)) {
            PassagewayHandler.jumpActivity(context, MyselfRankingActivity.class);
        }
    }

    @OnClick(R.id.mainPaper_sprintButton)
    public void onSprint(View view) {
        Bundle b = new Bundle();
        b.putString(MyWebViewClient.KEY, "http://www.ispanish.cn/index.php/Home/Api/matchpage.html");
        b.putString(MyWebViewClient.TITLE, "参赛奖品");
        PassagewayHandler.jumpActivity(context, WebContentActivity.class, b);
    }

    private void setAnimation() {
        Animation animation=AnimationUtils.loadAnimation(context, R.anim.paper_rotate);
        contestPaperBg.startAnimation(animation);
    }

}
