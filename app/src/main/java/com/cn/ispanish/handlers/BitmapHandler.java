package com.cn.ispanish.handlers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;


import com.cn.ispanish.download.DownloadImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapHandler {

    public static byte[] bmpToByte(Bitmap bmp) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, bos);
        byte[] b = bos.toByteArray();
        bmp.recycle();
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static Bitmap getLogo(Context context) {
        Bitmap image = null;
        try {
            InputStream is = context.getAssets().open("logo.png");
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Deprecated
    public static String getLogoForPath(Context context) {
        String path = DownloadImageLoader.getImagePath() + "/logo.png";
        File f = new File(path);
        if (!f.exists()) {
            Bitmap b = getLogo(context);
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(f);
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return path;

    }

    /**
     * 根据指定的view截图
     *
     * @param v 要截图的view
     * @return Bitmap
     */
    public static Bitmap getViewBitmap(View v) {
        if (null == v) {
            return null;
        }
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        if (Build.VERSION.SDK_INT >= 11) {
            v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
            v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
        } else {
            v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        }

        Bitmap bitmap = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        v.setDrawingCacheEnabled(false);
        v.destroyDrawingCache();
        return bitmap;
    }

}
