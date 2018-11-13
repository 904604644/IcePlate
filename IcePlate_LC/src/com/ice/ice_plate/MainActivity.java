package com.ice.ice_plate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ice.iceplate.ActivateService;
import com.ice.iceplate.R;
import com.ice.util.CarIceUtils;

import dr.android.fileselector.FileSelectConstant;

public class MainActivity extends Activity implements OnClickListener {
	private static final Integer FILE_SELECTOR_REQUEST_CODE = 2016;
	private Button btnPic;
	private Button btnVedio;
	private Button btnActivate;
	private EditText editText;
	private boolean recogMode;
	private TextView tv_company;
	private TextView tv_version;
	private String myType;

	public ActivateService.ActivateBinder acBinder;
	public ServiceConnection acConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			acConnection = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			acBinder = (ActivateService.ActivateBinder) service;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		//Log.i("wu", "��ʼִ�г���!!!!!!!");
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent actiIntent = new Intent(MainActivity.this, ActivateService.class);
		bindService(actiIntent, acConnection, Service.BIND_AUTO_CREATE);

		setViews();
		setListeners();

	}

	private void setListeners() {
		btnPic.setOnClickListener(this);
		btnVedio.setOnClickListener(this);
		btnActivate.setOnClickListener(this);
	}

	private void setViews() {
		btnPic = (Button) findViewById(R.id.btn_by_photo);
		btnVedio = (Button) findViewById(R.id.btn_by_vedio);
		btnActivate = (Button) findViewById(R.id.btn_activate);
		tv_company = (TextView) findViewById(R.id.tv_company);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_company.setText(R.string.company_name);
		tv_version.setText("�汾��Ϣ:" + getVersion());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_by_photo:
			new Thread(new Runnable() {	
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						String responseStr = CarIceUtils.getCarInfo("/sdcard/qaz/IMG_20180628_174312.jpg",1);
						JSONObject obj = new JSONObject(responseStr);
				    	String result = obj.optString("result");
				    	JSONObject objResult = new JSONObject(result);
				    	String number = objResult.optString("number");
				    	String color = objResult.optString("color");
					}catch (Exception e) {
				    	e.printStackTrace();
				    }
				}
			}).start();
//			showSelectBg(1);
			break;

		case R.id.btn_by_vedio:
