package com.cn.ispanish.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.play.PlayOrderActivity;
import com.cn.ispanish.adapters.CommentAdapter;
import com.cn.ispanish.adapters.VideoTitleAdapter;
import com.cn.ispanish.box.Comment;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.Teacher;
import com.cn.ispanish.box.User;
import com.cn.ispanish.box.VideoInfo;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.views.InsideListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
 * Created by Hua on 17/3/10.
 */
public class VideoPlayListActivity extends BaseActivity {

    public static final String COURSE_ID_KEY = "courseid";


    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;
    @ViewInject(R.id.videoPlayList_image)
    private ImageView imageView;
    @ViewInject(R.id.videoPlayList_titleText)
    private TextView blockTitleText;
    @ViewInject(R.id.videoPlayList_moneyText)
    private TextView moneyText;
    @ViewInject(R.id.videoPlayList_playMoneyText)
    private TextView playMoneyText;
    @ViewInject(R.id.videoPlayList_contentText)
    private TextView contentText;
    @ViewInject(R.id.videoPlayList_teacherText)
    private TextView teacherText;
    @ViewInject(R.id.videoPlayList_imageBgView)
    private View imageBgView;
    @ViewInject(R.id.videoPlayList_videoDataList)
    private InsideListView videoDataList;
    @ViewInject(R.id.videoPlayList_sumText)
    private TextView sumText;
    @ViewInject(R.id.videoPlayList_uploadSumText)
    private TextView uploadSumText;
    @ViewInject(R.id.videoPlayList_playMoneyButton)
    private TextView playMoneyButton;
    @ViewInject(R.id.videoPlayList_commentsDataList)
    private InsideListView commentsDataList;
    @ViewInject(R.id.videoPlayList_playMoneyLayout)
    private LinearLayout playMoneyLayout;

    private String courseId;
    private IndexBlock block;
    private Teacher teacher;

    private List<VideoInfo> videoList;
    private List<VideoInfo> unFreeVideoList;
    private List<VideoInfo> freeVideoList;

    private List<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play_list);

        ViewUtils.inject(this);

        initActivity();

    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case PlayOrderActivity.Result_Code:
