package com.cn.ispanish.handlers;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

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
 * Created by Hua on 16/12/3.
 */
public class TextHandler {

    public static String getText(EditText input) {
        return input.getText().toString();
    }

    public static String getText(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public static String getText(TextView view) {
        return view.getText().toString();
    }

    public static Object getTextForUtf8(String s) {
        try {
            return new String(s.getBytes(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }
}
