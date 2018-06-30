package com.traveler.print.utils;

public class StringUtils extends org.springframework.util.StringUtils {
	/**
	 * @Description:把object对象转成str
	 * @author travler
	 * @param obj
	 * @return null 返回""
	 * @date 2018年6月28日上午9:55:09
	 */
	public static String obj2Str(Object obj) {
		return obj==null?"":obj.toString();
	}
}
