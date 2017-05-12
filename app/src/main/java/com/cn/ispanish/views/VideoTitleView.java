package com.cn.ispanish.views;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.VideoPlayActivity;
import com.cn.ispanish.activitys.VideoPlayContentActivity;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;

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
 * Created by Hua on 17/3/10.
 */
public class VideoTitleView extends TextView implements View.OnClickListener {

    private VideoInfo video;
    private Context context;

    String courseId;
    int p;

    public VideoTitleView(Context context, VideoInfo video, String courseId) {
        super(context);
        this.context = context;
        this.video = video;
        this.courseId = courseId;
        p = WinHandler.dipToPx(context, 6);
        initView(video);
        setOnClickListener(this);
    }

    private void initView(VideoInfo video) {
        setText(video.getName());
        setTextSize(16);
        setMaxLines(1);
        setEllipsize(TextUtils.TruncateAt.END);
        setPadding(p, p, p, p);
        if (video.isFree()) {
            setBackgroundResource(R.drawable.green_transparent_rounded_5);
            setTextColor(ColorHandle.getColorForID(context, R.color.module_green));
        } else if (video.isBuy()) {
//            setBackgroundResource(R.drawable.blue_transparent_rounded_5);
//            setTextColor(ColorHandle.getColorForID(context, R.color.bleu_text_36));
            setBackgroundResource(R.drawable.green_transparent_rounded_5);
            setTextColor(ColorHandle.getColorForID(context, R.color.module_green));
        } else {
            setBackgroundResource(R.drawable.gray_transparent_rounded_5);
            setTextColor(ColorHandle.getColorForID(context, R.color.gray_aa));
        }
        setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public void onClick(View view) {
        if (video.isFree() || video.isBuy()) {
            Bundle b = new Bundle();
            b.putString(VideoPlayContentActivity.VID_KEY, video.getVid());
            b.putString(VideoPlayContentActivity.TITLE_KEY, video.getName());
            b.putString(VideoPlayContentActivity.COURSE_ID_KEY, courseId);
            PassagewayHandler.jumpActivity(context, VideoPlayContentActivity.class, b);
//            PassagewayHandler.jumpActivity(context, VideoPlayActivity.class, b);
        } else {
            MessageHandler.showToast(context, "未拥有该课程");
        }
    }
}
