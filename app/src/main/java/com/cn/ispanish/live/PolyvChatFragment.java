package com.cn.ispanish.live;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.cn.ispanish.ISpanishApplication;
import com.cn.ispanish.R;
import com.cn.ispanish.box.User;
import com.cn.ispanish.download.DownloadImageLoader;
import com.cn.ispanish.fragments.BaseFragment;
import com.cn.ispanish.handlers.DateHandle;
import com.cn.ispanish.handlers.JsonHandle;
import com.cn.ispanish.handlers.MessageHandler;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.handlers.TextHandler;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.http.HttpUtilsBox;
import com.cn.ispanish.http.UrlHandle;
import com.cn.ispanish.topup.weixin.MD5;
import com.cn.ispanish.views.AutoScrollTextView;
import com.cn.ispanish.views.LiveGiftView;
import com.easefun.polyvsdk.live.chat.PolyvChatManager;
import com.easefun.polyvsdk.live.chat.PolyvChatMessage;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifEditText;
import pl.droidsonroids.gif.GifImageSpan;
import pl.droidsonroids.gif.RelativeImageSpan;


/**
 * 聊天Fragment
 */
public class PolyvChatFragment extends BaseFragment implements OnClickListener {
    private static final int DISCONNECT = 5;
    private static final int RECEIVEMESSAGE = 6;
    private static final int LOGINING = 12;
    private static final int LOGINSUCCESS = 13;
    private static final int RECONNECTING = 19;
    private static final int RECONNECTSUCCESS = 30;
    // fragmentView
    private View view;
    // 聊天listView
    private ListView lv_chat;
    // 聊天listView的适配器
    private PolyvChatAdapter polyvChatAdapter;
    // 聊天信息列表集合
    private List<PolyvChatMessage> messages;

    // 空信息控件，聊天室状态
    private TextView tv_empty, tv_status, teacherName;
    private AutoScrollTextView gongGaoText;
    private ImageView teacherPic, giftButton;
    // 信息编辑控件
    private GifEditText et_talk;
    // 聊天管理类
    private PolyvChatManager chatManager;
    // 用户id，频道id，昵称(自定义)
    private String userId, roomId, nickName, userPic;
    private Animation collapseAnimation;
    private User teacher;

    // 表情ViewPager
    private ViewPager vp_emo;
    private LiveGiftView giftView;
    // 表情的父布局
    private RelativeLayout rl_bot;
    // 表情页的下方圆点...，表情开关，发送按钮
    private ImageView iv_page1, iv_page2, iv_page3, iv_page4, iv_page5, iv_emoswitch, iv_send;
    // 表情的文本长度
    private int emoLength;
    // 列
    private int columns = 7;
    // 行
    private int rows = 3;
    // 页
    private int pages = 5;
    // 是否是ppt直播，ppt直播无需操作聊天室实例
    private boolean isPPTLive;

