package com.traveler.print.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static final String YYYYMMDD_TIME="yyyy-MM-dd HH:mm:ss";
	
	public static String nowTimeStr(Date date,String pattern){
		SimpleDateFormat format=new SimpleDateFormat(pattern);
		return format.format(date);
	}
}
