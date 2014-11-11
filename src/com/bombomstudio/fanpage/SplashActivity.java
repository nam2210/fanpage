package com.bombomstudio.fanpage;

import java.lang.ref.WeakReference;

import com.bombomstudio.fanpage.util.ConnectionManager;
import com.tien.facebookapi.FacebookAPIPreferences;
import com.tien.facebookapi.FacebookService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends Activity{

	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		getActionBar().hide();
		mContext = this;
		
		//check internet connection
		ConnectionManager connManager = new ConnectionManager(this);
		boolean isConnected = connManager.isInternetConn();
		if(isConnected){
			//start facebook service....
	        FacebookAPIPreferences.savePageID(this, "1482784261944904");
	        startService(new Intent(this, FacebookService.class));
	        mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent i = new Intent(mContext,MainActivity.class);
					startActivity(i);
					finish();
				}
			}, 3000);
		}else{
			//show alert dialog
			showAlertDialog(this, "No Internet Connection", " your device is not connected to internet");
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mContext = this;
	}


	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	}



	public MyHandler mHandler = new MyHandler(this); 
	public static class MyHandler extends Handler{
		private final WeakReference<SplashActivity> mActivity;
		public MyHandler(SplashActivity activity){
			mActivity = new WeakReference<SplashActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			SplashActivity parentActivity = mActivity.get();
			if(parentActivity!=null){
				switch (msg.what) {
				default:
					break;
				}
			}
		}
		
		
	}
	
	/**
	 * SHOW DIALOG
	 */
	public void showAlertDialog(Context context, String title, String message){
		AlertDialog mAlertDialog = new AlertDialog.Builder(context).create();
		mAlertDialog.setTitle(title);
		mAlertDialog.setMessage(message);
		mAlertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		mAlertDialog.show();
	}
}
