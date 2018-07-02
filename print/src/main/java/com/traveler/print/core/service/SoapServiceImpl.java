package com.traveler.print.core.service;

import javax.jws.WebMethod;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.traveler.print.entity.JasperParams;
import com.traveler.print.entity.ResultMsg;
@WebService
public class SoapServiceImpl implements SoapService{
	@Autowired
	private PrintTaskService printTaskService;
	@Override
	@WebMethod
	public ResultMsg soapPrintTask(String jasperParamsJson) {
		Gson gson=new Gson();
		JasperParams jasperParams=gson.fromJson(jasperParamsJson, JasperParams.class);
		if(printTaskService == null) {
			printTaskService = new PrintTaskService();
		}
		ResultMsg resultMsg = printTaskService.print(jasperParams);
		return resultMsg;
	}
}
