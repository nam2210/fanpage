package com.bombomstudio.fanpage.helper;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bombomstudio.fanpage.R;
import com.bombomstudio.fanpage.app.AppController;
import com.bombomstudio.fanpage.model.PictureItem;
import com.tien.facebookapi.FacebookAPIPhoto;


public class PictureListAdapter extends BaseAdapter{
	private Activity mActivity;
	private ArrayList<FacebookAPIPhoto> mPictureItems;
	ImageLoader mImageLoader = AppController.getInstance().getImageLoader();
	
	public PictureListAdapter(Activity activity, ArrayList<FacebookAPIPhoto> pictureItem){
		this.mActivity = activity;
		this.mPictureItems = pictureItem;
	}
	@Override
	public int getCount() {
		return mPictureItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mPictureItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	static class ViewHolder{
		public TextView txtTitle;
		public TextView txtDate;
		public TextView txtIndex;
		public NetworkImageView imgPicture;
		public TextView txtLikeComment;
		public Button btnFavorite;
		public Button btnComment;
		public Button btnSetting;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null){
			LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = vi.inflate(R.layout.photo_item, parent,false);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.txtTitle = (TextView) rowView.findViewById(R.id.photo_txt_title);
			viewHolder.txtIndex = (TextView) rowView.findViewById(R.id.photo_txt_index);
			viewHolder.txtDate = (TextView) rowView.findViewById(R.id.photo_txt_updated_time);
			viewHolder.txtLikeComment = (TextView) rowView.findViewById(R.id.photo_txt_like_cmt);
			viewHolder.imgPicture = (NetworkImageView) rowView.findViewById(R.id.photo_imgview_picture);
			viewHolder.btnFavorite = (Button) rowView.findViewById(R.id.photo_btn_favor);
			viewHolder.btnComment = (Button) rowView.findViewById(R.id.photo_btn_comment);
			viewHolder.btnSetting = (Button) rowView.findViewById(R.id.photo_btn_settings);
			rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		final FacebookAPIPhoto photo = mPictureItems.get(position);
		//set content for row
		String title = photo.mPhotoName;
		holder.txtTitle.setText(title);
		holder.txtIndex.setText(String.valueOf(position + 1));
		holder.txtDate.setText(photo.mCreateTime);
		//String likeComment = "Likes " + String.valueOf(photo.mNumberLike) + "  Comments " + String.valueOf(photo.mNumberComments);
		//holder.txtLikeComment.setText(likeComment);
		//set url for network image
		//holder.imgPicure.setImageBitmap(item.getPictureBm());
		if(mImageLoader==null){
			mImageLoader = AppController.getInstance().getImageLoader();
		}
		holder.imgPicture.setImageUrl(photo.mPhotoSource, mImageLoader);
		
		String likeComment = null;
		holder.btnFavorite.setId(position);
		if(photo.mIsFavourtie == 1){
			holder.btnFavorite.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_favorite_select));
			likeComment = "Likes " + String.valueOf(photo.mNumberLike + 1) + "  Comments " + String.valueOf(photo.mNumberComments);
		}else if(photo.mIsFavourtie == 0){
			holder.btnFavorite.setBackground(mActivity.getResources().getDrawable(R.drawable.ic_favorite));
			likeComment = "Likes " + String.valueOf(photo.mNumberLike) + "  Comments " + String.valueOf(photo.mNumberComments);
		}
		//set like text
		holder.txtLikeComment.setText(likeComment);
		holder.btnComment.setId(position);
		holder.btnSetting.setId(position);
		
		//set listener for favorite button
		holder.btnFavorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					int p =  (Integer) v.getId();
					if(photo.mIsFavourtie == 1){
						mListener.onClickRemoveFavoriteButton(p);
					}else if(photo.mIsFavourtie == 0){
						mListener.onClickAddFavoriteButton(p);
					}
				}
			}
		});
		
		//set comment for favorite button
		holder.btnComment.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener!=null){
						int p =  (Integer) v.getId();
						mListener.onClickCommentButton(p);
					}
				}
		});
		//set listener for favorite button
		holder.btnSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mListener!=null){
					int p =  (Integer) v.getId();
					mListener.onClickSettingButton(p);
				}
			}
		});
		
		return rowView;
	}
	
	PictureItemListener mListener;
	public void setOnPictureItemListener(PictureItemListener listener){
		mListener = listener;
	}

}
