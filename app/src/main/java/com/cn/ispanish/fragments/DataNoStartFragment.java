package com.cn.ispanish.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.PlayOrderActivity;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.Teacher;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.VestrewWebView;
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
 * Created by Hua on 17/4/18.
 */
public class DataNoStartFragment extends BaseFragment {

    IndexBlock block;
    Teacher teacher;
    VideoInfo info;
    boolean isBuy;

    @ViewInject(R.id.videoPlayContent_data_titleText)
    private TextView titleText;
    @ViewInject(R.id.videoPlayContent_data_priceText)
    private TextView priceText;
    @ViewInject(R.id.videoPlayContent_data_originalText)
    private TextView originalText;
    @ViewInject(R.id.videoPlayContent_data_sumText)
    private TextView sumText;
    @ViewInject(R.id.videoPlayContent_data_contentText)
    private TextView contentText;
    @ViewInject(R.id.videoPlayContent_data_teacherName)
    private TextView teacherName;
    @ViewInject(R.id.videoPlayContent_data_blockTitleText)
    private TextView blockTitleText;
    @ViewInject(R.id.videoPlayContent_data_teacherInfoText)
    private TextView teacherInfoText;
    @ViewInject(R.id.videoPlayContent_data_playMoneyButton)
    private TextView playMoneyButton;
    @ViewInject(R.id.videoPlayContent_data_playMoneyText)
    private TextView playMoneyText;
    @ViewInject(R.id.videoPlayContent_data_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.videoPlayContent_data_contentView)
    private VestrewWebView contentView;
    @ViewInject(R.id.videoPlayContent_data_buttonLayout)
    private LinearLayout buttonLayout;
    @ViewInject(R.id.videoPlayContent_data_playMoneyLayout)
    private LinearLayout playMoneyLayout;
    @ViewInject(R.id.videoPlayContent_data_teacherLayout)
    private LinearLayout teacherLayout;

    public DataNoStartFragment() {

    }

    public DataNoStartFragment(IndexBlock block, Teacher teacher, VideoInfo info, boolean isBuy) {
        this.block = block;
        this.teacher = teacher;
        this.info = info;
        this.isBuy = isBuy;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_video_paly_content_no_start_data,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        setData(block, teacher, info);

        return contactsLayout;
    }

    private void setData(IndexBlock block, Teacher teacher, VideoInfo info) {
        if (block == null || info == null) {
            return;
        }
        if (teacher == null) {
            teacherLayout.setVisibility(View.GONE);
        } else {
            teacherLayout.setVisibility(View.VISIBLE);

            teacherName.setText(teacher.getName());
            teacherInfoText.setText(teacher.getSynopsis());
            DownloadImageLoader.loadImage(teacherPic, teacher.getPortrait(), WinHandler.dipToPx(context, 30));
        }

        setTitleText(info);

        setPlayLayout();

        sumText.setText(block.getNum() + "人正在学习");
        contentText.setText(block.getDigest());
        priceText.setText(block.getPrice());
        playMoneyText.setText("￥" + block.getPrice());
        blockTitleText.setText("[" + block.getName() + "]");

        originalText.setText(block.getOriginal());
        originalText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setWebChromeClient(new WebChromeClient());
        contentView.setFocusable(false);
        contentView.loadData("http://www.ispanish.cn", block.getContent());

    }

    public void setTitleText(VideoInfo video) {
        titleText.setText(video.getName());
    }

    private void setPlayLayout() {
        playMoneyLayout.setVisibility(View.VISIBLE);
        playMoneyButton.setText("立即购买");
        if (block.isFree()) {
            playMoneyButton.setText("免费试听");
            playMoneyLayout.setVisibility(View.GONE);
        }

        if (isBuy) {
            playMoneyButton.setText("参与课程");
            playMoneyLayout.setVisibility(View.GONE);
        }

    }

    @OnClick(R.id.videoPlayContent_data_playMoneyButton)
    public void onPlayMoney(View view) {
        if (isBuy || block.isFree()) {
            return;
        }
        if (User.isLoginAndShowMessage(context)) {
            Bundle b = new Bundle();
            b.putString(PlayOrderActivity.TOPUP_BLOCK, block.getCourseid());
            b.putString(PlayOrderActivity.TOPUP_TITLE, block.getName());
            b.putString(PlayOrderActivity.TOPUP_MONEY, block.getPrice());

            PassagewayHandler.jumpActivity(context, PlayOrderActivity.class, PlayOrderActivity.Result_Code, b);
        }
    }
}
