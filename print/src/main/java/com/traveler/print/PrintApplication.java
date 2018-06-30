package com.traveler.print;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import com.traveler.print.utils.SysConstans;
@SpringBootApplication
public class PrintApplication {
	private final static Logger loger = LoggerFactory.getLogger(PrintApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(PrintApplication.class, args);
	    ApplicationHome home = new ApplicationHome(PrintApplication.class);
	    File jarFile = home.getSource();
	    SysConstans.runJarPath=jarFile.getParentFile().getPath();
	    loger.info("主程序已启动!运行目录为："+SysConstans.runJarPath," application station ",null);
	    copyFile();
	}
	
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
	            targetFilePath = SysConstans.runJarPath +File.separator+ temptName;
	            File ttfFile = new File(targetFilePath);
	            FileCopyUtils.copy(FileCopyUtils.copyToByteArray(stream),ttfFile);
	            files.append(temptName+"-->" + targetFilePath +" 完成；");
	        }
		} catch (IOException e) {
			e.printStackTrace();
			loger.info("复制模板文件到文件夹："+SysConstans.runJarPath," copyFile  ",null);
		}
		loger.info("模板文件拷贝完成："+files.toString(),"copyFile",null);
	}
}
