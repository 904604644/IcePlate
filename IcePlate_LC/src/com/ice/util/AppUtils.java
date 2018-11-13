package com.ice.util;

import java.util.ArrayList;

import com.ice.iceplate.R;
import com.ice.widget.ImageSelectorActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

public class AppUtils {
	public static String OSS_SAVE_URL = "http://demoparking.oss-cn-shenzhen.aliyuncs.com/";
	/**
	 * 图片下载展示
	 * 
	 * @param imagepath
	 *            图片地址 包括 url和本地的路径,如果是本地SD卡图片在图片路径前
	 * @param imageview
	 *            显示的 imageview
	 * @param type
	 *            图片来源 1为网络，传入的是URL，2为本地图片，传入的为本地路径。
	 */
	public static void ImageLoader(String imagepath, ImageView imageview,
			int type) {
		if(imagepath == null){
			return;
		}
		if (type == 2) {
			if(!imagepath.startsWith("file://")){				
				imagepath = "file://" + imagepath;
			}
		} else {
			if(!imagepath.startsWith("http")){				
				imagepath = OSS_SAVE_URL + imagepath;
			}
		}
		DisplayImageOptions options = DisplayImageOption.getHttpBuilder(
				R.drawable.parking_photo_normal).build();
		ImageLoader.getInstance().displayImage(imagepath, imageview, options);
	}
	
	/**
	 * 
	 * 启动本地图片选择器
	 * 
	 * @param activity
	 *            当前的activity或者fragment 同时也是接收图片的页面
	 * @param isShowCamera
	 *            是否显示拍照按钮
	 * @param MaxImageNum
	 *            选择的最大照片数量
	 * @param selectedMode
	 *            选择模型 0为单选，1为多选
	 * @param mSelectPath
	 *            已经选择的图片list。
	 * @param result
	 *            返回的result值
	 */
	public static void starImageSelector(Activity activity,
			boolean isShowCamera, int MaxImageNum, int selectedMode,
			ArrayList<String> mSelectPath, int result) {
		Intent intent = new Intent(activity, ImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(ImageSelectorActivity.EXTRA_SHOW_CAMERA, isShowCamera);
		// 最大可选择图片数量
		intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_COUNT, MaxImageNum);
		// 选择模式
		intent.putExtra(ImageSelectorActivity.EXTRA_SELECT_MODE, selectedMode);
		// 默认选择
		if (mSelectPath != null && mSelectPath.size() > 0) {
			intent.putExtra(ImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
					mSelectPath);
		}
		activity.startActivityForResult(intent, result);
	}
}
