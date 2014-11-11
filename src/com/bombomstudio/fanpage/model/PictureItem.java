package com.bombomstudio.fanpage.model;

import android.graphics.Bitmap;

public class PictureItem {
	private String mTitle; // message of picture 
	private Bitmap mPictureBm; // picture link
	private int mNoLikes; // numbers of like
	private int mNoComment; // numbers of comment
	private String mUpdatedDate; // date of updated
	private String mFaceLink; // Face of Link
	
	public PictureItem(){}
	
	public PictureItem(String title, Bitmap pictureBm, int noLikes, int noComment,String updatedDate, String  faceLink){
		this.mTitle = title;
		this.mPictureBm = pictureBm;
		this.mNoLikes = noLikes;
		this.mNoComment = noComment;
		this.mUpdatedDate = updatedDate;
		this.mFaceLink = faceLink;
	}
	
	public String getTitle(){
		return this.mTitle;
	}
	
	public void setTitle(String title){
		this.mTitle = title;
	}
	
	public Bitmap getPictureBm(){
		return this.mPictureBm;
	}
	
	public void setPictureBm(Bitmap pictureBm){
		this.mPictureBm = pictureBm;
	}
	
	public int getNoLikes(){
		return this.mNoLikes;
	}
	
	public void setNoLikes(int noLikes){
		this.mNoLikes = noLikes;
	}
	
	public int getNoComment(){
		return this.mNoComment;
	}
	
	public void setNoComment(int noComment){
		this.mNoComment = noComment;
	}
	
	public String getUpdatedDate(){
		return this.mUpdatedDate;
	}
	
	public void setUpdatedDate(String updatedDate){
		this.mUpdatedDate = updatedDate;
	}
	
	public String getFaceLink(){
		return this.mFaceLink;
	}
	
	public void setFaceLink(String faceLink){
		this.mFaceLink = faceLink;
	}
}
