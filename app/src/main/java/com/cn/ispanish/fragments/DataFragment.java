package com.cn.ispanish.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.Teacher;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.VestrewWebView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

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
public class DataFragment extends BaseFragment {

    IndexBlock block;
    Teacher teacher;
    VideoInfo info;

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
    @ViewInject(R.id.videoPlayContent_data_teacherPic)
    private ImageView teacherPic;
    @ViewInject(R.id.videoPlayContent_data_contentView)
    private VestrewWebView contentView;

    public DataFragment() {

    }

    public DataFragment(IndexBlock block, Teacher teacher, VideoInfo info) {
        this.block = block;
        this.teacher = teacher;
        this.info = info;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_video_paly_content_data,
                container, false);
        ViewUtils.inject(this, contactsLayout);

        setData(block, teacher, info);

        return contactsLayout;
    }

    private void setData(IndexBlock block, Teacher teacher, VideoInfo info) {
        if (block == null || teacher == null || info == null) {
            return;
        }
        setTitleText(info);

        sumText.setText(block.getNum() + "人正在学习");
        contentText.setText(block.getDigest());
        priceText.setText(block.getPrice());
        blockTitleText.setText("[" + block.getName() + "]");

        originalText.setText(block.getOriginal());
        originalText.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        teacherName.setText(teacher.getName());
        teacherInfoText.setText(teacher.getSynopsis());
        DownloadImageLoader.loadImage(teacherPic, teacher.getPortrait(), WinHandler.dipToPx(context, 30));


        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setWebChromeClient(new WebChromeClient());
        contentView.setFocusable(false);
        contentView.loadData("http://www.ispanish.cn",block.getContent());

    }

    public void setTitleText(VideoInfo video) {
        titleText.setText(video.getName());
    }
}
