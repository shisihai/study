package com.traveler.print.core.service;

import com.traveler.print.entity.ResultMsg;
public interface SoapService {
	public ResultMsg soapPrintTask(String jasperParams);
}
