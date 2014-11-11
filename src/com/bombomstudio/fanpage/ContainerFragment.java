package com.bombomstudio.fanpage;

import java.util.ArrayList;

import com.bombomstudio.fanpage.helper.ListViewScrollListener;
import com.bombomstudio.fanpage.helper.PictureItemListener;
import com.bombomstudio.fanpage.helper.PictureListAdapter;
import com.tien.facebookapi.FacebookAPIPhoto;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ContainerFragment extends Fragment implements ContainerFragmentListener{
	private static final String TAG = ContainerFragment.class.getSimpleName();
	private MainActivity mActivity = null;
	
	private ListView mPhotoListView;
	private ArrayList<FacebookAPIPhoto> mPhotoItems;
	private PictureListAdapter mPhotoListAdapter;
	private ProgressBar mLoadPictureProgress;
	ListViewScrollListener mListViewScrollListener;
	//TODO 11/10/2014
	private LinearLayout mLoadingLayout;
	
	
	//Contrucstor
	public ContainerFragment(){}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Log.e(TAG, "OnCreateView");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_gallery, container, false);
		mPhotoListView = (ListView) rootView.findViewById(R.id.gallery_listView);
		mLoadingLayout = (LinearLayout) rootView.findViewById(R.id.gallery_layout_loading);
		
		//Log.e(TAG, "OnCreateView ContainerFragment");
		mActivity.getNewPage();
		showLoadingLayout();
		mPhotoItems = new ArrayList<FacebookAPIPhoto>();
		mPhotoItems = mActivity.getFacebookApiPhoto();
		
		//set photo adapter
		mPhotoListAdapter = new PictureListAdapter(mActivity, mPhotoItems);
		mPhotoListAdapter.setOnPictureItemListener(mPictureItemListener);
		mPhotoListView.setAdapter(mPhotoListAdapter);
		//Progress bar
		mLoadPictureProgress = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		//swipe refresh layout
		initSwipeRefreshUI(rootView);
		//configure listener
		mListViewScrollListener = new ListViewScrollListener(mActivity);
		mPhotoListView.setOnScrollListener(mListViewScrollListener);
		mListViewScrollListener.setSwipeRefreshLayout(swipeRefreshLayout, mPhotoListView);
		
		return rootView;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mActivity.setOnContainFragmentListener(this);
	}


	@Override
	public void onPause() {
		super.onPause();
		mActivity.setOnContainFragmentListener(null);
	}
	
	
	PictureItemListener mPictureItemListener = new PictureItemListener() {
		
		@Override
		public void onClickSettingButton(int position) {
			showActionDialog(position);
		}
		
		@Override
		public void onClickAddFavoriteButton(int position) {
			if(mActivity!=null){
				mActivity.addFavorite(position, false);
			}
		}
		
		@Override
		public void onClickRemoveFavoriteButton(int position) {
			if(mActivity!=null){
				mActivity.removeFavorite(position, false);
			}
		}
		
		@Override
		public void onClickCommentButton(int position) {
			if(mActivity!=null){
				mActivity.openCommentView(position,false);
			}
		}
	};
	
	/**
	 * DIALOG FOR ACTIONS
	 */
	private void showActionDialog(final int position){
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_share);
		dialog.setCancelable(false);
		TextView txtTitle = (TextView) dialog.findViewById(R.id.dialog_txt_title);
		Button btnShare = (Button)dialog.findViewById(R.id.dialog_btn_share);
		Button btnDownload = (Button)dialog.findViewById(R.id.dialog_btn_download);
		Button btnCancel = (Button)dialog.findViewById(R.id.dialog_btn_cancel);
		Typeface tf = Typeface.createFromAsset(mActivity.getAssets(), "fonts/VN FOCO.OTF");
		txtTitle.setTypeface(tf);
		btnShare.setTypeface(tf);
		btnDownload.setTypeface(tf);
		btnCancel.setTypeface(tf);
		
		
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(mActivity!=null){
					mActivity.openShareView(position,false);
				}
			}
		});
		
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(mActivity!=null){
					mActivity.downloadPicture(position,false);
				}
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	/**
	 * RECEIVE COMMAND FROM MAINACTIVITY
	 **/
	@Override
	public void onClickScrollUp() {
		mPhotoListView.setSelection(0);
	}


	@Override
	public void onStartLoadDataMore() {
//		if(mLoadPictureProgress != null && !mLoadPictureProgress.isShown()){
//			mLoadPictureProgress.setVisibility(View.VISIBLE);
//		}
		showLoadingLayout();
	}


	@Override
	public void onEndLoadDataMore() {
//		if(mLoadPictureProgress != null && mLoadPictureProgress.isShown()){
//			mLoadPictureProgress.setVisibility(View.GONE);
//		}
		hideLoadingLayout();
		//disable if previous state is refreshing
		disableRefresh();
	}
	
	@Override
	public void onUpdatePhoto() {
		mPhotoListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onSetIsLoadPhoto(boolean status) {
		mListViewScrollListener.setIsLoading(status);
		//when load data complete downloading process, swipe function to refresh data is enable
		if(status){
			swipeRefreshLayout.setEnabled(false);
			swipeRefreshLayout.setOnRefreshListener(null);
		}else{
			swipeRefreshLayout.setEnabled(true);
		    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
		         
		        @Override
		        public void onRefresh() {
		            // TODO : request data here
		            // our swipeRefreshLayout needs to be notified when the data is returned in order for it to stop the animation
		        	//Log.e(TAG, "refresh data");
		        	mActivity.refreshPage();
		        	
		        }
		    });
		}
	}
	
	
	/**
	 * Swipe Refresh UI
	 */
	private SwipeRefreshLayout swipeRefreshLayout;
	
	private void initSwipeRefreshUI(View view){
		 // find the layout
	    swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
	    // the refresh listner. this would be called when the layout is pulled down
	    swipeRefreshLayout.setOnRefreshListener(null);
	    // sets the colors used in the refresh animation
	    swipeRefreshLayout.setColorSchemeResources(R.color.blue_bright, R.color.green_light,
	            R.color.orange_light, R.color.red_light);
	}

	private void disableRefresh(){
		if(swipeRefreshLayout.isRefreshing()){
			swipeRefreshLayout.setRefreshing(false);
		}
	}


	/**
	 * LOADING UI
	 */
	private void showLoadingLayout(){
		if(mLoadingLayout!=null && !mLoadingLayout.isShown()){
			mLoadingLayout.setVisibility(View.VISIBLE);
		}
	}
	
	private void hideLoadingLayout(){
		if(mLoadingLayout!=null && mLoadingLayout.isShown()){
			mLoadingLayout.setVisibility(View.GONE);
		}
	}

	
}
