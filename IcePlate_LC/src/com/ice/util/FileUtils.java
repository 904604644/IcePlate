package com.ice.util;

import java.io.ByteArrayOutputStream;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

public class FileUtils {

	// calculateInSampleSizeֵ

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

		final int height = options.outHeight;

		final int width = options.outWidth;

		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height / (float) reqHeight);

			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

		}

		return inSampleSize;

	}

	// getSmallBitmap

	public static Bitmap getSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize

		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);

	}

	// bitmapToString

	public static String bitmapToString(String filePath,int type) {
		Bitmap bm = null;
		if(type == 1) {
			bm = getSmallBitmap(filePath);
		}else {
			bm = BitmapFactory.decodeFile(filePath);
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int size = getBitmapSize(bm)/1024;
		if(type == 1) {
			if (size<1000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			}else if (size>1000 && size<=2000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
			}else if (size>2000 && size<=4000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 6, baos);
			}else if (size>4000 && size<=6000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 4, baos);
			}else {
				bm.compress(Bitmap.CompressFormat.JPEG, 3, baos);
			}
		}else {
			if (size > 1000 && size<=2000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			}else if (size>2000 && size<=4000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
			}else if (size<4000 && size<=6000) {
				bm.compress(Bitmap.CompressFormat.JPEG, 25, baos);
			}else {
				bm.compress(Bitmap.CompressFormat.JPEG, 5, baos);
			}
		}
		byte[] b = baos.toByteArray();
		Log.i("ice", "图片大小："+size);
		bm.recycle();
		return Base64.encodeToString(b, Base64.DEFAULT);

	}
	
	/** 
	  * 得到bitmap的大小 
	  */  
	 @SuppressLint("NewApi")
	public static int getBitmapSize(Bitmap bitmap) {  
	     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//API 19  
	         return bitmap.getAllocationByteCount();  
	     }  
	     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12  
	         return bitmap.getByteCount();  
	     }  
	     // 在低版本中用一行的字节x高度  
	     return bitmap.getRowBytes() * bitmap.getHeight();//earlier version  
	 }

}
