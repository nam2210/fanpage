package com.bombomstudio.fanpage;

import java.util.ArrayList;

import com.bombomstudio.fanpage.helper.PictureItemListener;
import com.bombomstudio.fanpage.helper.PictureListAdapter;
import com.tien.facebookapi.FacebookAPIPhoto;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FavoriteFragment extends Fragment implements FavoriteFragmentListener{
	private static final String TAG = FavoriteFragment.class.getSimpleName();
	private MainActivity mActivity;
	
	private ListView mFavoriteListView;
	private ArrayList<FacebookAPIPhoto> mFavoritePhoto;
	private PictureListAdapter mFavoritePhotoListAdapter;
	private ProgressBar mLoadPictureProgress;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.activity_favorite, container, false);
		
		//getListView
		mFavoriteListView = (ListView) rootView.findViewById(R.id.favorite_listView);
		mFavoritePhoto = new ArrayList<FacebookAPIPhoto>();
		//Log.e(TAG, "OnCreate FavoriteFragment");
		//get data for favorite
		mFavoritePhoto = mActivity.getFavoritePhoto();
		//set adapter
		mFavoritePhotoListAdapter = new PictureListAdapter(mActivity, mFavoritePhoto);
		mFavoritePhotoListAdapter.setOnPictureItemListener(mFavoriteAdapterListener);
		mFavoriteListView.setAdapter(mFavoritePhotoListAdapter);

		return rootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		mActivity.setOnFavoriteFragmentListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		mActivity.setOnFavoriteFragmentListener(null);
	}

	/**
	 * FAVORITE FRAGMENT LISTENER
	 */
	@Override
	public void onUpdatePhoto() {
		mFavoritePhotoListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * FAVORITE ADAPTER LISTENER
	 */
	PictureItemListener mFavoriteAdapterListener = new PictureItemListener() {
		
		@Override
		public void onClickSettingButton(int position) {
			showActionDialog(position);
		}
		
		@Override
		public void onClickRemoveFavoriteButton(int position) {
			if(mActivity != null){
				mActivity.removeFavorite(position, true);
			}
		}
		
		@Override
		public void onClickAddFavoriteButton(int position) {
			if(mActivity != null){
				mActivity.addFavorite(position, true);
			}
		}
		
		@Override
		public void onClickCommentButton(int position) {
			if(mActivity!=null){
				mActivity.openCommentView(position,true);
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
					mActivity.openShareView(position,true);
				}
			}
		});
		
		btnDownload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				if(mActivity!=null){
					mActivity.downloadPicture(position,true);
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
	
}