//                onPlayResult(data);
//                break;
//            case LoginActivity.RequestCode:
//                downloadData(courseId);
//                break;
//        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        downloadData(courseId);
    }

    private void onPlayResult(Intent data) {
        if (data == null) {
            return;
        }

        if (data.getBooleanExtra(PlayOrderActivity.Result_Key, false)) {
            downloadData(courseId);
        }
    }

    @OnClick(R.id.videoPlayList_playMoneyButton)
    public void onPlayMoney(View view) {
        if (User.isLoginAndShowMessage(context)) {
//            downloadOrderId();
//            showPlayDialog();
            Bundle b = new Bundle();
            b.putString(PlayOrderActivity.TOPUP_ID, block.getCourseid());
            b.putString(PlayOrderActivity.TOPUP_TITLE, block.getName());
            b.putString(PlayOrderActivity.TOPUP_MONEY, block.getPrice());

            PassagewayHandler.jumpActivity(context, PlayOrderActivity.class, PlayOrderActivity.Result_Code, b);
        }
    }

    @OnClick(R.id.videoPlayList_allVideo)
    public void onAllVideo(View view) {
        Bundle b = new Bundle();
        b.putString(VideoDataListActivity.COURSE_ID_KEY, courseId);
        PassagewayHandler.jumpActivity(context, VideoDataListActivity.class, b);
    }

    private void initActivity() {
        titleText.setText("课程详细");

        Bundle b = getIntent().getExtras();
        if (b == null) {
            finish();
            return;
        }

        courseId = b.getString(COURSE_ID_KEY);

        downloadData(courseId);
        downloadComments(courseId);

    }

    private void downloadComments(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", id);
        params.addBodyParameter("start", "3");

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getVideoComment(context), params,
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

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            initCommenr(JsonHandle.getArray(json, "comment"));
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initCommenr(JSONArray array) {
        if (array == null) {
            return;
        }

        commentList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            commentList.add(new Comment(JsonHandle.getJSON(array, i)));
        }

        initCommenr(commentList);

    }

    private void initCommenr(List<Comment> commentList) {
        commentsDataList.setFocusable(false);
        commentsDataList.setAdapter(new CommentAdapter(context, commentList));
    }

    private void downloadData(String id) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams();
        params.addBodyParameter("sid", id);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getVideoPlay(context), params,
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

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {

                            boolean isBuy = JsonHandle.getString(json, "buy").equals("YES");

                            initBlock(JsonHandle.getJSON(json, "sp"));
                            initTeasher(JsonHandle.getJSON(json, "teacher"));
                            initVideoList(JsonHandle.getArray(json, "splist"), isBuy);

                            setPlayLayout(isBuy);
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void initVideoList(JSONArray array, boolean isBuy) {
        if (array == null) {
            return;
        }
        videoList = new ArrayList<>();
        unFreeVideoList = new ArrayList<>();
        freeVideoList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            VideoInfo video = new VideoInfo(JsonHandle.getJSON(array, i));
            video.setBuy(isBuy);
            videoList.add(video);
            if (video.isFree() || video.isBuy()) {
                freeVideoList.add(video);
            } else {
                unFreeVideoList.add(video);
            }
        }

        uploadSumText.setText("更新 第" + videoList.size() + "节");
        sumText.setText("选集（" + videoList.size() + "）");

        initVideoList(freeVideoList);

    }

    private void initVideoList(List<VideoInfo> list) {
        videoDataList.setFocusable(false);
        videoDataList.setAdapter(new VideoTitleAdapter(context, list, courseId));
    }

    private void initTeasher(JSONObject json) {
        if (json == null) {
            return;
        }

        teacher = new Teacher(json);
        initTeasher(teacher);

    }

    private void initTeasher(Teacher teacher) {
        teacherText.setText(teacher.getName());
    }

    private void initBlock(JSONObject json) {
        if (json == null) {
            return;
        }
        block = new IndexBlock(json);

        Log.e("Content","Content = "+block.getContent());

        initBlock(block);

    }

    private void initBlock(IndexBlock block) {
        setImage(imageView, block.getImages());
        blockTitleText.setText(block.getName());

        playMoneyText.setText(block.getPrice());
        contentText.setText(block.getDigest());

        if(!block.isNoMoney()){
            moneyText.setText("学点：" + block.getPrice());
        }else{
            moneyText.setText(" ");
        }

        initPlayLayout();
    }

    private void initPlayLayout() {
        if (block.isFree()) {
            playMoneyLayout.setVisibility(View.GONE);
            if (block.isNoMoney()) {
                playMoneyButton.setText("免费试听");
                playMoneyButton.setClickable(false);
            }
        } else {
            playMoneyButton.setText("参与课程");
            playMoneyButton.setClickable(true);
            playMoneyLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setPlayLayout(boolean isBuy) {
        if (isBuy) {
            playMoneyLayout.setVisibility(View.GONE);
            playMoneyButton.setText("已经购买");
            playMoneyButton.setClickable(false);
        } else {
            initPlayLayout();
        }
    }

    private void setImage(ImageView view, String images) {
        double w = (double) WinHandler.getWinWidth(context) / 5d * 2d;
        double h = w / 250d * 140d;
        view.setLayoutParams(new RelativeLayout.LayoutParams((int) w, (int) h));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        DownloadImageLoader.loadImage(view, images, 15, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                setMainColor(imageBgView, bitmap);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });
    }

    private void setMainColor(final View bgView, Bitmap bitmap) {
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
//                Palette.Swatch swatch = palette.getMutedSwatch();
                Palette.Swatch swatch = palette.getVibrantSwatch();
//                Palette.Swatch swatch = palette.getDarkMutedSwatch();
//                Palette.Swatch swatch = palette.getDarkVibrantSwatch();
//                Palette.Swatch swatch = palette.getLightMutedSwatch();
//                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                if (swatch != null) {
                    bgView.setBackgroundColor(swatch.getRgb());
                } else {
                    blockTitleText.setTextColor(ColorHandle.getColorForID(context, R.color.black_text_24));
                    teacherText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_56));
                }
            }
        });
    }

}
