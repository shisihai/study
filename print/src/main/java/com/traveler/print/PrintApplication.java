package com.traveler.print;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;

import com.traveler.print.utils.FileUtils;
import com.traveler.print.utils.SysConstans;

import lombok.extern.slf4j.Slf4j;
@SpringBootApplication
@Slf4j
public class PrintApplication {
	public static void main(String[] args) {
		SpringApplication.run(PrintApplication.class, args);
	    ApplicationHome home = new ApplicationHome(PrintApplication.class);
	    File jarFile = home.getSource();
	    SysConstans.RUNJARPATH=jarFile.getParentFile().getPath();
	    log.info("主程序已启动!运行目录为："+SysConstans.RUNJARPATH," application station ",null);
	    FileUtils.copyFile();
	}
	
}
