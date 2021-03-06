package com.cn.ispanish.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;


import com.cn.ispanish.activitys.FirstActivity;
import com.cn.ispanish.download.DownloadImageLoader;

import java.io.File;
import java.util.List;

public class PassagewayHandler {

    public static final int CAMERA_REQUEST_CODE = 322;
    public static final int IMAGE_REQUEST_CODE = 323;
    public static final int RESULT_REQUEST_CODE = 321;

    public static void resizeImage(Context context, Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
        intent.setDataAndType(GetPathFromUriHandler.getUri((Activity) context, uri), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        ((Activity) (context)).startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    public static void selectImage(Context context, Uri imageUri) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        galleryIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) (context)).startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public static void selectImage(Context context) {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        ((Activity) (context)).startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE);
    }

    public static String takePhoto(Context context) {
        String fileName = "pic_" + DateHandle.getTime() + ".jpg";
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri(fileName));
        cameraIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
        ((Activity) (context)).startActivityForResult(cameraIntent,
                CAMERA_REQUEST_CODE);
        return fileName;
    }

    private static Uri getImageUri(String fileName) {
        return Uri.fromFile(new File(DownloadImageLoader.getImagePath(), fileName));
    }

    /**
     * @param context
     * @param cls
     */
    public static void jumpActivity(Context context, Class<?> cls) {
        jumpActivity(context, cls, -1, null, -1);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode) {
        jumpActivity(context, cls, requestCode, null, -1);
    }

    /**
     * @param context
     * @param cls
     * @param bundle
     */
    public static void jumpActivity(Context context, Class<?> cls, Bundle bundle) {
        jumpActivity(context, cls, -1, bundle, -1);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     * @param bundle
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode, Bundle bundle) {
        jumpActivity(context, cls, requestCode, bundle, -1);
    }

    public static void jumpDialing(Context context, String tel) {
        Uri uri = Uri.parse("tel:" + tel);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param flagActivityClearTop
     * @param cls
     */
    public static void jumpActivity(Context context, int flagActivityClearTop,
                                    Class<?> cls) {
        jumpActivity(context, cls, -1, null, flagActivityClearTop);
    }

    public static void jumpActivity(Context context, int flagActivityClearTop,
                                    Class<?> cls, Bundle bundle) {
        jumpActivity(context, cls, -1, bundle, flagActivityClearTop);
    }

    /**
     * @param context
     * @param cls
     * @param bundle
     * @param flagActivityClearTop
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    Bundle bundle, int flagActivityClearTop) {
        jumpActivity(context, cls, -1, bundle, flagActivityClearTop);
    }

    /**
     * @param context
     * @param cls
     * @param requestCode
     * @param bundle
     * @param flagActivityClearTop
     */
    public static void jumpActivity(Context context, Class<?> cls,
                                    int requestCode, Bundle bundle, int flagActivityClearTop) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (flagActivityClearTop > 0) {
            intent.addFlags(flagActivityClearTop);
        }
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ((Activity) (context)).startActivityForResult(intent, requestCode);
    }

    /**
     * 忽略中间页面，直接跳转
     *
     * @param context
     * @param cls
     */
    public static void jumpToActivity(Context context, Class<?> cls) {
        jumpActivity(context, Intent.FLAG_ACTIVITY_CLEAR_TOP, cls);
        ((Activity) context).finish();
    }

    public static void jumpToActivity(Context context, Class<?> cls, Bundle b) {
        jumpActivity(context, Intent.FLAG_ACTIVITY_CLEAR_TOP, cls, b);
        ((Activity) context).finish();
    }

    public static boolean jumpFirstActivity(Context context) {
        if (SystemHandle.isFirst(context)) {
            jumpActivity(context, FirstActivity.class);
            return true;
        }

        return false;
    }

    public static void jumpQQ(Context context, String uin) {
        boolean isCan = false;
        try {
            Log.e("", "uin = " + uin);
            if (isQQClientAvailable(context)) {
                isCan = true;
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + uin)));
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
        if (!isCan) {
            MessageHandler.showToast(context, "您的QQ版本过低或您当前未安装QQ,请安装最新版QQ后再试");
        }
    }

    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }
}
