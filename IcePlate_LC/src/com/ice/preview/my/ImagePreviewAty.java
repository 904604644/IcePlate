package com.ice.preview.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.ice.ice_plate.AliIceActivity;
import com.ice.iceplate.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 裁剪
 * 
 * @author Administrator
 * 
 */
public class ImagePreviewAty extends Activity implements OnClickListener {

	private CropImageView iv;
	private String path;
	private long lastTime = 0;
	private TextView title;
	private TextView function;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preview);
		iv = (CropImageView) findViewById(R.id.iv);
		title = (TextView) findViewById(R.id.title);
		title.setText("车牌裁剪");
		function = (TextView) findViewById(R.id.function_tv);
		function.setVisibility(View.VISIBLE);
		function.setText("确定");
		function.setOnClickListener(this);
		findViewById(R.id.function_img).setVisibility(View.GONE);
		path = getIntent().getStringExtra("path");
		// 拍照
		if (path != null) {
			Bitmap bit = BitmapFactory.decodeFile(path);
			// 设置资源和默认长宽
			iv.setDrawable(new BitmapDrawable(bit), 280, 140, bit.getWidth(),bit.getHeight());
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.file_select, menu);
		MenuItem item_ok = menu.findItem(R.id.action_fileselect_ok);
		item_ok.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		return true;
	}

	/**
	 * actionbar 返回键和完成键的操作
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			this.finish();
			return true;
		} else if (id == R.id.action_fileselect_ok) {
			onClickOkBtn();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	private void onClickOkBtn() {
		Bitmap mBitmap = iv.getCropImage();
		Uri uri = bitmapTOUrl(mBitmap);
		path = uri.toString().substring(uri.toString().indexOf("///") + 2);// 图片上传路径
		Intent data = new Intent();
		data.putExtra("path", path);
		setResult(1012, data);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(ImagePreviewAty.this, AliIceActivity.class);
		startActivity(intent);
		finish();
	}

	/***
	 * bitmapTOUrl
	 * 
	 * @param bitmap
	 * @return
	 */
	private File currentImageFile;

	private Uri bitmapTOUrl(Bitmap bitmap) {
		if (hasSdcard()) {
			File dir = new File(Environment.getExternalStorageDirectory(),"/image");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			currentImageFile = new File(dir.getAbsolutePath()+ "/crop.png");
			try {
				FileOutputStream fos = new FileOutputStream(currentImageFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 45, fos);
				fos.flush();
				fos.close();
				return Uri.fromFile(currentImageFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			Toast.makeText(ImagePreviewAty.this, "没有SD卡", Toast.LENGTH_SHORT)
					.show();
			return null;
		}
	}

	private boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.function_tv:
			if (System.currentTimeMillis() - lastTime > 1000) {
				Bitmap mBitmap = iv.getCropImage();
				Uri uri = bitmapTOUrl(mBitmap);
				path = uri.toString().substring(uri.toString().indexOf("///") + 2);// 图片上传路径
				Intent data = new Intent();
				data.putExtra("path", path);
				setResult(1012, data);
				finish();
			} else {
				return;
			}
			lastTime = System.currentTimeMillis();
			break;

		default:
			break;
		}
	}
	
	/**顶部返回按钮*/
	/*public void btnBack(View view){
		Bitmap mBitmap = iv.getCropImage();
		Uri uri = bitmapTOUrl(mBitmap);
		myPath = uri.toString().substring(
				uri.toString().indexOf("///") + 2);// 图片上传路径
		Intent data = new Intent();
		data.putExtra("path", myPath);
		setResult(1012, data);
		finish();
	}*/

}
