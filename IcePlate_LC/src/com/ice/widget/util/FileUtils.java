package com.ice.widget.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * 文件操作类
 */
public class FileUtils {

    public static File createTmpFile(Context context){

        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            // 已挂载
        	File pic = null;  
			pic = new File(Environment.getExternalStorageDirectory()+"/camera");
			if (!pic.exists()) {
				pic.mkdirs();
			}
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "camera_"+timeStamp;
            File tmpFile = new File(pic, fileName+".jpg");
            return tmpFile;
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
            String fileName = "camera_"+timeStamp;
            File tmpFile = new File(cacheDir, fileName+".jpg");
            return tmpFile;
        }

    }

}
