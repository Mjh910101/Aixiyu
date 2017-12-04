package com.cn.ispanish.activitys;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.handlers.BitmapHandler;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

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
 * Created by Hua on 15/11/2.
 */
public class ShareActivity extends BaseActivity {

    public final static String URL_KEY = "url";
    public final static String TITIE_TEXT = "爱西语";
    public final static String CONTENT_TEXT = "西语大赛";


    @ViewInject(R.id.share_dataGrid)
    private GridView dataGrid;
    @ViewInject(R.id.share_progress)
    private ProgressBar progress;

    private String url;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        ViewUtils.inject(this);
        context = this;

        initActivity();
    }


    @OnClick({R.id.title_backIcon})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_backIcon:
                finish();
                break;
        }
    }

    private void initActivity() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            url = b.getString(URL_KEY);
            dataGrid.setAdapter(new ShareBaseAdapter());

            downloadData();
        }

    }

    private void downloadData() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getITestPaper(context), params,
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
                            title = JsonHandle.getString(json, "name");
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    class ShareBaseAdapter extends BaseAdapter {

        List<Integer> dataList;
        LayoutInflater inflater;

        ShareBaseAdapter() {
            initDataList();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        private void initDataList() {
            dataList = new ArrayList<Integer>();

            if (isClientValid(Wechat.NAME)) {
                dataList.add(R.drawable.share_weixin_icon);
                dataList.add(R.drawable.share_pengyouquan_icon);
            } else {
                dataList.add(R.drawable.weixin_login_gary_icon);
                dataList.add(R.drawable.share_pengyouquan_gray_icon);
            }
//            dataList.add(R.drawable.share_weibo_icon);
            dataList.add(R.drawable.share_msm_icon);
            if (isClientValid(QQ.NAME)) {
                dataList.add(R.drawable.share_qq_icon);
            } else {
                dataList.add(R.drawable.qq_login_gary_icon);
            }
//            dataList.add(R.drawable.share_copy_icon);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HolderView holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.layout_share_item, null);
                holder = new HolderView();

                holder.shareIcon = (ImageView) convertView.findViewById(R.id.share_item_shareIcon);
                holder.shareText = (TextView) convertView.findViewById(R.id.share_item_shareText);

                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }

            setViewMessage(holder.shareIcon, holder.shareText, position);
            serOnClickView(convertView, position);

            return convertView;
        }

        private void serOnClickView(View view, final int position) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (position) {
                        case 0:
                            if (isClientValid(Wechat.NAME)) {
                                shareWeixinBtn();
                            }
                            break;
                        case 1:
                            if (isClientValid(Wechat.NAME)) {
                                shareFriendBtn();
                            }
                            break;
//                        case 2:
//                            shareWeiboBtn();
//                            break;
                        case 2:
                            shareMsmBtn();
                            break;
                        case 3:
                            if (isClientValid(QQ.NAME)) {
                                shareQqBtn();
                            }
                            break;
                        default:
                            copy(url);
                            break;
                    }
                }
            });
        }

        private boolean isClientValid(String name) {
            return ShareSDK.getPlatform(context, name).isClientValid();
        }

        private void shareWeiboBtn() {
            SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
            sp.setText(getContentText() + " " + url);
            sp.setImagePath(BitmapHandler.getLogoForPath(context));

            Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            weibo.share(sp);
            MessageHandler.showToast(context, "分享到微博");

        }

        public void copy(String content) {
            ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            MessageHandler.showToast(context, "已复制至复制面板");
        }

        private void shareMsmBtn() {
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
            sendIntent.setData(Uri.parse("smsto:"));
            sendIntent.putExtra("sms_body", getContentText() + " " + url);
            startActivity(sendIntent);
        }

        private void shareQqBtn() {
            QQ.ShareParams sp = new QQ.ShareParams();
            sp.setTitle(TITIE_TEXT);
            sp.setText(getContentText());
            sp.setTitleUrl(url);
            sp.setImageData(BitmapHandler.getLogo(context));

            Platform qq = ShareSDK.getPlatform(QQ.NAME);
            qq.share(sp);
        }

        private void shareFriendBtn() {
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Wechat.SHARE_WEBPAGE);
            sp.setTitle(TITIE_TEXT);
            sp.setText(getContentText());
            sp.setUrl(url);
            sp.setImageData(BitmapHandler.getLogo(context));

            Platform wcshat = ShareSDK.getPlatform(WechatMoments.NAME);
            wcshat.share(sp);
        }

        private void shareWeixinBtn() {
            Wechat.ShareParams sp = new Wechat.ShareParams();
            sp.setShareType(Wechat.SHARE_WEBPAGE);
            sp.setTitle(TITIE_TEXT);
            sp.setText(getContentText());
            sp.setUrl(url);
            sp.setImageData(BitmapHandler.getLogo(context));

            Platform wcshat = ShareSDK.getPlatform(Wechat.NAME);
            wcshat.share(sp);
        }

        private String getContentText() {
            StringBuilder sb = new StringBuilder();

            sb.append("我在爱西语参加了《");
            if (title == null || title.equals("")) {
                sb.append(CONTENT_TEXT);
            } else {
                sb.append(title);
            }
            sb.append("》，你也快来一决高下吧！");

            return sb.toString();
        }

        private void setViewMessage(ImageView shareIcon, TextView shareText, int position) {
            int w = WinHandler.getWinWidth(context) / 5;
            int p = WinHandler.dipToPx(context, 8);
            shareIcon.setLayoutParams(new LinearLayout.LayoutParams(w, w));
            shareIcon.setPadding(p, p, p, p);
            shareIcon.setImageResource(dataList.get(position));

            shareText.setText(getShareText(position));
        }

        private String getShareText(int position) {
            switch (position) {
                case 0:
                    if (isClientValid(Wechat.NAME)) {
                        return "微信";
                    } else {
                        return "请先安装";

                    }
                case 1:
                    if (isClientValid(Wechat.NAME)) {
                        return "朋友圈";
                    } else {
                        return "请先安装";
                    }
//                case 2:
//                    return "微博";
                case 2:
                    return "短信";
                case 3:
                    if (isClientValid(QQ.NAME)) {
                        return "QQ";
                    } else {
                        return "请先安装";
                    }
                default:
                    return "复制网址";
            }
        }


        class HolderView {
            ImageView shareIcon;
            TextView shareText;
        }
    }

}
