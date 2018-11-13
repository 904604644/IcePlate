package com.ice.util;

import android.graphics.Bitmap.Config;

import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * QIXIN ImageLoader DisplayImageOption<BR />
 * </ul>
 * 
 * @author luojh
 *
 */
public final class DisplayImageOption {

	/**
	 * Show Http Image Default Option<BR />
	 * Tips: you can set showImageOnLoading, Call Build() At the last
	 * <P />
	 * <ul>
	 * <li>save cache in Memory</li>
	 * <li>save cache in File</li>
	 * <li>bitmapConfig RGB_565</li>
	 * </ul>
	 */
	public static Builder getHttpBuilder() {
		return new Builder().cacheInMemory(true).cacheOnDisc(true).resetViewBeforeLoading(true).considerExifParams(true)
				.bitmapConfig(Config.RGB_565);
	}

	/**
	 * Show Http Image Default Option<BR />
	 * Tips: Call Build() At the last
	 * <P />
	 * <ul>
	 * <li>save cache in Memory</li>
	 * <li>save cache in File</li>
	 * <li>bitmapConfig RGB_565</li>
	 * </ul>
	 * 
	 * @param defaultImageResourceID
	 *            the Stub Image when loading or load failed
	 */
	public static Builder getHttpBuilder(int defaultImageResourceID) {
		return new Builder().cacheInMemory(false).cacheOnDisk(false).bitmapConfig(Config.ARGB_8888)
				.resetViewBeforeLoading(true).considerExifParams(true).showImageOnLoading(defaultImageResourceID)
				.showImageOnFail(defaultImageResourceID).showImageForEmptyUri(defaultImageResourceID);
	}

	public static Builder getAvatarBuilder(int defaultImageResourceID) {
		return new Builder().cacheInMemory(true).cacheOnDisc(true).bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).showImageOnLoading(defaultImageResourceID)
				.showImageOnFail(defaultImageResourceID).showImageForEmptyUri(defaultImageResourceID);
	}

	/**
	 * Show Local Image Default Option Tips: Call Build() At the last
	 * <P />
	 * <ul>
	 * <li>Don't save cache in Memory</li>
	 * <li>Don't save cache in File</li>
	 * <li>bitmapConfig RGB_565</li>
	 * </ul>
	 */
	public static Builder getLocalBuilder() {
		return new Builder().cacheInMemory(false).cacheOnDisc(false).considerExifParams(true)
				.resetViewBeforeLoading(true).bitmapConfig(Config.RGB_565);
	}

}
