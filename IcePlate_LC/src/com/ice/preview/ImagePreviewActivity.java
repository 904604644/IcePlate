package com.ice.preview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ice.iceplate.R;
import com.ice.preview.zoom.PhotoView;
import com.ice.preview.zoom.ViewPagerFixed;
import com.ice.util.AppUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 这个是用于进行图片浏览时的界�?
 * �?要传入图片的来源�?1为网络图片，2为本地图片�??
 * 传入 图片路径�?
 * 图片位置
 * 是否显示删除按钮
 *
 */
public class ImagePreviewActivity extends Activity {
	//获取前一个activity传过来的position
	private int position;
	//当前的位�?
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed viewPagerFixed;
	private MyPageAdapter adapter;

	public ArrayList<String> imagePath = new ArrayList<String>();
	private Intent intent;
	private Context mContext;
	private ImageView actionback;
	private TextView simple_action_bar_title;
	private Button simple_action_bar_btn;
	private boolean isShowDelete = false;
	/**图片类型
	 * 分为本地和网络两�?
	 * 1为网络图�?
	 * 2为本地图�?*/
	private int ImageType = 1;
    /** 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合  */
    public static final String EXTRA_RESULT = "select_result";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_preview);
		intent = getIntent();
		position = intent.getIntExtra("position", 0);
		location = position;
		ImageType = intent.getIntExtra("imagetype", 0);
		isShowDelete = intent.getBooleanExtra("ishowdelete", false);
		imagePath = intent.getStringArrayListExtra("image");
		initView();
		for (int i = 0; i < imagePath.size(); i++) {
			try {
				if(ImageType == 2){					
					initListViews(Bimp.revitionImageSize(imagePath.get(i)));
				}else{
					if(imagePath.get(i).startsWith("http")){
						initListViewsForUrl(imagePath.get(i));
					}else{
						initListViewsForUrl(AppUtils.OSS_SAVE_URL+imagePath.get(i));
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		initListener();

		
		
	}
	private void initView(){
		viewPagerFixed = (ViewPagerFixed) findViewById(R.id.image_preview);
		actionback  = (ImageView) findViewById(R.id.simple_action_bar_back);
		simple_action_bar_title = (TextView) findViewById(R.id.simple_action_bar_title);
		simple_action_bar_btn = (Button) findViewById(R.id.simple_action_bar_btn);
		simple_action_bar_btn.setText(R.string.delete);
		simple_action_bar_btn.setTextColor(getResources().getColor(R.color.red));
//		simple_action_bar_btn.setBackgroundResource(R.drawable.button_ransparent_bg_red_frame);
		simple_action_bar_title.setText(position+1+"/"+imagePath.size());
		if(isShowDelete){
			simple_action_bar_btn.setVisibility(View.VISIBLE);
		}else{
			simple_action_bar_btn.setVisibility(View.GONE);
		}
		
	}
	private void initListener(){
		viewPagerFixed.setOnPageChangeListener(pageChangeListener);
		adapter = new MyPageAdapter(listViews);
		viewPagerFixed.setAdapter(adapter);
		viewPagerFixed.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.image_preview_margin));
		viewPagerFixed.setCurrentItem(position);
		
		actionback.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				setResult();
			}
		});
		
		simple_action_bar_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(imagePath.size() == 1){
					imagePath.clear();
					setResult();
				}else if(imagePath.size() >1){
					imagePath.remove(location);
					listViews.clear();
					for (int i = 0; i < imagePath.size(); i++) {
						try {
							if(ImageType == 2){					
								initListViews(Bimp.revitionImageSize(imagePath.get(i)));
							}else{
								initListViewsForUrl(imagePath.get(i));
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					adapter.setListViews(listViews);
					adapter.notifyDataSetChanged();
					if(location-1 >0){					
						viewPagerFixed.setCurrentItem(location-1);
						simple_action_bar_title.setText(location+1+"/"+imagePath.size());
					}else{
						viewPagerFixed.setCurrentItem(0);
						simple_action_bar_title.setText(1+"/"+imagePath.size());
					}
				}
			}
		});
		
	}
	private void setResult(){
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, imagePath);
        setResult(RESULT_OK, data);
        finish();
	}
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			if(imagePath !=  null && imagePath.size() >0)
			simple_action_bar_title.setText(arg0+1+"/"+imagePath.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	private void initListViews(Bitmap bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		img.setImageBitmap(bm);
		listViews.add(img);
	}
	private void initListViewsForUrl(String url) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoView img = new PhotoView(this);
		img.setBackgroundColor(0xff000000);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		AppUtils.ImageLoader(url, img, 1);
		listViews.add(img);
	}


	/**
	 * 监听返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult();
		}
		return true;
	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
}
