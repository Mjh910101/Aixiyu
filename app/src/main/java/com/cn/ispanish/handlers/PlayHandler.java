package com.cn.ispanish.handlers;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

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
 * Created by Hua on 17/5/27.
 */
public class PlayHandler implements OnBufferingUpdateListener, MediaPlayer.OnPreparedListener {
    public MediaPlayer mediaPlayer;
    private SeekBar skbProgress;
    private TextView playTimeText;
    private Timer mTimer = new Timer();

    private boolean isPlay = false;

    public PlayHandler(SeekBar skbProgress) {
        this.skbProgress = skbProgress;

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }

        mTimer.schedule(mTimerTask, 0, 1000);
    }

    /*******************************************************
     * 通过定时器和Handler来更新进度条
     ******************************************************/
    TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (isPlay && skbProgress.isPressed() == false) {
//            if (mediaPlayer.isPlaying() && skbProgress.isPressed() == false) {
                handleProgress.sendEmptyMessage(0);
            }
        }
    };

    Handler handleProgress = new Handler() {
        public void handleMessage(Message msg) {

            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();

            if (duration > 0) {
                long pos = skbProgress.getMax() * position / duration;
                skbProgress.setProgress((int) pos);
            }
        }

    };

    public boolean isPlay() {
        return isPlay;
//        return mediaPlayer.isPlaying();
    }

    public void setIsPlay(boolean isPlay) {
        this.isPlay = isPlay;
    }

    public void play() {
        mediaPlayer.start();
        isPlay = true;
    }

    public void playInPath(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();//prepare之后自动播放
            mediaPlayer.start();

            setTimeText(playTimeText);

            isPlay = true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();//prepare之后自动播放
            mediaPlayer.start();

            setTimeText(playTimeText);

            isPlay = true;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void pause() {
        mediaPlayer.pause();
        isPlay = false;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            isPlay = false;
//            mediaPlayer = null;
        }
    }

    public void setOnCompletionListener(MediaPlayer.OnCompletionListener listener) {
        mediaPlayer.setOnCompletionListener(listener);
    }

    @Override
    /**
     * 通过onPrepared播放
     */
    public void onPrepared(MediaPlayer arg0) {
        arg0.start();
        Log.e("mediaPlayer", "onPrepared");
    }

    @Override
    public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
        skbProgress.setSecondaryProgress(bufferingProgress);
        int currentProgress = skbProgress.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.e(currentProgress + "% play", bufferingProgress + "% buffer");
    }

    public void setTimeText(TextView text) throws IOException {
        if (text == null) {
            return;
        }
        long t = mediaPlayer.getDuration() / 1000;
        text.setText(getTime(t));

    }

    private String getTime(long s) {
        StringBuilder sb = new StringBuilder();

        int time_m = (int) (s / 60);
        int time_s = (int) (s % 60);

        sb.append(time_m);
        sb.append(":");
        if (time_s < 10) {
            sb.append("0");
        }
        sb.append(time_s);

        return sb.toString();
    }

    public void initTimeText(TextView playTimeText) {
        this.playTimeText = playTimeText;
    }
}
