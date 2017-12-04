package com.cn.ispanish.handlers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class SystemHandle {

    public final static String NAME = "Aixiyu";
    public final static int MODE = Activity.MODE_PRIVATE;

    private final static String IS_SHOW = "IS_SHOW";
    public final static String LUCK_TIME = "luck_time";
    private final static String IS_FLAG_SECURE = "IS_FLAG_SECURE";
    private final static String FIRST_KEY = "FIRST_KEY";
    private final static String ISDOWNLOAD = "Is_Download";

    public static void saveIntMessage(Context context, String ksy, int i) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putInt(ksy, i);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getInt(key, 0);
    }

    // *************************************************************************

    public static void saveStringMessage(Context context, String ksy, String v) {
        if (v == null | v.equals("null")) {
            v = "";
        }
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putString(ksy, v);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getString(key, "");
    }

    // *************************************************************************

    public static void saveBooleanMessage(Context context, String ksy, boolean b) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putBoolean(ksy, b);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getBoolean(key, false);
    }

    // **************************************
    public static void saveIsFlagSecure(Context context, boolean isFlagSecure) {
        saveBooleanMessage(context, IS_FLAG_SECURE, isFlagSecure);
    }

    public static boolean isFlagSecure(Context context) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getBoolean(IS_FLAG_SECURE, true);
    }

    // **************************************
    public static void saveIsDownload(Context context, boolean isDownload) {
        saveBooleanMessage(context, ISDOWNLOAD, isDownload);
    }

    public static boolean getIsDownload(Context context) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getBoolean(ISDOWNLOAD, false);
    }

    // *************************************************************************

    public static void saveLongMessage(Context context, String ksy, long l) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        Editor editor = pref.edit();
        editor.putLong(ksy, l);
        editor.commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(NAME, MODE);
        return pref.getLong(key, 0);
    }

    // *************************************************************************

    public static boolean getIsGoneShow(Context context) {
        return getBoolean(context, IS_SHOW);
    }

    public static void saveIsGoneShow(Context context, boolean b) {
        saveBooleanMessage(context, IS_SHOW, b);
    }

    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return "当前版本号:" + version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean detectionVersion(Context context, int v) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            String versionName = packInfo.versionName;
            int versionCode = packInfo.versionCode;
            Log.e("", versionName + " : " + versionCode + " : " + v);
            if (versionCode < v) {
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isFirst(Context context) {
        return !getBoolean(context, FIRST_KEY + "_" + getVersionCode(context));
    }

    public static void setFirst(Context context) {
        saveBooleanMessage(context, FIRST_KEY + "_" + getVersionCode(context), true);
    }

    public static boolean isDubug(Context context) {
        return getBoolean(context, "isDebug");
    }

    public static void setDebug(Context context, boolean b) {
        saveBooleanMessage(context, "isDebug", b);
    }
}
