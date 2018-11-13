package com.ice.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * TimeUtils
 *
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.CHINA);

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     * @return
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }


    /**
     * 将毫秒时间转化为 “00:00”格式时间
     */
    public static String getTimeFromMillisecond(long time) {

        if (time <= 0) {
            return "0:00";
        }
        int second = (int) ((time / 1000) / 60);
        int million = (int) ((time / 1000) % 60);
        String f = String.valueOf(second);
        String m = million >= 10 ? String.valueOf(million) : "0"
                + String.valueOf(million);
        return f + ":" + m;
    }


    /**
     * 将毫秒时间转化为秒 “00”格式时间
     */
    public static String getTimeFromSecond(long time) {

        if (time <= 0) {
            return "00";
        }
        int second = (int) (time / 1000);
        String f = String.valueOf(second);
        return f;
    }
    /**获取长整形时间*/
	public static long getTime(){
		Calendar c = Calendar.getInstance(Locale.getDefault());
		return c.getTimeInMillis();
	}
    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 16:09:00"）
     * 
     * @param time
     * @return
     */
    public static String times(String time) {
            SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            @SuppressWarnings("unused")
            long lcc = Long.valueOf(time);
            int i = Integer.parseInt(time);
            String times = sdr.format(new Date(i * 1000L));
            return times;

    }
}