    public void setIsPPTLive(boolean isPPTLive) {
        this.isPPTLive = isPPTLive;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECEIVEMESSAGE:
                    PolyvChatMessage chatMessage = (PolyvChatMessage) msg.obj;
                    if (chatMessage.getChatType() == PolyvChatMessage.CHATTYPE_RECEIVE) {

                        if (chatMessage.getValues() != null) {
                            chatMessage.getValues()[0] = chatMessage.getValues()[0].replace("<img src=\"http://www.ispanish.cn/Public/Home/Images/live/onplay_11.png\">", "[玫瑰]");
                        }

                        if (chatMessage.getUser() != null) {
                            if (chatMessage.getUser().getUserType() != null) {
                                if (chatMessage.getUser().getUserType().equals("manager") && chatMessage.getValues() != null) {
                                    gongGaoText.initScrollTextView(getActivity().getWindowManager(), "公告：" + chatMessage.getValues()[0]);
                                    gongGaoText.starScroll();
                                }
                            }
                        }

                    } else if (chatMessage.getChatType() == PolyvChatMessage.CHATTYPE_RECEIVE_NOTICE) {
                        switch (chatMessage.getEvent()) {
                            // 用户被踢，不能发送信息，再次连接聊天室可恢复
                            case PolyvChatMessage.EVENT_KICK:
                                String nick = chatMessage.getUser().getNick();
                                if (chatMessage.getUser().getUserId().equals(userId))
                                    nick = nick + "(自己)";
                                // 这里需要自定义显示的信息
                                chatMessage.setValues(new String[]{nick + "被踢"});
                                break;
                            // 用户被禁言，不能接收或发送信息，不能再次连接聊天室，需要在后台恢复
                            case PolyvChatMessage.EVENT_SHIELD:
                                String nick2 = chatMessage.getUser().getNick();
                                if (chatMessage.getUser().getUserId().equals(userId))
                                    nick2 = nick2 + "(自己)";
                                chatMessage.setValues(new String[]{nick2 + "被禁言"});
                                break;
                            // 聊天室关闭时，不能接收或发送信息
                            case PolyvChatMessage.EVENT_CLOSEROOM:
                                boolean isClose = chatMessage.getValue().isClosed();
                                if (isClose)
                                    chatMessage.setValues(new String[]{"聊天室关闭"});
                                else
                                    chatMessage.setValues(new String[]{"聊天室开启"});
                                break;
                            // 公告
                            case PolyvChatMessage.EVENT_GONGGAO:
//                                gongGaoText.setText("公告：" + chatMessage.getContent());
                                gongGaoText.initScrollTextView(getActivity().getWindowManager(), "公告：" + chatMessage.getContent());
                                gongGaoText.starScroll();
                                chatMessage.setValues(new String[]{""});
//                                chatMessage.setValues(new String[]{"公告 " + chatMessage.getContent()});
                                break;
                        }
                    }

                    polyvChatAdapter.add(chatMessage);

                    break;
                case DISCONNECT:
                    // ((PolyvChatManager.ConnectStatus) msg.obj).getDescribe()的值与PolyvCahtManager的DISCONNECT_常量对应
                    tv_status.setText("连接失败(" + ((PolyvChatManager.ConnectStatus) msg.obj).getDescribe() + ")");
//                    tv_status.setVisibility(View.VISIBLE);
                    break;
                case LOGINING:
                    tv_status.setText("正在登录中...");
//                    tv_status.setVisibility(View.VISIBLE);
                    break;
                case LOGINSUCCESS:
                    tv_status.setText("登录成功");
                    tv_status.clearAnimation();
                    tv_status.startAnimation(collapseAnimation);
//                    lv_chat.setVisibility(View.VISIBLE);
                    break;
                case RECONNECTING:
                    tv_status.setText("正在重连中...");
//                    tv_status.setVisibility(View.VISIBLE);
                    break;
                case RECONNECTSUCCESS:
                    tv_status.setText("重连成功");
                    tv_status.clearAnimation();
                    tv_status.startAnimation(collapseAnimation);
                    break;
            }
        }
    };

    /**
     * ppt直播初始化聊天室配置信息
     *
     * @param chatManager
     */
    public void initPPTLiveChatConfig(@NonNull final PolyvChatManager chatManager, String userId, String roomId, String nickName, String userPic, User teacher) {
        this.userId = userId;
        this.roomId = roomId;
        this.nickName = nickName;
        this.userPic = userPic;
        this.chatManager = chatManager;
        this.teacher = teacher;

        this.chatManager.setOnChatManagerListener(new PolyvChatManager.ChatManagerListener() {

            @Override
            public void connectStatus(PolyvChatManager.ConnectStatus connect_status) {
                switch (connect_status) {
                    case DISCONNECT:
                        handler.sendMessage(handler.obtainMessage(DISCONNECT, connect_status.DISCONNECT));
                        break;
                    case LOGINING:
                        handler.sendEmptyMessage(LOGINING);
                        break;
                    case LOGINSUCCESS:
                        handler.sendEmptyMessage(LOGINSUCCESS);
                        break;
                    case RECONNECTING:
                        try {
                            Field field = chatManager.getClass().getDeclaredField("canSend");
                            field.setAccessible(true);
                            field.set(chatManager, true);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(RECONNECTING);
                        break;
                    case RECONNECTSUCCESS:
                        handler.sendEmptyMessage(RECONNECTSUCCESS);
                        break;
                }
            }

            @Override
            public void receiveChatMessage(PolyvChatMessage chatMessage) {
                Message message = handler.obtainMessage();
                message.obj = chatMessage;
                message.what = RECEIVEMESSAGE;
                handler.sendMessage(message);
            }
        });
    }

    /**
     * 非ppt直播，初始化聊天室配置
     *
     * @param userId   用户id
     * @param roomId   房间id
     * @param nickName 昵称
     * @param userPic
     * @param teacher
     */
    public void initChatConfig(String userId, String roomId, String nickName, String userPic, User teacher) {
        initPPTLiveChatConfig(new PolyvChatManager(), userId, roomId, nickName, userPic, teacher);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.polyv_fragment_chat, null);
        return view;
    }

    //根据索引获取表情页的view
    private View getEmoGridView(int position) {
        GridView gv_emo = (GridView) LayoutInflater.from(getContext()).inflate(R.layout.polyv_gridview_emo, null)
                .findViewById(R.id.gv_emo);
        List<String> lists = new ArrayList<String>();
        lists.addAll(PolyvFaceManager.getInstance().getFaceMap().keySet());
        final List<String> elists = lists.subList(position * (columns * rows), (position + 1) * (columns * rows));
        PolyvEmoGridViewAdapter emoGridViewAdapter = new PolyvEmoGridViewAdapter(elists, getContext());
        gv_emo.setAdapter(emoGridViewAdapter);
        gv_emo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (position == elists.size() - 1)
                    deleteEmoText();
                else
                    appendEmo(elists.get(position));
            }
        });
        return gv_emo;
    }

    private void findIdAndNew() {
        lv_chat = (ListView) view.findViewById(R.id.lv_chat);
        tv_empty = (TextView) view.findViewById(R.id.tv_empty);
        tv_status = (TextView) view.findViewById(R.id.tv_status);
        gongGaoText = (AutoScrollTextView) view.findViewById(R.id.polyvChat_teacherLayout_gongGaoText);
        teacherName = (TextView) view.findViewById(R.id.polyvChat_teacherLayout_teacherName);
        giftButton = (ImageView) view.findViewById(R.id.iv_gift);
        teacherPic = (ImageView) view.findViewById(R.id.polyvChat_teacherLayout_teacherPic);
        iv_send = (ImageView) view.findViewById(R.id.iv_send);
        et_talk = (GifEditText) view.findViewById(R.id.et_talk);
        vp_emo = (ViewPager) view.findViewById(R.id.vp_emo);
        iv_page1 = (ImageView) view.findViewById(R.id.iv_page1);
        iv_page2 = (ImageView) view.findViewById(R.id.iv_page2);
        iv_page3 = (ImageView) view.findViewById(R.id.iv_page3);
        iv_page4 = (ImageView) view.findViewById(R.id.iv_page4);
        iv_page5 = (ImageView) view.findViewById(R.id.iv_page5);
        iv_emoswitch = (ImageView) view.findViewById(R.id.iv_emoswitch);
        rl_bot = (RelativeLayout) view.findViewById(R.id.rl_bot);
        giftView = (LiveGiftView) view.findViewById(R.id.polyvChat_gifeView);
        messages = new ArrayList<PolyvChatMessage>();
        collapseAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.polyv_collapse);
        collapseAnimation.setAnimationListener(new ViewAnimationListener(tv_status));
    }

    private class ViewAnimationListener implements AnimationListener {
        private View view;

        public ViewAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            view.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    // 删除表情
    private void deleteEmoText() {
        int start = et_talk.getSelectionStart();
        int end = et_talk.getSelectionEnd();
        if (end > 0) {
            if (start != end) {
                et_talk.getText().delete(start, end);
            } else if (isEmo(end)) {
                et_talk.getText().delete(end - emoLength, end);
            } else {
                et_talk.getText().delete(end - 1, end);
            }
        }
    }

    //判断是否是表情
    private boolean isEmo(int end) {
        String preMsg = et_talk.getText().subSequence(0, end).toString();
        int regEnd = preMsg.lastIndexOf("]");
        int regStart = preMsg.lastIndexOf("[");
        if (regEnd == end - 1 && regEnd - regStart >= 2) {
            String regex = preMsg.substring(regStart);
            emoLength = regex.length();
            if (PolyvFaceManager.getInstance().getFaceId(regex) != -1)
                return true;
        }
        return false;
    }

    //添加表情
    private void appendEmo(String emoKey) {
        SpannableStringBuilder span = new SpannableStringBuilder(emoKey);
        int textSize = (int) et_talk.getTextSize();
        Drawable drawable = null;
        ImageSpan imageSpan = null;
        try {
            drawable = new GifDrawable(getResources(), PolyvFaceManager.getInstance().getFaceId(emoKey));
            imageSpan = new GifImageSpan(drawable, RelativeImageSpan.ALIGN_CENTER);
        } catch (NotFoundException | IOException e) {
            drawable = getResources().getDrawable(PolyvFaceManager.getInstance().getFaceId(emoKey));
            imageSpan = new RelativeImageSpan(drawable, RelativeImageSpan.ALIGN_CENTER);
        }
        drawable.setBounds(0, 0, textSize + 8, textSize + 8);
        span.setSpan(imageSpan, 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int selectionStart = et_talk.getSelectionStart();
        int selectionEnd = et_talk.getSelectionEnd();
        if (selectionStart != selectionEnd)
            et_talk.getText().replace(selectionStart, selectionEnd, span);
        else
            et_talk.getText().insert(selectionStart, span);
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        // 登录聊天室
        if (!isPPTLive && chatManager != null)
            chatManager.login(userId, roomId, nickName, userPic);

        try {
            Field field = chatManager.getClass().getDeclaredField("canSend");
            field.setAccessible(true);
            field.set(chatManager, true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        polyvChatAdapter = new PolyvChatAdapter(getContext(), messages, lv_chat);
        polyvChatAdapter.setChatManager(chatManager);
        polyvChatAdapter.setOnItemClickListener(new PolyvChatAdapter.OnItemClickListener() {

            @Override
            public void onClick(View view) {
                closeKeybordAndEmo(et_talk, getContext());
            }
        });
        lv_chat.setEmptyView(tv_empty);
        lv_chat.setAdapter(polyvChatAdapter);
        lv_chat.setSelection(polyvChatAdapter.getCount() - 1);
        lv_chat.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    closeKeybordAndEmo(et_talk, getContext());
                }
                return false;
            }
        });
        iv_send.setOnClickListener(this);
        // 表情
        List<View> lists = new ArrayList<>();
        for (int i = 0; i < pages; i++) {
            lists.add(getEmoGridView(i));
        }
        PolyvEmoPagerAdapter emoPagerAdapter = new PolyvEmoPagerAdapter(lists, getContext());
        vp_emo.setAdapter(emoPagerAdapter);
        vp_emo.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                resetPageImageView();
                switch (arg0) {
                    case 0:
                        iv_page1.setSelected(true);
                        break;
                    case 1:
                        iv_page2.setSelected(true);
                        break;
                    case 2:
                        iv_page3.setSelected(true);
                        break;
                    case 3:
                        iv_page4.setSelected(true);
                        break;
                    case 4:
                        iv_page5.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        iv_page1.setSelected(true);
        iv_page1.setOnClickListener(this);
        iv_page2.setOnClickListener(this);
        iv_page3.setOnClickListener(this);
        iv_page4.setOnClickListener(this);
        iv_page5.setOnClickListener(this);
        iv_emoswitch.setOnClickListener(this);
        et_talk.setOnClickListener(this);

        if (teacher != null) {
            DownloadImageLoader.loadImage(teacherPic, "http://www.ispanish.cn" + teacher.getPortrait(), WinHandler.dipToPx(getActivity(), 25));
            teacherName.setText(teacher.getName());
        }

        giftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                giftView.showGift();
            }
        });

    }

    private void resetPageImageView() {
        iv_page1.setSelected(false);
        iv_page2.setSelected(false);
        iv_page3.setSelected(false);
        iv_page4.setSelected(false);
        iv_page5.setSelected(false);
    }

    /**
     * 重置表情布局的可见性
     *
     * @param isgone 以隐藏方式
     */
    public void resetEmoLayout(boolean isgone) {
        if (rl_bot.getVisibility() == View.VISIBLE || isgone) {
            rl_bot.setVisibility(View.GONE);
        } else {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            rl_bot.setVisibility(View.VISIBLE);
            closeKeybord(et_talk, getContext());
            et_talk.requestFocus();
        }
    }

    // 发送聊天信息
    private void sendMsg() {
        String msg = et_talk.getText().toString();
        if (msg.trim().length() == 0) {
            Toast.makeText(getContext(), "发送信息不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        final PolyvChatMessage message = new PolyvChatMessage(msg);
        polyvChatAdapter.add(message);
        final int lastPosition = polyvChatAdapter.getCount() - 1;
        if (chatManager.sendChatMsg(message)) {
            polyvChatAdapter.updateStatusView(true, false, lastPosition);
        } else {
            polyvChatAdapter.updateStatusView(false, false, lastPosition);
        }
        lv_chat.setSelection(lastPosition);
        et_talk.setText("");
        closeKeybordAndEmo(et_talk, getContext());
    }

    //关闭键盘和表情布局
    private void closeKeybordAndEmo(EditText mEditText, Context mContext) {
        closeKeybord(mEditText, mContext);
        resetEmoLayout(true);
    }

    //关闭键盘
    private void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    //表情布局是否可见
    public boolean emoLayoutIsVisible() {
        return rl_bot.getVisibility() == View.VISIBLE ? true : false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findIdAndNew();
        initView();
        dowmloadData();
    }

    private String getToday() {
        String t = DateHandle.format(DateHandle.getToday().getTimeInMillis(), DateHandle.DATESTYP_4);
        Log.d("", t);
        return t;
    }

    private String getSevenDaysAgo() {
        Calendar c = DateHandle.getToday();
        c.add(Calendar.DATE, -7);
        String t = DateHandle.format(c.getTimeInMillis(), DateHandle.DATESTYP_4);
        Log.d("", t);
        return t;
    }

    private String getSign(String appSecret, String appId, String endDay, String startDay, String timestatmp) {

        StringBuffer sb = new StringBuffer();

        sb.append(appSecret);
        sb.append("appId");
        sb.append(appId);
        sb.append("endDay");
        sb.append(endDay);
        sb.append("startDay");
        sb.append(startDay);
        sb.append("timestamp");
        sb.append(timestatmp);
        sb.append(appSecret);

        String sign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();

        Log.d("", sb.toString());
        Log.d("", sign);

        return sign;
    }

    private void dowmloadData() {
        String appSecret = ISpanishApplication.getLiveAppSecret();
        String appId = ISpanishApplication.getLiveAppId();
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000 * 1000);
        String startDay = getSevenDaysAgo();
        String dayText = getToday();


        RequestParams params = HttpUtilsBox.getRequestParams(context);
        params.addBodyParameter("appId", appId);
        params.addBodyParameter("startDay", startDay);
        params.addBodyParameter("endDay", dayText);
        params.addBodyParameter("timestamp", timestamp);
        params.addBodyParameter("sign", getSign(appSecret, appId, dayText, startDay, timestamp));


        HttpUtilsBox.getHttpUtil().send(HttpRequest.HttpMethod.POST, UrlHandle.getChannelHistory(roomId), params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException exception,
                                          String msg) {
                        MessageHandler.showFailure(context);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        String result = responseInfo.result;
                        Log.d("", result);

                        JSONObject json = JsonHandle.getJSON(result);
                        if (json != null) {
                            setHistory(JsonHandle.getArray(json, "data"));
                        }
                    }
                });
    }

    private void setHistory(JSONArray data) {
        if (data == null || polyvChatAdapter == null) {
            return;
        }

        List<PolyvChatMessage> dataList = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            PolyvChatMessage chatMessage = PolyvChatMessage.fromJsonObject(JsonHandle.getJSON(data, i));
            chatMessage.setChatType(PolyvChatMessage.CHATTYPE_RECEIVE);
            chatMessage.setValues(new String[]{chatMessage.getContent()});
            if (chatMessage.getValues() != null) {
                chatMessage.getValues()[0] = chatMessage.getValues()[0].replace("<img src=\"http://www.ispanish.cn/Public/Home/Images/live/onplay_11.png\">", "[玫瑰]");
            }
            dataList.add(chatMessage);
        }

        polyvChatAdapter.addHistory(dataList);

        int pos = lv_chat.getCount() - 1;
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            lv_chat.smoothScrollToPosition(pos);
        } else {
            lv_chat.setSelection(pos);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            closeKeybordAndEmo(et_talk, getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isPPTLive && chatManager != null) {
            // 关闭聊天室
            chatManager.disconnect();
            chatManager.setOnChatManagerListener(null);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // 隐藏表情布局
        resetEmoLayout(true);
        // 清除焦点
        et_talk.clearFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_send:
                sendMsg();
                break;
            case R.id.iv_page1:
                vp_emo.setCurrentItem(0);
                break;
            case R.id.iv_page2:
                vp_emo.setCurrentItem(1);
                break;
            case R.id.iv_page3:
                vp_emo.setCurrentItem(2);
                break;
            case R.id.iv_page4:
                vp_emo.setCurrentItem(3);
                break;
            case R.id.iv_page5:
                vp_emo.setCurrentItem(4);
                break;
            case R.id.iv_emoswitch:
                resetEmoLayout(false);
                break;
            case R.id.et_talk:
                resetEmoLayout(true);
                break;
        }
    }
}
