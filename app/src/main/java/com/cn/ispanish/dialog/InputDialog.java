package com.cn.ispanish.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cn.ispanish.R;
import com.cn.ispanish.handlers.SystemHandle;
import com.cn.ispanish.handlers.WinHandler;
import com.cn.ispanish.interfaces.CallbackForString;

import java.util.Timer;
import java.util.TimerTask;

public class InputDialog {

    private boolean isShow;

    private Context context;
    private AlertDialog ad;
    private Window window;

    private EditText mEditText;
    private TextView title, commit;

    private InputMethodManager imm = null;

    public InputDialog(Context context) {
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();

        ad.setView(((LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.layout_input_dialog, null));
        ad.show();

        window = ad.getWindow();
        window.setContentView(R.layout.layout_input_dialog);

        title = (TextView) window.findViewById(R.id.inputDialog_title);
        mEditText = (EditText) window.findViewById(R.id.inputDialog_ET);
        commit = (TextView) window.findViewById(R.id.inputDialog_commit);

        imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        isShow = SystemHandle.getIsGoneShow(context);

        setLayout();

        showImm();
    }

    public void setIntutType(int type) {
        mEditText.setInputType(type);
    }

    private void showImm() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                imm.showSoftInput(mEditText, 0);
            }
        }, 1000);
    }

    private void setLayout() {
        setLayout(0.8, 0.5);
    }

    public void setLayout(double Xnum, double Ynum) {
        window.setLayout((int) (WinHandler.getWinWidth(context) * Xnum),
                (int) (WinHandler.getWinHeight(context) * Ynum));
    }

    public void setTitle(String t) {
        if (t != null && !t.equals("")) {
            title.setVisibility(View.VISIBLE);
            title.setText(t);
        }
    }

    public void goneInput() {
        mEditText.setVisibility(View.GONE);
    }

    public void setInput(String str) {
        mEditText.setText(str);
    }

    public void setHint(String h) {
        if (h != null && !h.equals("")) {
            mEditText.setHint(h);
        }
    }

    public void setListener(final CallbackForString callback) {
        commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String str = mEditText.getText().toString();
                if (callback != null) {
                    callback.callback(str);
                }
                dismiss();
            }
        });
    }

    public void dismiss() {
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mEditText.getApplicationWindowToken(),
                    0);
        }
        SystemHandle.saveIsGoneShow(context, isShow);
        ad.dismiss();
    }

}
