package com.traveler.print.core.service;

import java.util.Collection;
import java.util.Map;

import javax.print.PrintService;

import org.springframework.stereotype.Service;

import com.traveler.print.entity.JasperParams;
import com.traveler.print.entity.PrinterService;
import com.traveler.print.entity.ResultMsg;
import com.traveler.print.utils.ExceptionUtil;
import com.traveler.print.utils.FileUtils;
import com.traveler.print.utils.OperationException;
import com.traveler.print.utils.PrintUtils;
import com.traveler.print.utils.ReportExportUtil;
import com.traveler.print.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JasperPrint;

@Service
@Slf4j
public class PrintTaskService {
	
	/**
	 * @Description:基于ireport打印服务
	 * @author travler
	 * @param jasperParams
	 * @return
	 * @date 2018年7月2日上午11:26:55
	 */
	public ResultMsg print(JasperParams jasperParams) {
		ResultMsg resultMsg=new ResultMsg();
		String msg="";
		String warnMsg="";
		try {
			String jasperTempName=jasperParams.getJasperName();
			//校验变量数据
			Map<String,Object> paramsMap=jasperParams.getParamsMap();
			if(paramsMap==null || paramsMap.isEmpty()) {
				warnMsg += "变量数据 Map<String, Object> 为空！";
			}
			//校验循环体数据
			Collection<Map<String,Object>>  datas=jasperParams.getDatas();
			if(datas==null || datas.isEmpty()) {
				warnMsg += " 循环体数据 Collection<Map<String, Object>> 为空！";
			}
			String printerName=jasperParams.getPrinterName();
			//查找打印机
			PrinterService printerService=PrintUtils.findPrintService(printerName);
			if(StringUtils.isEmpty(printerName) || printerService.isDefault()) {
				if(StringUtils.isEmpty(printerName)) {
					warnMsg += "未指定打印机！使用系统默认打印机执行任务！";
				}else {
					warnMsg += "未找到打印机："+printerName+"！使用系统默认打印机执行任务！";
				}
			}
			PrintService printService=printerService.getPrintService();
			//获取模板地址
			String jasperPath = FileUtils.findJasperPath(jasperTempName);
		    //填充模板
			JasperPrint jasperPrint=ReportExportUtil.IreportInjectionData(datas, jasperPath, paramsMap);
			//发布打印任务到打印机
			ReportExportUtil.print(printService, jasperPrint);
			resultMsg.setFlag(true);
			msg = "打印任务执行成功！"+warnMsg;
		} catch (OperationException e) {
			msg=e.getMessage();
		}catch(Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.formatStackTrace(e),"printQRCode",null);
		}
		resultMsg.setMsg(msg);
		return resultMsg;
	}
}
