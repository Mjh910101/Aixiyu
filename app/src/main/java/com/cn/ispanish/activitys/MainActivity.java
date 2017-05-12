package com.cn.ispanish.activitys;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.fragments.IndexFragment;
import com.cn.ispanish.fragments.MyselfFragment;
import com.cn.ispanish.fragments.OfflineFragment;
import com.cn.ispanish.fragments.PaperFragment;
import com.cn.ispanish.handlers.ColorHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.tools.PermissionsChecker;
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
 * Created by Hua on 17/3/6.
 */
public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE = 100201;

    private final static long EXITTIME = 2000;
    private long EXIT = 0;

    @ViewInject(R.id.main_indexText)
    private TextView indexText;
    @ViewInject(R.id.main_myselfText)
    private TextView myselfText;
    @ViewInject(R.id.main_offlineText)
    private TextView offlineText;
    @ViewInject(R.id.main_paperText)
    private TextView paperText;
    @ViewInject(R.id.main_indexIcon)
    private ImageView indexIcon;
    @ViewInject(R.id.main_myselfIcon)
    private ImageView myselfIcon;
    @ViewInject(R.id.main_offLineIcon)
    private ImageView offlineIcon;
    @ViewInject(R.id.main_paperIcon)
    private ImageView paperIcon;

    private FragmentManager fragmentManager;

    private IndexFragment indexFragment;
    private MyselfFragment myselfFragment;
    private OfflineFragment offlineFragment;
    private PaperFragment paperFragment;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private PermissionsChecker mPermissionChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPermissionChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
        switch (requestCode) {
            case LoginActivity.RequestCode:
            case UserCenterActivity.RequestCode:
                if (myselfFragment != null) {
                    myselfFragment.uploadData();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (System.currentTimeMillis() - EXIT < EXITTIME) {
                finish();
            } else {
                MessageHandler.showToast(context, "再次点击退出");
            }
            EXIT = System.currentTimeMillis();
        }
        return false;
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    private void initActivity() {
        fragmentManager = getFragmentManager();
        mPermissionChecker = new PermissionsChecker(context);

        onButton(findViewById(R.id.main_indexButton));
    }

    @OnClick({R.id.main_indexButton, R.id.main_myselfButton, R.id.main_offlineButton, R.id.main_paperButton})
    public void onButton(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initButton();
        hideFragments(transaction);
        switch (view.getId()) {
            case R.id.main_indexButton:
                onIndex(transaction);
                break;
            case R.id.main_myselfButton:
                onMyself(transaction);
                break;
            case R.id.main_offlineButton:
                onOffline(transaction);
                break;
            case R.id.main_paperButton:
                onPaper(transaction);
                break;
        }
        transaction.commit();
    }

    private void onPaper(FragmentTransaction transaction) {
        paperText.setTextColor(ColorHandle.getColorForID(context, R.color.black_text_24));
        paperIcon.setImageResource(R.drawable.paper_on_icon);

        if (paperFragment == null) {
            paperFragment = new PaperFragment();
            transaction.add(R.id.main_contentLayout, paperFragment);
        } else {
            transaction.show(paperFragment);
        }
    }

    private void onOffline(FragmentTransaction transaction) {
        offlineText.setTextColor(ColorHandle.getColorForID(context, R.color.black_text_24));
        offlineIcon.setImageResource(R.drawable.offline_on_icon);

        if (offlineFragment == null) {
            offlineFragment = new OfflineFragment();
            transaction.add(R.id.main_contentLayout, offlineFragment);
        } else {
            transaction.show(offlineFragment);
        }
    }

    private void onMyself(FragmentTransaction transaction) {
        myselfText.setTextColor(ColorHandle.getColorForID(context, R.color.black_text_24));
        myselfIcon.setImageResource(R.drawable.uesr_on_icon);

        if (myselfFragment == null) {
            myselfFragment = new MyselfFragment();
            transaction.add(R.id.main_contentLayout, myselfFragment);
        } else {
            transaction.show(myselfFragment);
        }
    }

    private void onIndex(FragmentTransaction transaction) {
        indexText.setTextColor(ColorHandle.getColorForID(context, R.color.black_text_24));
        indexIcon.setImageResource(R.drawable.home_on_icon);

        if (indexFragment == null) {
            indexFragment = new IndexFragment();
            transaction.add(R.id.main_contentLayout, indexFragment);
        } else {
            transaction.show(indexFragment);
        }
    }

    private void initButton() {
        indexText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        myselfText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        offlineText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));
        paperText.setTextColor(ColorHandle.getColorForID(context, R.color.gray_text_90));

        indexIcon.setImageResource(R.drawable.home_off_icon);
        myselfIcon.setImageResource(R.drawable.user_off_icon);
        offlineIcon.setImageResource(R.drawable.offline_off_icon);
        paperIcon.setImageResource(R.drawable.paper_off_icon);
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (indexFragment != null) {
            transaction.hide(indexFragment);
        }
        if (myselfFragment != null) {
            transaction.hide(myselfFragment);
        }
        if (offlineFragment != null) {
            transaction.hide(offlineFragment);
        }
        if (paperFragment != null) {
            transaction.hide(paperFragment);
        }
    }

}
