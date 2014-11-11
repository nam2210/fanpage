package com.bombomstudio.fanpage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CommentActivity extends Activity{
	private WebView browser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		//set actionbar
		getActionBar().hide();
		
		//prepare webview for comments
		browser = (WebView)findViewById(R.id.comment_webview);
		browser.setWebViewClient(new MyBrowser());
		
		//load Webview
		Bundle requestData = getIntent().getExtras();
		String url = requestData.getString("COMMENT_URL", "https://www.facebook.com/hocsinhcabiet.vn");
		openWebView(url);
	}
	
   private class MyBrowser extends WebViewClient {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
         view.loadUrl(url);
         return true;
      }
   }
   
   @SuppressLint("SetJavaScriptEnabled")
   private void openWebView(String url){
      browser.getSettings().setLoadsImagesAutomatically(true);
      browser.getSettings().setJavaScriptEnabled(true);
      browser.getSettings().setBuiltInZoomControls(true);
      browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
      browser.loadUrl(url);
   }
	
}
