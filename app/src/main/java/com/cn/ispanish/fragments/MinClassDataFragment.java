package com.cn.ispanish.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;

import com.cn.ispanish.R;
import com.cn.ispanish.box.MinClass;
import com.cn.ispanish.box.OneByOne;
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
public class MinClassDataFragment extends BaseFragment {

    private MinClass minClass;

    @ViewInject(R.id.one2OneContentData_caontentView)
    private VestrewWebView contentView;

    public MinClassDataFragment(MinClass minClass) {
        this.minClass = minClass;
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
        contentView.loadData("http://www.ispanish.cn",minClass.getContent());

        return contactsLayout;
    }

}
