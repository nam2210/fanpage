package com.bombomstudio.fanpage;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.bombomstudio.fanpage.app.AppController;
import com.bombomstudio.fanpage.helper.NavDrawerListAdapter;
import com.bombomstudio.fanpage.util.FileManager;
import com.tien.facebookapi.FacebookAPIListener;
import com.tien.facebookapi.FacebookAPIPagePhoto;
import com.tien.facebookapi.FacebookAPIPhoto;
import com.tien.facebookapi.FacebookAPIService;
import com.tien.facebookapi.FacebookService;
import com.tien.facebookapi.FacebookServiceVar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("RtlHardcoded")
public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private DrawerLayout mDrawerLayout;
	private ImageView mDrawerIndicator;
	private ImageView mScrollUpIndicator;
	private TextView mTitle;
	private LinearLayout mDrawer;
	private FileManager mFileManager;
	
	
	private ListView mDrawerList;
	private NavDrawerListAdapter mDrawerAdapter;
	private ArrayList<String> mDrawerListItem;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		//TODO 07/11/2014
		initFacebookAPIService();
		mFileManager = new FileManager(this);
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.act_main_layout);
		mDrawerIndicator = (ImageView) findViewById(R.id.container_img_drawer);
		mDrawerIndicator.setOnClickListener(mDrawerClick);
		mScrollUpIndicator = (ImageView) findViewById(R.id.container_img_up);
		mScrollUpIndicator.setOnClickListener(mScrollUpClick);
		mTitle = (TextView) findViewById(R.id.container_txt_title);
		
		mDrawer = (LinearLayout) findViewById(R.id.drawer);
		//Getting category which is support
		mDrawerList = (ListView) findViewById(R.id.drawer_list);
		mDrawerListItem = new ArrayList<String>();
		String[] tamp = getResources().getStringArray(R.array.category);
		for(int i=0; i< tamp.length; i++){
			mDrawerListItem.add(tamp[i]);
		}
		mDrawerAdapter = new NavDrawerListAdapter(getApplication(), mDrawerListItem);
		mDrawerList.setOnItemClickListener(mDrawerListItemClick);
		mDrawerList.setAdapter(mDrawerAdapter);
		
		if(savedInstanceState == null){
			displayGallery(PHOTO_STREAM);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mFacebookAPIService.ResumeReceiver();
	}



	@Override
	protected void onPause() {
		super.onPause();
		mFacebookAPIService.PauseReceiver();
	}

	@Override
	protected void onDestroy() {
		stopService(new Intent(this, FacebookService.class));
		super.onDestroy();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/*
	 * EVENT WHEN CLICK DRAWER NAVIGATION
	 */
	OnClickListener mDrawerClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mDrawerLayout.openDrawer(mDrawer);
		}
	};
	
	OnClickListener mScrollUpClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(mContainerFragmentListener!=null){
				mContainerFragmentListener.onClickScrollUp();
			}
		}
	};
	
	/*
	 * EVENT WHEN CLICK DRAWER LIST
	 */
	int selectPosition = 0;
	OnItemClickListener mDrawerListItemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(selectPosition != position){
				//Log.e(TAG, "change fragment");
				selectPosition = position;
				displayGallery(position);
			}else{
				mDrawerLayout.closeDrawer(mDrawer);
			}
		}
	}; 
	
	/*
	 * DISPLAY FRAGMENT GALLERY
	 */
	private static final int PHOTO_STREAM = 0;
	private static final int PHOTO_FAVORITE = 1;
	private void displayGallery(int position){
		Fragment fragment = null;
		switch (position) {
		case PHOTO_STREAM:
			fragment = new ContainerFragment();
			break;
		case PHOTO_FAVORITE:
			fragment = new FavoriteFragment();
			break;

		default:
			break;
		}
		
		if(fragment!=null){
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction().replace(R.id.container_frame, fragment).commit();
			
			//update selected Item and Title then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			String title = mDrawerListItem.get(position);
			setTitle(title);
			mDrawerLayout.closeDrawer(mDrawer);
		}else{
			
		}
	}
	

	public void setTitle(String title){
		mTitle.setText(title);
	}
	
	/**
	 * set listener for fragment container
	 */
	ContainerFragmentListener mContainerFragmentListener = null;
	public void setOnContainFragmentListener(ContainerFragmentListener listener){
		mContainerFragmentListener = listener;
	}
	
	private void updateNewDataWhichHasReceived(){
		if(mContainerFragmentListener!=null){
			//mContainerFragmentListener.onLoadDateMore();
		}
	}
	
	/**
	 * SET HANDLER
	 */
	public static final int GET_NEW_PAGE = 0;
	public static final int GET_NEXT_PAGE = 1;
	public MyHandler mHandler = new MyHandler(this);
	public static class MyHandler extends Handler{
		private final WeakReference<MainActivity> mActivity;
		public MyHandler(MainActivity activity){
			mActivity = new WeakReference<MainActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {
			MainActivity activity = mActivity.get();
			if(activity!=null){
				switch (msg.what) {
				case MainActivity.GET_NEW_PAGE:
					break;
				case MainActivity.GET_NEXT_PAGE:
					activity.startLoadNextPage();
					break;
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * FACEBOOK API SERVICE 
	 **/
	FacebookAPIService mFacebookAPIService;
	private void initFacebookAPIService(){
	        mFacebookAPIService = new FacebookAPIService(this);
	        mFacebookAPIService.setOnlistener(mFacebookApiListener);
	}
	
	FacebookAPIListener mFacebookApiListener = new FacebookAPIListener() {
		@Override
		public void newPhoto(FacebookAPIPhoto photo) {
			addFacebookPhoto(photo);
		}
		
		@Override
		public void newPage(FacebookAPIPagePhoto page) {
			mFacebookAPIPagePhoto = page;
			endLoadNextPage();
		}
		
		@Override
		public void listFavouritePhoto(ArrayList<FacebookAPIPhoto> list) {
			int size = list.size();
			if(size > 0){
				//Log.e(TAG, "received list of favourite photo:" + size);
				onUpdateFavoriteList(list);
			}else{
				//Log.e(TAG, ">>>>>>>>received list of favourite photo:" + size);
				showMessage("No such favorite photo");
			}
		}
		
		@Override
		public void deleteFavouritePhoto(FacebookAPIPhoto photo, int status) {
			
		}
		
		@Override
		public void addFavouritePhoto(FacebookAPIPhoto photo, int status) {
			
		}

		@Override
		public void notifyCompeletedDownload() {
			if(mContainerFragmentListener!=null){
				mContainerFragmentListener.onSetIsLoadPhoto(false);
			}
		}
		
	};
	
	/**
	 * PHOTO STREAM FRAGMENT
	 */
	FacebookAPIPagePhoto mFacebookAPIPagePhoto = new FacebookAPIPagePhoto();
	ArrayList<FacebookAPIPhoto> mListPhoto = new ArrayList<FacebookAPIPhoto>();
	
	private void addFacebookPhoto(FacebookAPIPhoto item){
		if(item.mPageID == mFacebookAPIPagePhoto.mPageID){
			if(!isCheckExistingsPhoto(item.mPhotoID)){
	    		mListPhoto.add(item);
	    		if(mContainerFragmentListener != null){
	    			mContainerFragmentListener.onUpdatePhoto();
	    		}
			}
    	}
	}
	
	public FacebookAPIPagePhoto getFacebookApiPagePhoto(){
		return mFacebookAPIPagePhoto;
	}
	
	public ArrayList<FacebookAPIPhoto> getFacebookApiPhoto(){
		return mListPhoto;
	}
	
	private boolean isCheckExistingsPhoto(String photoId){
		boolean result = false;
		for(FacebookAPIPhoto a : mListPhoto){
			if(a.mPhotoID.equals(photoId)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	//TODO 11/08/2014
	public void getNewPage(){
		mListPhoto.clear();
		mFacebookAPIService.pressGetNew();
	}
	
	public void refreshPage(){
		mListPhoto.clear();
		mFacebookAPIService.pressGetNew();
	}
	
	/**
	 * FAVORITE FRAGMENT
	 */
	FavoriteFragmentListener mFavoriteFragmentListener = null;
	public void setOnFavoriteFragmentListener(FavoriteFragmentListener listener){
		mFavoriteFragmentListener = listener;
	}
	
	ArrayList<FacebookAPIPhoto> mFavoriteList = null;
	public ArrayList<FacebookAPIPhoto> getFavoritePhoto(){
		if(mFavoriteList == null){
			mFavoriteList = new ArrayList<FacebookAPIPhoto>();
		}
		mFavoriteList.clear();
		mFacebookAPIService.getListFavourite();
		return mFavoriteList;
	}
	
	private void onUpdateFavoriteList(ArrayList<FacebookAPIPhoto> list){
		if(mFavoriteList!=null){
			mFavoriteList.clear();
		}
		for(FacebookAPIPhoto item : list){
			mFavoriteList.add(item);
		}
		if(mFavoriteFragmentListener != null){
			mFavoriteFragmentListener.onUpdatePhoto();
		}
	}
	

	/**
	 * FUNCTION APP
	 */
	public void openCommentView(int position, boolean isFavorite){
		if(!isFavorite){
			if(mListPhoto.size() >0){
				FacebookAPIPhoto item = mListPhoto.get(position);
				Intent i = new Intent(this, CommentActivity.class);
				i.putExtra("COMMENT_URL", item.mPhotoLink);
				startActivity(i);
			}
		}else{
			if(mFavoriteList.size() >0){
				FacebookAPIPhoto item = mFavoriteList.get(position);
				Intent i = new Intent(this, CommentActivity.class);
				i.putExtra("COMMENT_URL", item.mPhotoLink);
				startActivity(i);
			}
		}
	}
	
	/**
	 *	ACTION OF DIALOG 
	 */
	public void openShareView(int position, boolean isFavorite){
		if(!isFavorite){
			if(mListPhoto.size() >0){
				FacebookAPIPhoto item = mListPhoto.get(position);
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, item.mPhotoLink);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Share"));
			}
		}else{
			if(mFavoriteList.size() >0){
				FacebookAPIPhoto item = mFavoriteList.get(position);
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, item.mPhotoLink);
				sendIntent.setType("text/plain");
				startActivity(Intent.createChooser(sendIntent, "Share"));
			}
		}
	}
	
	public void downloadPicture(int position, boolean isFavorite){
		if(!isFavorite){
			if(mListPhoto.size() >0){
				FacebookAPIPhoto item = mListPhoto.get(position);
				fetchImage(item.mPhotoSource);
			}
		}else{
			if(mFavoriteList.size() >0){
				FacebookAPIPhoto item = mFavoriteList.get(position);
				fetchImage(item.mPhotoSource);
			}
		}
	}
	
	 private void fetchImage(String link){
	        final String url = link;
	 
	        // show loader before making request
	        // add code to show progress
	        showProgress();

	        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(url,
            		new ImageListener() {
                        @Override
                        public void onErrorResponse(
                                VolleyError arg0) {
                        	//Log.e(TAG, arg0.toString());
                            Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.photo_download_error),
                                    Toast.LENGTH_LONG).show();
                            hideProgress();
                        }

                        @Override
                        public void onResponse(
                                ImageContainer response,
                                boolean arg1) {
                            if (response.getBitmap() != null) {
                                // load bitmap into imageview
                               mFileManager.saveImageToSDcard(response.getBitmap(), "/HocSinhCaBiet");

                                // hide loader and show set &
                               hideProgress();
                            }
                        }
            		});
	 }
	 
	 /**
	  * FAVORITE FUNCTION
	  */
	 public void addFavorite(int position, boolean isFavorite){
		if(!isFavorite){
			if(mListPhoto.size() >0){
				mListPhoto.get(position).setFavorite(1);
				FacebookAPIPhoto item = mListPhoto.get(position);
				mFacebookAPIService.addFavourite(item);
	    		if(mContainerFragmentListener != null){
	    			mContainerFragmentListener.onUpdatePhoto();
	    		}
			}
		}else{
			if(mFavoriteList.size() >0){
				mFavoriteList.get(position).setFavorite(1);
				FacebookAPIPhoto item = mFavoriteList.get(position);
				mFacebookAPIService.addFavourite(item);
	    		if(mFavoriteFragmentListener != null){
	    			mFavoriteFragmentListener.onUpdatePhoto();
	    		}
			}
		}
	 }
	 
	 public void removeFavorite(int position, boolean isFavorite){
		if(!isFavorite){ 
			if(mListPhoto.size() >0){
				mListPhoto.get(position).setFavorite(0);
				FacebookAPIPhoto item = mListPhoto.get(position);
				mFacebookAPIService.deleteFavourite(item);
	    		if(mContainerFragmentListener != null){
	    			mContainerFragmentListener.onUpdatePhoto();
	    		}
			}
		}else{
			if(mFavoriteList.size() >0){
				mFavoriteList.get(position).setFavorite(0);
				FacebookAPIPhoto item = mFavoriteList.get(position);
				mFacebookAPIService.deleteFavourite(item);
	    		if(mFavoriteFragmentListener != null){
	    			mFavoriteFragmentListener.onUpdatePhoto();
	    		}
			}
		}
	 }
	 
	 /**
	  * LOAD MORE DATA
	  */
	 boolean isPageLoading = false;
	 private void startLoadNextPage(){
		 if(!isPageLoading){
			 //Log.e(TAG, ">>>>>>>> Load next Page");
			 isPageLoading = true;
			 mFacebookAPIService.pressGetNext();
			 if(mContainerFragmentListener!=null){
				 mContainerFragmentListener.onStartLoadDataMore();
			 }
		 }
	 }
	 
	 private void endLoadNextPage(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				////Log.e(TAG, "isPage Load is completed");
				isPageLoading = false;
			}
		}, 2000);
		 if(mContainerFragmentListener!=null){
			 //Log.e(TAG, "endLoadNextPage");
			 mContainerFragmentListener.onSetIsLoadPhoto(true);
			 mContainerFragmentListener.onEndLoadDataMore();
		 }
	 }
	 
	 
	 /**
	  * PROGRESS DIALOG
	  */
	 ProgressDialog mProgressDialog = null;
	 private void showProgress(){
		 mProgressDialog = new ProgressDialog(this);
		 mProgressDialog.setMessage("Downloading...");
		 mProgressDialog.setCancelable(false);
		 mProgressDialog.show();
	 }
	 
	 private void hideProgress(){
		 if(mProgressDialog != null && mProgressDialog.isIndeterminate()){
			 mProgressDialog.dismiss();
			 mProgressDialog = null;
		 }
	 }
	 
	 
	/**
	 * UTLIS FUNCTION
	 */
	private void showMessage(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	        
	
}
