package com.bombomstudio.fanpage.helper;

import com.bombomstudio.fanpage.MainActivity;
import com.bombomstudio.fanpage.MainActivity.MyHandler;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.AbsListView.OnScrollListener;

public class ListViewScrollListener implements OnScrollListener{
	private static final String TAG  = ListViewScrollListener.class.getSimpleName();
    private int visibleThreshold = 1;
    private int currentPage = 0;
    private int previousTotal = 0;
    //private boolean loading = false;
    private MainActivity mActivity;
    private MyHandler mMainActivityHandler;
//    private ProgressBar mProgressBar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView mListView;
    private boolean isLoading = false;

    public ListViewScrollListener(MainActivity activity) {
    	this.mActivity = activity;
    }
    public ListViewScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }
    
    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout, ListView listView){
    	mSwipeRefreshLayout = swipeRefreshLayout;
    	mListView = listView;
    }
    
    public void setIsLoading(boolean isLoading){
    	this.isLoading = isLoading;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
//        if (loading) {
//            if (totalItemCount > previousTotal) {
//                loading = false;
//                previousTotal = totalItemCount;
//                currentPage++;
//            }
//        }
        
        if (!isLoading && (firstVisibleItem + 1 ) == (totalItemCount -1)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
        	////Log.e(TAG, "load next page");
        	mActivity.mHandler.sendEmptyMessage(MainActivity.GET_NEXT_PAGE);
        }
        
        //check position of listview is top or not (position == 0 )
        boolean enable = false;
        if(mListView != null && mListView.getChildCount() > 0){
            // check if the first item of the list is visible
            boolean firstItemVisible = mListView.getFirstVisiblePosition() == 0;
            // check if the top of the first item is visible
            boolean topOfFirstItemVisible = mListView.getChildAt(0).getTop() == 8;
            // enabling or disabling the refresh layout
            enable = firstItemVisible && topOfFirstItemVisible;
            //mActivity.mHandler.sendEmptyMessage(MainActivity.GET_NEW_PAGE);
        }
        if(!isLoading && mSwipeRefreshLayout!=null){
        	mSwipeRefreshLayout.setEnabled(enable);
        }
        
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }
    
    private Handler mHandler = new Handler();
}
