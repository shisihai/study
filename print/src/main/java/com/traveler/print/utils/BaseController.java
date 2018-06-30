package com.traveler.print.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

public class BaseController<T> {
	protected   Logger loger = LoggerFactory.getLogger(getType());
	public String findJasperPath(String jasperName) throws OperationException{
		String path = null;
		try {
			File file=ResourceUtils.getFile(SysConstans.runJarPath +File.separator+ jasperName);
			if(file.exists()) {
				path=file.getPath();
			}else {
				throw new OperationException(jasperName+"文件不存在！");
			}
		} catch (FileNotFoundException e) {
			loger.error(ExceptionUtil.formatStackTrace(e),"BaseController.JasperPath",null);
			throw new OperationException("没有找到当前文件！"+ path==null?jasperName:path);
		}
		return path;
	}
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
