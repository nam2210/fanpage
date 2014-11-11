package com.bombomstudio.fanpage.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import com.bombomstudio.fanpage.R;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileManager {
	private String TAG = FileManager.class.getSimpleName();
	private Context mContext;

	public FileManager(Context context){
		this.mContext = context;
	}
	/**
	 * 
	 * @param bitmap bitmap of resource which you want to save
	 * @param path where file is save
	 */
	public void saveImageToSDcard(Bitmap bitmap, String path){
		File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), path);
		if(!myDir.exists()){
			myDir.mkdirs();
		}
		Random generator = new Random();
		int n = 10000;
		n = generator.nextInt(n);
		String fname = "HocSinhCaBiet-" + n + ".jpg";
		File file = new File(myDir,fname);
		if(file.exists()){
			file.delete();
		}
		
		try{
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Toast.makeText(
                    mContext,
                    mContext.getString(R.string.toast_saved).replace("#",
                            "\"" + path + "\""),
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Wallpaper saved to: " + file.getAbsolutePath());
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(mContext, mContext.getString(R.string.toast_saved_failed), Toast.LENGTH_SHORT).show();
		}
	}
	

	
	
	
}
