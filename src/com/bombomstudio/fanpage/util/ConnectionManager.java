package com.bombomstudio.fanpage.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionManager {
	private Context mContext;
	
	public ConnectionManager(Context context){
		this.mContext = context;
	}
	
    public boolean isMobileInternetConn() {
        //Create ConnectivityManager object for network connection info
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivity != null) {
            //get internet connection info
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                //check device which is connected to internet 
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isWifiInternetConn(){
        //Create ConnectivityManager object for network connection info
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivity != null) {
            //get internet connection info
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                //check device which is connected to internet 
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isInternetConn(){
        //Create ConnectivityManager object for network connection info
        ConnectivityManager connectivity = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        
        if (connectivity != null) {
            //get internet connection info
            NetworkInfo mobileInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (wifiInfo != null) {
                //check device which is connected to internet 
                if (wifiInfo.isConnected()) {
                    return true;
                }
            }
            
            if (mobileInfo != null) {
                //check device which is connected to internet 
                if (mobileInfo.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

}
