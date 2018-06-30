package com.traveler.print.entity;

import java.util.Collection;
import java.util.Map;

import com.traveler.print.utils.OperationException;
import com.traveler.print.utils.StringUtils;
import com.traveler.print.utils.WarnException;
public class JasperParams {
	//模板名称
	private String jasperName;
	//打印机名称
	private String printerName;
	//循环的数据
	private Collection<Map<String,Object>> datas;
	//非循环的变量
	private Map<String,Object> paramsMap;
	
	
	public String getJasperName() throws OperationException {
		if(StringUtils.isEmpty(jasperName)){
			throw new OperationException("模板名称不能为空！");
		}
		return jasperName;
	}
	public void setJasperName(String jasperName) {
		this.jasperName = jasperName;
	}
	public String getPrinterName() throws WarnException {
		return printerName;
	}
	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}
	public Collection<Map<String, Object>> getDatas() throws WarnException {
		return datas;
	}
	public void setDatas(Collection<Map<String, Object>> datas) {
		this.datas = datas;
	}
	public Map<String, Object> getParamsMap() throws WarnException {
		return paramsMap;
	}
	public void setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
	}
	
}
