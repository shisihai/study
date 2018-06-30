package com.traveler.print.utils;
import javax.print.*;

import com.traveler.print.entity.PrinterService;
public final  class PrintUtils {
	/**
	 * 根据地址获取打印机Service
	 * @param printerURI
	 * @return
	 * @throws OperationException 
	 */
	public static PrinterService findPrintService(String printerName) throws OperationException{
		PrinterService printerService=new PrinterService();
		PrintService printService = null;
        //初始化打印机  
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null,null);  
        if (services != null && services.length > 0) {  
            for (PrintService service : services) {  
                if (printerName.equalsIgnoreCase(service.getName())) {  
                    printService = service;  
                    break;  
                }  
            }  
        }
        //查找默认打印机
        if (printService == null) {
        	printService = PrintServiceLookup.lookupDefaultPrintService();
            if(printService == null){
            	throw new OperationException("没找到打印机"+printerName);
            }
            printerService.setDefault(true);
        }
        printerService.setPrintService(printService);
        return printerService;
	}
}