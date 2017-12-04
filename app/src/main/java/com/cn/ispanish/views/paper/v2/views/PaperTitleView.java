package com.cn.ispanish.views.paper.v2.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.activitys.ImageListActivity;
import com.cn.ispanish.box.question.Question;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.views.InsideListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
 * Created by Hua on 17/7/27.
 */
public class PaperTitleView extends LinearLayout {

    @ViewInject(R.id.paperTitle_questionNumText)
    private TextView questionNumText;
    @ViewInject(R.id.paperTitle_questionTypeText)
    private TextView typeText;
    @ViewInject(R.id.paperTitle_questionTitleText)
    private TextView questionTitle;
    @ViewInject(R.id.paperTitle_questionPic)
    private ImageView questionPic;
    @ViewInject(R.id.paperTitle_questionTypeLayout)
    private LinearLayout questionTypeLayout;
    @ViewInject(R.id.paperTitle_questionImageList)
    private InsideListView questionImageList;

    private Context context;

    private View view;
    private LayoutInflater inflater;

    private Question question;
    private int num;

    public PaperTitleView(Context context) {
        super(context);
        initView(context);
    }

    public PaperTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PaperTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public PaperTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.layout_paper_title, null);

        ViewUtils.inject(this, view);
        addView(view);
    }

    public void initQuestion(Question question, int num) {
        this.question = question;
        this.num = num;
        initViewData();
        goneTypeLayout();
    }

    private void initViewData() {
        questionNumText.setText(String.valueOf(num));
        typeText.setText(question.getQuestionType());
        setTitle(question.getName());
//        setPic(questionPic);
        setImageList(questionImageList);
    }

    private void setImageList(InsideListView imageList) {
        List<String> imgDataList = question.getImageList();
        if (imgDataList == null) {
            return;
        }
        if (imgDataList.isEmpty()) {
            return;
        }
        if (imgDataList.size() == 1) {
            if (imgDataList.get(0).equals("")) {
                return;
            }
        }

        imageList.setAdapter(new ImageAdapter(imgDataList));

    }

    public void setTitle(CharSequence text) {
        questionTitle.setText(text);
    }

    public void setPic(ImageView picView) {
        String picUrl = question.getImg();
        if (picView == null) {
            return;
        }
        if (picUrl == null) {
            return;
        }
        if (picUrl.equals("")) {
            return;
        }
        if (picUrl.equals("null")) {
            return;
        }

        DownloadImageLoader.loadImage(picView, picUrl);

    }

    public void goneTypeLayout() {
        questionTypeLayout.setVisibility(GONE);
    }

    public void goneTitleLayout() {
        questionTitle.setVisibility(GONE);
    }

    class ImageAdapter extends BaseAdapter {

        List<String> dataList;

        ImageAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            double w = WinHandler.getWinWidth(context) - 50;
            double h = w / 16 * 9;
            imageView.setLayoutParams(new AbsListView.LayoutParams((int) w, (int) h));

            DownloadImageLoader.loadImage(imageView, dataList.get(i));

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle b = new Bundle();
                    b.putInt(ImageListActivity.POSITION_KEY, i);
                    b.putString(ImageListActivity.MESSAGE_KEY, "");
                    b.putStringArrayList(ImageListActivity.LIST_KEY, (ArrayList<String>) dataList);

                    PassagewayHandler.jumpActivity(context, ImageListActivity.class, b);
                }
            });
            return imageView;
        }
    }

}
