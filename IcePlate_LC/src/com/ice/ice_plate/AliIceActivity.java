package com.ice.ice_plate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONObject;

import com.ice.iceplate.R;
import com.ice.preview.my.ImagePreviewAty;
import com.ice.util.AppUtils;
import com.ice.util.CarIceUtils;
import com.ice.widget.ImageSelectorActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import dr.android.fileselector.FileSelectConstant;

/**
 * 
 * 
 * @since 2018-07-02
 */
public class AliIceActivity extends Activity implements
		OnClickListener {
	private static final Integer FILE_SELECTOR_REQUEST_CODE = 2016;
	private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	private String photokey = "";//地址
	private Button btn_by_takephoto;
	private Button btn_by_takefilephoto;
	private ImageView img_by_takephoto;
	private TextView tx_by_takephoto;
	private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
//    private String PHOTO_PATH = Environment.getExternalStorageDirectory() +  "/image/123.jpg";
    /** 图片路径*/
	private ArrayList<String> mSelectPathForPark1 = new ArrayList<String>();
	private ArrayList<String> path = new ArrayList<String>();
	/** 图片选择返回code*/
	private static final int IMAGEFORPARKCODE1 = 1011;
	private static StringBuffer sbfile = new StringBuffer();
	private List<String> imagePathList = new ArrayList<String>();
	/** 总共*/
	private int count = 0;
	/** 执行图片识别次数*/
	private int calCount = 0;
	/** 执行错误图片识别次数*/
	private int errCount = 0;
	private static ExecutorService threadPool =  Executors.newFixedThreadPool(5);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ali_ice);
		initView();
		setListener();
	}

	/**
	 * 初始化view
	 * */
	private void initView() {
		btn_by_takephoto = (Button) findViewById(R.id.btn_by_takephoto);
		btn_by_takefilephoto = (Button) findViewById(R.id.btn_by_takefilephoto);
		img_by_takephoto = (ImageView) findViewById(R.id.img_by_takephoto);
		tx_by_takephoto = (TextView) findViewById(R.id.tx_by_takephoto);
		imageLoader.init(ImageLoaderConfiguration.createDefault(this));
	        options = new DisplayImageOptions.Builder()
	        .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片URL为空或是错误的时候显示图片
	        .resetViewBeforeLoading(true)//设置下载的图片下载前是否重置，复位
	        .cacheInMemory(false)//设置下载图片是否缓存到内存      
	        .imageScaleType(ImageScaleType.EXACTLY)
	        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片解码类型
	        .displayer(new FadeInBitmapDisplayer(300))//设置用户加载图片的task(这里是渐现)
	        .build();
	}

	/**
	 * 监听
	 */
	private void setListener() {
		btn_by_takephoto.setOnClickListener(this);
		btn_by_takefilephoto.setOnClickListener(this);
	}

	/**
	 * 点击监听事件
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_by_takephoto:
//			camera();
			AppUtils.starImageSelector(AliIceActivity.this,true, 1, 0, mSelectPathForPark1, IMAGEFORPARKCODE1);
			break;
		case R.id.btn_by_takefilephoto:
			startActivity("1");
			break;
		default:
			break;
		}
	}
    
    private void startActivity(String type) {
    	Intent intent = new Intent();
		intent.setClass(getApplicationContext(), FsActivity.class);
		intent.putExtra(FileSelectConstant.SELECTOR_REQUEST_CODE_KEY, FileSelectConstant.SELECTOR_MODE_FOLDER);
		if("1".equals(type)){
			intent.putExtra(FileSelectConstant.SELECTOR_IS_ZIP, false);
		}else{
			intent.putExtra(FileSelectConstant.SELECTOR_IS_ZIP, true);
		}
		startActivityForResult(intent, FILE_SELECTOR_REQUEST_CODE);
	}

	/*
	 * 从相机获取
	 */
