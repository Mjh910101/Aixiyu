package com.cn.ispanish.activitys;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.dialog.ListDialog;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.PassagewayHandler;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.jzxiang.pickerview.TimePickerDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import chihane.jdaddressselector.BottomDialog;
import chihane.jdaddressselector.OnAddressSelectedListener;
import chihane.jdaddressselector.model.City;
import chihane.jdaddressselector.model.County;
import chihane.jdaddressselector.model.Province;
import chihane.jdaddressselector.model.Street;


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
 * Created by Hua on 17/3/8.
 */
public class UserCenterActivity extends BaseActivity {

    public static final int RequestCode = 12012;

    @ViewInject(R.id.title_titleText)
    public TextView titleText;
    @ViewInject(R.id.userCenter_portrait)
    private ImageView portrait;
    @ViewInject(R.id.userCenter_nameInput)
    private EditText nameInput;
    @ViewInject(R.id.userCenter_birthdateText)
    private TextView birthdateText;
    @ViewInject(R.id.userCenter_cityText)
    private TextView cityText;
    @ViewInject(R.id.view_progress)
    private ProgressBar progress;

    private String imageName;
    private File imageFile;
    private long start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_center);

        ViewUtils.inject(this);

        initActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        } else {
            switch (requestCode) {
                case PassagewayHandler.IMAGE_REQUEST_CODE:
                    if (data != null) {
                        resizeImage(data.getData());
                    }
                    break;
                case PassagewayHandler.CAMERA_REQUEST_CODE:
                    if (MessageHandler.isSdcardExisting(context)) {
                        resizeImage(getImageUri());
                    }
                    break;
                case PassagewayHandler.RESULT_REQUEST_CODE:
                    if (data != null) {
                        getResizeImage(data);
                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.title_backIcon)
    public void onBack(View view) {
        finish();
    }

    @OnClick(R.id.userCenter_portraitButton)
    public void onPic(View view) {
        showPicList();
    }

    @OnClick(R.id.userCenter_birthdateText)
    public void onBirthdate(View view) {
        Calendar c = Calendar.getInstance();
        Dialog dateDialog = new DatePickerDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {
            boolean mFired = true;

            @Override
            public void onDateSet(DatePicker picker, int y, int m, int d) {
                if (mFired) {
                    mFired = false;
                    birthdateText.setText(y + "-" + (m + 1) + "-" + d);
                }
            }

        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dateDialog.setTitle("请选择日期");
        dateDialog.setCancelable(false);
        dateDialog.show();
    }

    @OnClick(R.id.userCenter_cityText)
    public void onCity(View view) {
        final BottomDialog dialog = new BottomDialog(context);
        dialog.setOnAddressSelectedListener(new OnAddressSelectedListener() {
            @Override
            public void onAddressSelected(Province province, City city, County county, Street street) {
                String address =
                        (province == null ? "" : province.name)
                                + (city == null ? "" : city.name)
                                + (county == null ? "" : county.name)
                                + (street == null ? "" : street.name);
                cityText.setText(address);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @OnClick(R.id.userCenter_saveButton)
    public void onSave(View view) {
        saveInfo();
    }

    private void initActivity() {

        titleText.setText("个人信息");

        setUserPic();

        nameInput.setText(User.getName(context));
        birthdateText.setText(User.getBirthdate(context));
        cityText.setText(User.getAddress(context));
    }

    private void setUserPic() {
        DownloadImageLoader.loadImage(portrait, User.getPortrait(context), WinHandler.dipToPx(context, 25));
    }

    private void inputParam(RequestParams params, String key, String v) {
        if (v == null) {
            return;
        }
        if (v.equals("")) {
            return;
        }
        params.addBodyParameter(key, v);
    }

    private void saveInfo() {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("name", TextHandler.getText(nameInput));
        params.addBodyParameter("birthdate", TextHandler.getText(birthdateText));
        params.addBodyParameter("address", TextHandler.getText(cityText));
        params.addBodyParameter("key", User.getAppKey(context));

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getUserInfoset(), params,
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
                            MessageHandler.showException(context, json);
                            if (JsonHandle.getString(json, "error").equals("1031")) {
                                User.saveName(context, TextHandler.getText(nameInput));
                                User.saveAddress(context, TextHandler.getText(cityText));
                                User.saveBirthdate(context, TextHandler.getText(birthdateText));
                                close();
                            }

                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

    private void close() {
        setResult(RequestCode);
        finish();
    }

    private void showPicList() {
        final List<String> msgList = getMsgList();
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(msgList);
        start = System.currentTimeMillis();
        dialog.setItemListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                if (msgList.get(position).equals("拍照")) {
                    takePhoto();
                } else {
                    selectImage();
                }
                dialog.dismiss();
            }

        });
    }

    private void takePhoto() {
        imageName = PassagewayHandler.takePhoto(context);
    }


    private void selectImage() {
        PassagewayHandler.selectImage(context);
    }

    public List<String> getMsgList() {
        List<String> list = new ArrayList<String>();
        list.add("拍照");
        list.add("本地相册");
        return list;
    }

    private Uri getImageUri() {
        return Uri.fromFile(new File(DownloadImageLoader.getImagePath(),
                imageName));
    }

    private void resizeImage(Uri uri) {
        PassagewayHandler.resizeImage(context, uri);
    }

    private File getImageFile() {
        String name = "head_" + start + ".jpg";
        return new File(DownloadImageLoader.getImagePath(), name);
    }

    private void getResizeImage(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            Drawable drawable = new BitmapDrawable(photo);
            FileOutputStream foutput = null;
            try {
                foutput = new FileOutputStream(getImageFile());
                photo.compress(Bitmap.CompressFormat.JPEG, 100, foutput);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (null != foutput) {
                    try {
                        foutput.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            uploadUserPortrait(getImageFile());
//            DownloadImageLoader.loadImageForFile(portrait, getImageFile()
//                    .toString(),WinHandler.dipToPx(context, 25));
        }
    }

    private void uploadUserPortrait(File imageFile) {
        progress.setVisibility(View.VISIBLE);

        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("key", User.getAppKey(context));
        params.addBodyParameter("photo", imageFile);

        HttpUtilsBox.getHttpUtil().send(HttpMethod.POST, UrlHandle.getUpportrait(), params,
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
                            if (!MessageHandler.showException(context, json)) {
                                String portrait = JsonHandle.getString(json, "portrait");
                                User.savePortrait(context, portrait);
                                setUserPic();
                            }
                        }
                        progress.setVisibility(View.GONE);
                    }
                });
    }

}
