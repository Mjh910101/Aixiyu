package com.cn.ispanish.fragments;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.IndexBlock;
import com.cn.ispanish.box.OneByOne;
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
public class OneByOneDataFragment extends BaseFragment {

    private OneByOne oneByOne;

    @ViewInject(R.id.one2OneContentData_caontentView)
    private VestrewWebView contentView;

    public OneByOneDataFragment(OneByOne oneByOne) {
        this.oneByOne = oneByOne;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        View contactsLayout = inflater.inflate(R.layout.layout_one_by_one_content_data,
                container, false);
        ViewUtils.inject(this, contactsLayout);


        contentView.getSettings().setJavaScriptEnabled(true);
        contentView.setWebChromeClient(new WebChromeClient());
        contentView.setFocusable(false);
        contentView.loadData("http://www.ispanish.cn",oneByOne.getContent());

        return contactsLayout;
    }

}