//			showSelectBg(2);
			Intent intent = new Intent(MainActivity.this, CameraActivity.class);
			recogMode = true;
			intent.putExtra("camera", recogMode);
			startActivity(intent);
			break;

		case R.id.btn_activate:
			activateSN();
			break;
		}
	}
	
	/**
     * ���ѡ��Ի���
     */
    @SuppressWarnings("deprecation")
    private void showSelectBg(final int type) {
    	View exitAppView = LayoutInflater.from(this).inflate(
    			R.layout.dialog_show_sex_bg, null);
    	final PopupWindow exitAppWindow = new PopupWindow(exitAppView,
    			ViewGroup.LayoutParams.MATCH_PARENT,
    			ViewGroup.LayoutParams.MATCH_PARENT);
    	// �����ڿɵ��
    	exitAppWindow.setFocusable(true);
    	// ������ɵ��
    	exitAppWindow.setOutsideTouchable(true);
    	exitAppWindow.setBackgroundDrawable(new BitmapDrawable());
    	exitAppWindow.showAtLocation(exitAppView, Gravity.BOTTOM, 0, 0);
    	TextView tvToken = (TextView) exitAppView.findViewById(R.id.tv_token);
    	TextView tvFile = (TextView) exitAppView.findViewById(R.id.tv_file);
    	TextView tvZip = (TextView) exitAppView.findViewById(R.id.tv_zip);
    	if(type ==1){
    		tvToken.setText("����ʶ��");
    		tvFile.setText("ͼƬ�ļ�ʶ��");
    		tvZip.setText("ѹ�����ļ�ʶ��");
    	}else if (type ==2){
    		tvToken.setText("��Ƶʶ��");
    		tvFile.setText("��Ƶ�ļ�ʶ��");
    		tvZip.setText("ѹ�����ļ�ʶ��");
    	}
    	exitAppView.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			exitAppWindow.dismiss();
    		}
    	});
    	
    	tvToken.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			exitAppWindow.dismiss();
    			if(type == 1){
    				recogMode = false;
    				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
    				intent.putExtra("camera", recogMode);
    				startActivity(intent);
    			}else if(type == 2){
    				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
    				recogMode = true;
    				intent.putExtra("camera", recogMode);
    				startActivity(intent);
    			}
    		}
    	});
    	tvFile.setOnClickListener(new View.OnClickListener() {
			@Override
    		public void onClick(View v) {
    			exitAppWindow.dismiss();
    			if(type == 1){
//    				getImagePathFromSD(1);
//    				Intent intent = new Intent(MainActivity.this, CameraFromPicActivity.class);
//    				intent.putExtra("type", "file");
//    				startActivity(intent);
    				myType = "file";
    				startActivity("1");
    			}else if(type == 2){
    				getImagePathFromSD(2);
    			}
    		}
    	});
    	
    	tvZip.setOnClickListener(new View.OnClickListener() {
    		@Override
    		public void onClick(View v) {
    			exitAppWindow.dismiss();
    			if(type == 1){
//    				getImagePathFromSD(1);
//    				Intent intent = new Intent(MainActivity.this, CameraFromPicActivity.class);
//    				intent.putExtra("type", "zip");
//    				startActivity(intent);
    				myType = "zip";
    				startActivity("2");
    			}else if(type == 2){
    				getImagePathFromSD(2);
    			}
    		}
    	});
    	
    }
    
    void startActivity(String type){
//    	Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.setType("*/*");//ѡ��ͼƬ
//		// intent.setType(��audio/*��); //ѡ����Ƶ
//		// intent.setType(��video/*��); //ѡ����Ƶ ��mp4 3gp ��android֧�ֵ���Ƶ��ʽ��
//		// intent.setType(��video/*;image/*��);//ͬʱѡ����Ƶ��ͼƬ
//		intent.addCategory(Intent.CATEGORY_OPENABLE);
//		startActivityForResult(intent, 1);
		
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
    
    
	
	/** 
	* ��sd����ȡͼƬ��Դ 
	* @return 
	*/  
	private List<String> getImagePathFromSD(int type) {  
		// ͼƬ�б�   
		List<String> imagePathList = new ArrayList<String>();  
		// �õ�sd����image�ļ��е�·��   File.separator(/)
		String filePath = null;
		if(type == 1){
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator+ "image";  
		}else if(type == 2){
			filePath = Environment.getExternalStorageDirectory().toString() + File.separator+ "video";
		}
		// �õ���·���ļ��������е��ļ�   
		File fileAll = new File(filePath);  
		File[] files = fileAll.listFiles();  
		// �����е��ļ�����ArrayList��,����������ͼƬ��ʽ���ļ�   
		for (int i = 0; i < files.length; i++) {  
			File file = files[i];
			if(type == 1){
				if (checkIsImageFile(file.getPath())) {  
					imagePathList.add(file.getPath());  
				}  
			}else if(type == 2){
				if (checkIsVideoFile(file.getPath())) {  
					imagePathList.add(file.getPath());  
				}  
			}
		}  
		// ���صõ���ͼƬ�б�   
		return imagePathList;  
	}  
	
	/** 
	* �����չ�����õ�ͼƬ��ʽ���ļ� 
	* @param fName  �ļ��� 
	* @return 
	*/  
	@SuppressLint("DefaultLocale")  
	private boolean checkIsImageFile(String fName) {  
		boolean isImageFile = false;  
		// ��ȡ��չ��   
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,  
		fName.length()).toLowerCase();  
		if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")|| FileEnd.equals("jpeg")|| FileEnd.equals("bmp") ) {  
			isImageFile = true;  
		} else {  
			isImageFile = false;  
		}  
		return isImageFile;  
	}
	
	private boolean checkIsVideoFile(String fName) {  
		boolean isVideoFile = false;
		String fileExtension = MimeTypeMap.getFileExtensionFromUrl(fName);  
		String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension);
		if (mimeType!=null&&mimeType.contains("video")){
			isVideoFile = true;
		}
		return isVideoFile;  
	}

	private void activateSN() {
		editText = new EditText(this);
		new AlertDialog.Builder(this)
				.setTitle(R.string.dialog_title)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(editText)
				.setPositiveButton(R.string.license_verification,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								String snInput = editText.getText().toString()
										.trim();
								int code = acBinder.login(snInput);
								if (code == 0) {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage("��ϲ,���򼤻�ɹ�!").show();
								} else if (code == 1795) {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage(
													"���򼤻�ʧ��,����Ļ��������Ѵ����ޣ���Ȩ�벻���ڸ���Ļ�����ʹ��")
											.show();
								} else if (code == 1793) {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage("���򼤻�ʧ��,��Ȩ���ѹ���").show();
								} else if (code == 276) {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage(
													"���򼤻�ʧ��,û���ҵ���Ӧ�ı�����Ȩ��������ļ�")
											.show();
								} else if (code == 284) {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage(
													"���򼤻�ʧ��,��Ȩ���������������Ȩ��ƴд�Ƿ���ȷ")
											.show();
								} else {
									new AlertDialog.Builder(MainActivity.this)
											.setMessage("���򼤻�ʧ��,������Ϊ��" + code)
											.show();
								}

							}
						})
				.setNegativeButton(R.string.disactivate,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								Toast.makeText(MainActivity.this, "ȡ������Ȩ����",
										Toast.LENGTH_SHORT).show();
							}
						}).show();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (acBinder != null) {
			unbindService(acConnection);
		}
	}
	
	String path;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*if (resultCode == Activity.RESULT_OK) {
			Uri uri = data.getData();
			if ("file".equalsIgnoreCase(uri.getScheme())) {// ʹ�õ�����Ӧ�ô�
				path = uri.getPath();
				int index = path.lastIndexOf("/");
				path = path.substring(0, index);
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			}
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {// 4.4�Ժ�
				path = getPath(this, uri);
				int index = path.lastIndexOf("/");
				path = path.substring(0, index);
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			} else {// 4.4������ϵͳ���÷���
				path = getRealPathFromURI(uri);
				int index = path.lastIndexOf("/");
				path = path.substring(0, index);
				Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
			}
			Intent intent = new Intent(MainActivity.this, CameraFromPicActivity.class);
			intent.putExtra("type", myType);
			intent.putExtra("path", path);
			startActivity(intent);
		}*/
		if (resultCode == RESULT_OK) {
			ArrayList<String> path = data.getStringArrayListExtra(FileSelectConstant.SELECTOR_BUNDLE_PATHS);
			Toast.makeText(this, path.get(0),Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(MainActivity.this, CameraFromPicActivity.class);
			intent.putExtra("type", myType);
			intent.putExtra("path", path.get(0));
			startActivity(intent);
		}
	}

	/**
	 * ��ȡ�汾��
	 * 
	 * @return ��ǰӦ�õİ汾��
	 */
	private String getVersion() {
		String version = null;
		try {
			PackageManager manager = this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
			version = info.versionName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return version;
	}
	
	public String getRealPathFromURI(Uri contentUri) {
		String res = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = getContentResolver().query(contentUri, proj, null,
				null, null);
		if (null != cursor && cursor.moveToFirst()) {
			;
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			res = cursor.getString(column_index);
			cursor.close();
		}
		return res;
	}

	/**
	 * רΪAndroid4.4��ƵĴ�Uri��ȡ�ļ�����·������ǰ�ķ����Ѳ���ʹ
	 */
	@SuppressLint("NewApi")
	public String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/"
							+ split[1];
				}
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"),
						Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] { split[1] };

				return getDataColumn(context, contentUri, selection,
						selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 * 
	 * @param context
	 *            The context.
	 * @param uri
	 *            The Uri to query.
	 * @param selection
	 *            (Optional) Filter used in the query.
	 * @param selectionArgs
	 *            (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

}
