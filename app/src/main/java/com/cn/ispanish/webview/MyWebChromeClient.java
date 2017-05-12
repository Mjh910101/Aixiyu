package com.cn.ispanish.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.cn.ispanish.interfaces.CallbackForString;


public class MyWebChromeClient extends WebChromeClient {

	private CallbackForString callback;

	public MyWebChromeClient(CallbackForString callback) {
		this.callback = callback;
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		if (callback != null) {
			callback.callback(title);
		}
	}
}
