package com.traveler.print.core.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.print.PrintService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.traveler.print.entity.JasperParams;
import com.traveler.print.entity.PrinterService;
import com.traveler.print.entity.ResultMsg;
import com.traveler.print.utils.BaseController;
import com.traveler.print.utils.ExceptionUtil;
import com.traveler.print.utils.OperationException;
import com.traveler.print.utils.PrintUtils;
import com.traveler.print.utils.ReportExportUtil;
import com.traveler.print.utils.StringUtils;

import net.sf.jasperreports.engine.JasperPrint;
@RestController
public class PrintQRCodeController extends BaseController<PrintQRCodeController>{
	/**
	 * @Description: 测试打印二维码
	 * @author travler
	 * @return
	 * @date 2018年6月28日下午8:15:32
	 */
	@RequestMapping("/printTask")
	public Object printQRCode(@RequestBody JasperParams jasperParams) {
		ResultMsg resultMsg=new ResultMsg();
		String msg="打印任务执行成功！";
		try {
			String jasperTempName=jasperParams.getJasperName();
			//校验变量数据
			Map<String,Object> paramsMap=jasperParams.getParamsMap();
			if(paramsMap==null || paramsMap.isEmpty()) {
				msg += "变量数据 Map<String, Object> 为空！";
			}
			//校验循环体数据
			Collection<Map<String,Object>>  datas=jasperParams.getDatas();
			if(datas==null || datas.isEmpty()) {
				msg += " 循环体数据 Collection<Map<String, Object>> 为空！";
			}
			String printerName=jasperParams.getPrinterName();
			//查找打印机
			PrinterService printerService=PrintUtils.findPrintService(printerName);
			if(StringUtils.isEmpty(printerName) || printerService.isDefault()) {
				if(StringUtils.isEmpty(printerName)) {
					msg += "未指定打印机！使用系统默认打印机执行任务！";
				}else {
					msg += "未找到打印机："+printerName+"！使用系统默认打印机执行任务！";
				}
			}
			PrintService printService=printerService.getPrintService();
			//获取模板地址
			String jasperPath = findJasperPath(jasperTempName);
		    //填充模板
			JasperPrint jasperPrint=ReportExportUtil.IreportInjectionData(datas, jasperPath, paramsMap);
			
			//发布打印任务到打印机
			ReportExportUtil.print(printService, jasperPrint);
			resultMsg.setFlag(true);
			resultMsg.setMsg(msg);
		} catch (OperationException e) {
			resultMsg.setMsg(e.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			loger.error(ExceptionUtil.formatStackTrace(e),"printQRCode",null);
		}
		return resultMsg;
	}
	
	@RequestMapping("/getTestJson")
	public Object geJson() {
		JasperParams jasperParams=new JasperParams();
		jasperParams.setJasperName("word_A4.jasper");
		jasperParams.setPrinterName("Brother HL-2560DN Printer (副本 1)");
		Map<String,Object> singleData=new HashMap<>();
		singleData.put("imgUrl", "http://localhost:8088/timg.jpg");
		singleData.put("title", "测试打印A4");
		jasperParams.setParamsMap(singleData);
		ArrayList<Map<String,Object>> data=new ArrayList<>();
		for(int i=0;i<6;i++) {
			Map<String,Object> params=new HashMap<>();
			params.put("uname", "小王"+i);
			params.put("uage", ""+new Random().nextInt(20));
			params.put("usex", i%2==0?"男":"女");
			data.add(params);
		}
		jasperParams.setDatas(data);
		return jasperParams;
	}
	
	/**
	 * @Description:打印A4纸张  例子
	 * @author travler
	 * @return
	 * @date 2018年6月29日下午6:27:06
	 */
	public Object printWord() {
		ResultMsg resultMsg=new ResultMsg();
		try {
			//获取模板地址
			String jasperPath = findJasperPath("word_A4.jasper");
			//非循环数据
			Map<String,Object> singleData=new HashMap<>();
			singleData.put("imgUrl", "http://localhost:8088/timg.jpg");
			singleData.put("title", "测试打印A4");
			//循环数据
			ArrayList<Map<String,Object>> data=new ArrayList<>();
			for(int i=0;i<6;i++) {
				Map<String,Object> params=new HashMap<>();
				params.put("uname", "小王"+i);
				params.put("uage", ""+new Random().nextInt(20));
				params.put("usex", i%2==0?"男":"女");
				data.add(params);
			}
		    //填充模板
			JasperPrint jasperPrint=ReportExportUtil.IreportInjectionData(data, jasperPath, singleData);
			//查找打印机
			PrintService printService=PrintUtils.findPrintService("Brother HL-2560DN Printer (副本 1)").getPrintService();
			//发布打印任务到打印机
			ReportExportUtil.print(printService, jasperPrint);
			resultMsg.setFlag(true);
			resultMsg.setMsg(jasperPath);
		} catch (OperationException e) {
			e.printStackTrace();
			resultMsg.setFlag(false);
			resultMsg.setMsg(e.getMessage());
		}
		return resultMsg;
	}
	
	
}