//	private void camera() {
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PHOTO_PATH)));
//		startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
//	}
	
	/*
	 * 从相册获取
	 */
	private void gallery() {
		// 激活系统图库，选择一张图片
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == PHOTO_REQUEST_GALLERY) {
			if (data != null) {
				// 得到图片的全路径
				Uri uri = data.getData();
			}

		} else if (requestCode == PHOTO_REQUEST_CAMERA) {
			crop();
		} else if (requestCode == PHOTO_REQUEST_CUT) {
			photokey = data.getStringExtra("path");
			imageLoader.displayImage("file://" + photokey, img_by_takephoto,options);
			calCount = 0;
			errCount = 0;
			sbfile.setLength(0);
			ice(1,1,photokey);
		}else if(IMAGEFORPARKCODE1 == requestCode&&RESULT_OK == resultCode){
			mSelectPathForPark1.clear();
			mSelectPathForPark1 = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
			if (mSelectPathForPark1!=null&&mSelectPathForPark1.size()>0) {
				photokey = mSelectPathForPark1.get(0);
				crop();
			}
		}else if (FILE_SELECTOR_REQUEST_CODE == requestCode&&resultCode == RESULT_OK) {
			path.clear();
			path = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
			photokey = path.get(0);
			Log.i("ice", photokey);
			getImagePathFromSD(1);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/** 
	 * getImagePathFromSD
	 * @return 
	 */  
	private List<String> getImagePathFromSD(int type) {
		imagePathList.clear();
		sbfile.setLength(0);
		calCount = 0;
		errCount = 0;
		File fileAll = new File(photokey);  
		File[] files = fileAll.listFiles();
		count  = files.length;
		for (int i = 0; i < files.length; i++) {  
			File file = files[i];
			if(type == 1){
				if (checkIsImageFile(file.getPath())) {  
					imagePathList.add(file.getPath());
				}  
			}
		}
		
		sbfile.append("共"+count+"个文件"+",其中图片文件"+imagePathList.size()+"个\n");
		for (int j = 0; j < imagePathList.size(); j++) {
			photokey = imagePathList.get(j);
			Log.i("ice", imagePathList.get(j));
			if(j == (imagePathList.size()-1)) {
				ice(2,2,imagePathList.get(j));
			}else {
				ice(2,1,imagePathList.get(j));
			}
		}
		return imagePathList;  
	} 
	
	/** 
	 * checkIsImageFile
	 * @return 
	 */  
	@SuppressLint("DefaultLocale")
	private boolean checkIsImageFile(String fName) {  
		boolean isImageFile = false; 
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,  
		fName.length()).toLowerCase();  
		if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("jpeg")) {  
			isImageFile = true;  
		} else {  
			isImageFile = false;  
		}  
		return isImageFile;  
	}
	
	private void ice(final int type,final int finishType,final String picPath) {
		threadPool.submit(new Runnable() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Log.i("ice", "图片："+picPath);
					String responseStr = CarIceUtils.getCarInfo(picPath,type);
					JSONObject obj = new JSONObject(responseStr);
					if("208".equals(obj.optString("status"))) {
						++errCount;
						sbfile.append("车牌号："+null+",颜色："+null+"\n");
					}
			    	String result = obj.optString("result");
			    	JSONObject objResult = new JSONObject(result);
			    	String number = objResult.optString("number");
			    	String color = objResult.optString("color");
			    	sbfile.append("车牌号："+number+",颜色："+color+"\n");
			    	++calCount;
			    	Log.i("ice", "车牌号："+number+",颜色："+color);
			    	Log.i("ice", "执行次数："+calCount);
			    	if(1 == type) {
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tx_by_takephoto.setText(sbfile.toString());
								File photokeyFile = new File(photokey);
								if(photokeyFile.exists()) {
									photokeyFile.delete();
								}
							}
						});
			    	}else if(2 == type && 2 == finishType) {
				    	runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									Thread.sleep(5000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								img_by_takephoto.setVisibility(View.GONE);
								sbfile.append("其中有车牌数据"+calCount+"次,无车牌数据"+errCount+"次");
								tx_by_takephoto.setText(sbfile.toString());
							}
						});
			    	}
				}catch (Exception e) {
			    	e.printStackTrace();
			    }
			}
		});
	}

	/**
	 * 剪切图片
	 */
	private void crop() {
		Intent intent = new Intent(AliIceActivity.this, ImagePreviewAty.class);
		intent.putExtra("path", photokey);
		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}
	
}
