package com.traveler.print.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.print.PrintService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.traveler.print.core.service.MqService;
import com.traveler.print.core.service.PrintTaskService;
import com.traveler.print.entity.JasperParams;
import com.traveler.print.entity.ResultMsg;
import com.traveler.print.utils.BaseController;
import com.traveler.print.utils.FileUtils;
import com.traveler.print.utils.OperationException;
import com.traveler.print.utils.PrintUtils;
import com.traveler.print.utils.ReportExportUtil;
import com.traveler.print.utils.SysConstans;

import net.sf.jasperreports.engine.JasperPrint;

@RestController
public class PrintQRCodeController extends BaseController<PrintQRCodeController> {
	@Autowired
	private PrintTaskService printTaskService;
	
	@Autowired
	private MqService mqService;

	/**
	 * @Description: 实时打印
	 * @author travler
	 * @return
	 * @date 2018年6月28日下午8:15:32
	 */
	@RequestMapping("/printTask")
	public Object printTask(@RequestBody JasperParams jasperParams) {
		return printTaskService.print(jasperParams);
	}
	/**
	 * @Description:异步延时打印
	 * @author travler
	 * @param jasperParams
	 * @return
	 * @date 2018年7月2日下午3:02:52
	 */
	@RequestMapping("/printAsync")
	public Object printAsync(@RequestBody JasperParams jasperParams) {
		return mqService.sendMsg(SysConstans.PRINTMQQUEUENAME, jasperParams);
	}
	
	
	
	@RequestMapping("/getTestJson")
	public Object geJson() {
		JasperParams jasperParams = new JasperParams();
		jasperParams.setJasperName("word_A4.jasper");
		jasperParams.setPrinterName("Brother HL-2560DN Printer (副本 1)");
		HashMap<String, Object> singleData = new HashMap<>();
		singleData.put("imgUrl", "http://localhost:8088/timg.jpg");
		singleData.put("title", "测试打印A4");
		jasperParams.setParamsMap(singleData);
		ArrayList<Map<String, Object>> data = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			HashMap<String, Object> params = new HashMap<>();
			params.put("uname", "小王" + i);
			params.put("uage", "" + new Random().nextInt(20));
			params.put("usex", i % 2 == 0 ? "男" : "女");
			data.add(params);
		}
		jasperParams.setDatas(data);
		return jasperParams;
	}

	/**
	 * @Description:打印A4纸张 例子
	 * @author travler
	 * @return
	 * @date 2018年6月29日下午6:27:06
	 */
	public Object printWord() {
		ResultMsg resultMsg = new ResultMsg();
		try {
			// 获取模板地址
			String jasperPath = FileUtils.findJasperPath("word_A4.jasper");
			// 非循环数据
			Map<String, Object> singleData = new HashMap<>();
			singleData.put("imgUrl", "http://localhost:8088/timg.jpg");
			singleData.put("title", "测试打印A4");
			// 循环数据
			ArrayList<Map<String, Object>> data = new ArrayList<>();
			for (int i = 0; i < 6; i++) {
				Map<String, Object> params = new HashMap<>();
				params.put("uname", "小王" + i);
				params.put("uage", "" + new Random().nextInt(20));
				params.put("usex", i % 2 == 0 ? "男" : "女");
				data.add(params);
			}
			// 填充模板
			JasperPrint jasperPrint = ReportExportUtil.IreportInjectionData(data, jasperPath, singleData);
			// 查找打印机
			PrintService printService = PrintUtils.findPrintService("Brother HL-2560DN Printer (副本 1)")
					.getPrintService();
			// 发布打印任务到打印机
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
