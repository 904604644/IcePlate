package com.ice.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class CarIceUtils {
	public static String getCarInfo(String picPath,int type) {
		String responseStr = null;
	    String host = "http://jisucpsb.market.alicloudapi.com";
	    String path = "/licenseplaterecognition/recognize";
	    String method = "POST";
	    String appcode = "096c9d829fae42fc99fdb52f63d0e391";
	    Map<String, String> headers = new HashMap<String, String>();
	    headers.put("Authorization", "APPCODE " + appcode);
	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
	    Map<String, String> querys = new HashMap<String, String>();
	    Map<String, String> bodys = new HashMap<String, String>();
	    String picBase64String = FileUtils.bitmapToString(picPath,type);
	    bodys.put("pic", picBase64String);


	    try {
	    	HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
	    	responseStr = EntityUtils.toString(response.getEntity());
	    	Log.i("CarIceUtils",responseStr);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return responseStr;
	}
}
