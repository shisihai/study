package com.traveler.print;
import javax.print.*;
public final  class PrintUtils {
	/**
	 * 根据地址获取打印机Service
	 * @param printerURI
	 * @return
	 */
	public static PrintService findPrintService(String printerURI){
		PrintService printService = null;
        //初始化打印机  
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null,null);  
        if (services != null && services.length > 0) {  
            for (PrintService service : services) {  
                if (printerURI.equalsIgnoreCase(service.getName())) {  
                    printService = service;  
                    break;  
                }  
            }  
        }  
        
        if (printService == null) {  
        	printService = PrintServiceLookup.lookupDefaultPrintService();
            if (services != null && services.length > 0) {  
            	System.out.println("可用的打印机列表:");  
                for (PrintService service : services) {  
                	System.out.println("["+service.getName()+"]");  
                }  
             }  
            if(printService == null){
            	System.out.println("没找到打印机:"+printerURI); 
               System.out.println("没找到打印机"+printerURI);
            }
        }else{ 
        	System.out.println("找到打印机：["+printerURI+"]"); 
        } 
        return printService;
	}
}