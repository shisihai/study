package com.traveler.print.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FileUtils {
	/**
	 * @Description:拷贝模板到可识别目录
	 * @author travler
	 * @date 2018年6月29日下午4:07:20
	 */
	public static void copyFile() {
		//日志
		StringBuffer files=new StringBuffer();
		try {
			//获取容器资源解析器
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			//获取所有匹配的文件
			Resource[] resources;
			//文件名称
			String temptName=null;
			//目标地址
			String targetFilePath=null;
			//模板目录
			resources = resolver.getResources(ResourceUtils.CLASSPATH_URL_PREFIX+"printTemplate/*.jasper");
			for(Resource resource : resources) {
	            //获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流
	            InputStream stream = resource.getInputStream();
	            temptName=resource.getFilename();
	            targetFilePath = SysConstans.RUNJARPATH +File.separator+ temptName;
	            File ttfFile = new File(targetFilePath);
	            FileCopyUtils.copy(FileCopyUtils.copyToByteArray(stream),ttfFile);
	            files.append(temptName+"-->" + targetFilePath +" 完成；");
	        }
		} catch (IOException e) {
			e.printStackTrace();
			log.info("复制模板文件到文件夹："+SysConstans.RUNJARPATH," copyFile  ",null);
		}
		log.info("模板文件拷贝完成："+files.toString(),"copyFile",null);
	}
	
	/**
	 * @Description:查找当前项目jar同级目录下的文件绝对路径
	 * @author travler
	 * @param jasperName
	 * @return
	 * @throws OperationException
	 * @date 2018年7月2日上午11:12:21
	 */
	public static String findJasperPath(String jasperName) throws OperationException{
		String path = null;
		try {
			File file=ResourceUtils.getFile(SysConstans.RUNJARPATH +File.separator+ jasperName);
			if(file.exists()) {
				path=file.getPath();
			}else {
				throw new OperationException(jasperName+"文件不存在！");
			}
		} catch (FileNotFoundException e) {
			log.error(ExceptionUtil.formatStackTrace(e),"BaseController.JasperPath",null);
			throw new OperationException("没有找到当前文件！"+ path==null?jasperName:path);
		}
		return path;
	}
}
