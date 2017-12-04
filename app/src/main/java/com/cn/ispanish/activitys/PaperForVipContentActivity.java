package com.cn.ispanish.activitys;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.play.PlayPaperOrderActivity;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VipInformation;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.VestrewWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
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
 * Created by Hua on 17/6/15.
 */
public class PaperForVipContentActivity extends BaseActivity {

    public final static String KID_KEY = "kid";

    @ViewInject(R.id.title_titleText)
    private TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.paperContent_imageView)
    private ImageView imageView;
    @ViewInject(R.id.paperContent_titleText)
    private TextView paperTitleText;
    @ViewInject(R.id.paperContent_timeText)
    private TextView timeText;
    @ViewInject(R.id.paperContent_infoText)
    private TextView infoText;
    @ViewInject(R.id.paperContent_discountText)
    private TextView discountText;
    @ViewInject(R.id.paperContent_priceText)
    private TextView priceText;
    @ViewInject(R.id.paperContent_sumText)
    private TextView sumText;
    @ViewInject(R.id.paperContent_learningButton)
    private TextView learningButton;
    @ViewInject(R.id.paperContent_buyDayText)
    private TextView buyDayText;
    @ViewInject(R.id.paperContent_demandText)
    private TextView demandText;
    @ViewInject(R.id.paperContent_contentView)
    private VestrewWebView contentView;
    @ViewInject(R.id.paperContent_learningLayout)
    private RelativeLayout learningLayout;
    @ViewInject(R.id.paperContent_palyLayout)
    private LinearLayout palyLayout;
    @ViewInject(R.id.paperContent_getLayout)
    private LinearLayout getLayout;
    @ViewInject(R.id.paperContent_getButtonLayout)
    private RelativeLayout getButtonLayout;

    private String kid;

    private VipInformation vipInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_content);

        ViewUtils.inject(this);

        setFlagSecure(true);

        initActivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        downloadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.paperContent_closePlayLayout)
    public void onClosePlayLayout(View view) {
        learningLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.paperContent_getButton)
    public void onGetButton(View view) {
        getLayout.setVisibility(View.VISIBLE);
        palyLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.paperContent_playButton)
    public void onPlayButton(View view) {
        jumpPlay();
        learningLayout.setVisibility(View.GONE);
    }

    @OnClick(R.id.paperContent_copyButton)
    public void onCopyButton(View view) {
        uploadCopy();
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText("aixiyu1234".trim());
        MessageHandler.showToast(context, "已复制至复制面板");
    }

    private void uploadCopy() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("kid", kid);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getDemandBank(context), params,
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
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    @OnClick(R.id.paperContent_learningButton)
    public void onPlayMoney(View view) {
        if (vipInformation == null) {
            return;
        }
        if (User.isLoginAndShowMessage(context)) {
            if (vipInformation.isBuy() || vipInformation.isFeer()) {
                if (vipInformation.isImage()) {
                    jumpImageList();
                } else {
                    jumpPaper();
                }
            } else {
                learningLayout.setVisibility(View.VISIBLE);
                palyLayout.setVisibility(View.VISIBLE);
                getLayout.setVisibility(View.GONE);
            }
        }
    }

    private void jumpPlay() {
        if (User.isLoginAndShowMessage(context)) {
            Bundle b = new Bundle();
            b.putString(PlayPaperOrderActivity.TOPUP_BLOCK, vipInformation.getId());
            b.putString(PlayPaperOrderActivity.TOPUP_TITLE, vipInformation.getName());
            b.putString(PlayPaperOrderActivity.TOPUP_MONEY, vipInformation.getDiscount());

            PassagewayHandler.jumpActivity(context, PlayPaperOrderActivity.class, PlayPaperOrderActivity.Result_Code, b);
        }
    }

    private void jumpPaper() {
        Bundle b = new Bundle();
        b.putString(PaperActivity.KID_KEY, vipInformation.getId());
        b.putString(PaperActivity.PAPER_TITLE_KEY, vipInformation.getName());
        b.putBoolean(PaperActivity.COLL_KEY, vipInformation.isColl());

        PassagewayHandler.jumpActivity(context, PaperActivity.class, b);
    }

    private void jumpImageList() {
        Bundle b = new Bundle();
        b.putInt(ImageListActivity.POSITION_KEY, 0);
        b.putString(ImageListActivity.MESSAGE_KEY, "");
        b.putStringArrayList(ImageListActivity.LIST_KEY, (ArrayList<String>) vipInformation.getImgList());

        PassagewayHandler.jumpActivity(context, ImageListActivity.class, b);
    }

    private void initActivity() {
        titleText.setText("独家资料");

        Bundle b = getIntent().getExtras();

        if (b == null) {
            finish();
            return;
        }

        kid = b.getString(KID_KEY);

        downloadData();
    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("kid", kid);

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getImgInforMationXi(context), params,
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
                        if (json != null & JsonHandle.getInt(json, "code") == 1) {
                            JSONArray array = JsonHandle.getArray(json, "data");
                            initDataAdapter(array);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initDataAdapter(JSONArray array) {
        if (array == null) {
            return;
        }

        List<VipInformation> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            VipInformation p = new VipInformation(JsonHandle.getJSON(array, i));
            list.add(p);
        }

        if (!list.isEmpty()) {
            initData(list.get(0));
        }
    }

    private void initData(VipInformation vipInformation) {
        this.vipInformation = vipInformation;

        paperTitleText.setText(vipInformation.getName());
        timeText.setText("更新时间：" + vipInformation.getTime4Text());
        infoText.setText("适合人群：" + vipInformation.getFit());

        sumText.setText(vipInformation.getBuynum() + "人正在学习");
        discountText.setText("￥" + vipInformation.getDiscount());

        priceText.setText("￥" + vipInformation.getPrice());
        priceText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        DownloadImageLoader.loadImage(imageView, vipInformation.getImages());

//        contentView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        contentView.getSettings().setJavaScriptEnabled(true);
//        contentView.getSettings().setUseWideViewPort(true);
//        contentView.getSettings().setLoadWithOverviewMode(true);
        contentView.setWebChromeClient(new WebChromeClient());
        contentView.setFocusable(false);
        contentView.loadData("http://www.ispanish.cn", vipInformation.getIntroduction());

        if (vipInformation.isBuy() || vipInformation.isFeer()) {
            learningButton.setText("查看资料");
        } else {
            learningButton.setText("马上学习");
        }

        buyDayText.setText("(有效期" + vipInformation.getBuydeadline() + "天)");
        demandText.setText("(有效期" + vipInformation.getDemanddeadline() + "天)");

        if (vipInformation.getDemandcount() <= 0) {
            getButtonLayout.setVisibility(View.GONE);
        }

    }


}

