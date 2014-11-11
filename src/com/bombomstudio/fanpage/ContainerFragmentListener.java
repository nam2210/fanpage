package com.bombomstudio.fanpage;

public interface ContainerFragmentListener {
	public void onClickScrollUp();
	public void onStartLoadDataMore();
	public void onEndLoadDataMore();
	public void onUpdatePhoto();
	public void onSetIsLoadPhoto(boolean status);
}
