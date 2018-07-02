package com.traveler.print.utils;

import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class BaseController<T> {
	protected   Logger loger = LoggerFactory.getLogger(getType());
	/**
	 * 获取请求参数
	 * @param request
	 * @return
	 */
	public Map<String,Object> paramsMap(HttpServletRequest request){
		Map<String,Object> paramsMap=new HashMap<>();
		Enumeration<String> enumeration=request.getParameterNames();
		String paraName=null;
		Object value=null;
		while(enumeration.hasMoreElements()){
			paraName=enumeration.nextElement();
			value=request.getParameter(paraName);
			paramsMap.put(paraName, value);
		}
		return paramsMap;
	}
	@SuppressWarnings("unchecked")
	private Class<T> getType() {  
		Class <T> entityClass = (Class <T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return entityClass;
    }  
}
