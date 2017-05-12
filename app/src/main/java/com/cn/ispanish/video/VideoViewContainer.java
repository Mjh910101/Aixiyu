package com.cn.ispanish.video;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.easefun.polyvsdk.ijk.IjkVideoView;

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
 * Created by Hua on 17/3/13.
 */
public class VideoViewContainer extends RelativeLayout {

    private IjkVideoView videoView;
    private GestureDetector gestureDetector;

    public VideoViewContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVideoView(IjkVideoView videoView) {
        this.videoView = videoView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (videoView != null) {
            if (gestureDetector == null)
                gestureDetector = videoView.getGestureDetector();
            if (gestureDetector != null)
                gestureDetector.onTouchEvent(event);
        }
        return true;
    }

}
