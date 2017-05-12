package com.cn.ispanish.handlers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MessageHandler {

    public static void logException(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
    }

    public static void showToast(Context context, String msg, int duration) {
        Toast.makeText(context, msg, duration).show();
    }

    public static void showToast(Context context, String msg) {
        if (context != null) {
            showToast(context, msg, 0);
        }
    }


    public static void showToast(Context context, JSONObject msg) {
        if (msg != null) {
            String m = JsonHandle.getString(msg, "message");
            if (!m.equals("") && !m.equals("null")) {
                MessageHandler.showToast(context, m);
            }
        }
    }

    public static void showFailure(Context context) {
        showToast(context, "网络不佳");
    }

    public static void showLast(Context context) {
        showToast(context, "没有数据了");
    }

    public static void showException(Context context, Exception e) {
        showToast(context, "网络不佳");
        logException(e);
    }

    public static boolean showException(Context context, JSONObject error) {
        if (error != null) {
            int code = JsonHandle.getInt(error, "error");
            String msg = getErrorMessage(code);
            if (msg != null) {
                Log.e("error", msg);
                if (!msg.equals("")) {
                    showToast(context, msg);
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isSdcardExisting(Context context) {
        final String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            MessageHandler.showToast(context, "找不到SD卡");
            return false;
        }
    }

    public static Map<Integer, String> messageMap;

    public static String getErrorMessage(int code) {
        if (messageMap == null) {
            initMessageMap();
        }
        return messageMap.get(code);
    }

    private static void initMessageMap() {
        messageMap = new HashMap<>();
        messageMap.put(1000, "用户密码错误");
        messageMap.put(1001, " 用户名不存在或密码错误");
        messageMap.put(1002, " 注册用户名非法");
        messageMap.put(1003, " 请求超时");
        messageMap.put(1004, " 请求参数类型异常");
        messageMap.put(1005, " 数据不存在");
        messageMap.put(1006, " 视频不存在");
        messageMap.put(1007, " 缺少参数");
        messageMap.put(1008, " 注册类型异常");
        messageMap.put(1009, " 验证码错误");
        messageMap.put(1010, " 注册失败");
        messageMap.put(1011, " 生成appkey失败");
        messageMap.put(1012, " 用户没有购买课程");
        messageMap.put(1013, " 成功（用于各种操作的成功提示）");
        messageMap.put(1014, " 失败（用于各种操作的失败提示）");
        messageMap.put(1015, " 注册手机号已经存在");
        messageMap.put(1016, " App课程购买成功");
        messageMap.put(1017, " 购买记录添加失败");
        messageMap.put(1018, " 课程已经购买");
        messageMap.put(1019, " app商店课程恢复成功");
        messageMap.put(1020, " app商店课程恢复失败");
        messageMap.put(1021, " 购点成功");
        messageMap.put(1022, " 购点失败");
        messageMap.put(1023, " 购点保存充值信息成功，更新金额失败");
        messageMap.put(1024, " 信息已存在，不能使用同一记录重复购买");
        messageMap.put(1025, " 扣除金额时出错");
        messageMap.put(1026, " 金额不够购买课程");
        messageMap.put(1027, " 已经有相同appid的用户");
        messageMap.put(1031, " 成功");
        messageMap.put(1032, " 失败");
        messageMap.put(1033, " 合并课程成功，且删除游客用户成功");
        messageMap.put(1034, " 合并课程、删除游客用户失败");
        messageMap.put(1035, " 该游客没有课程，不须合并");
        messageMap.put(1036, " qq第三方通过openid注册失败");
        messageMap.put(1037, " 会员已过期");

    }

}
