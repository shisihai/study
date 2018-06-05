package jcom;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Excel2Html {
	private static final int EXCEL_HTML = 44;
	public static void main(String[] args) {
		excelToHtml("D:/第三方接口数据结构汇总v.2.0.xlsx", "D:/第三方接口数据结构汇总.html");
	}
	public static void excelToHtml(String xlsfile, String htmlfile){ 
	ActiveXComponent app =null; 
	Dispatch excel=null;
	Dispatch excels =null;
	try { 
		app=new ActiveXComponent("Excel.Application"); 
		app.setProperty("Visible", new Variant(false));
		app.setProperty("DisplayAlerts", new Variant(false)); //拒绝弹框提示
		excels = app.getProperty("Workbooks").toDispatch(); 
		excel = Dispatch.invoke(     
                 excels,     
                 "Open",     
                 Dispatch.Method,     
                 new Object[] { xlsfile, new Variant(false),     
                         new Variant(true) }, new int[1]).toDispatch();     
       Dispatch.call(excel, "SaveAs", htmlfile, new Variant(EXCEL_HTML));
	}catch (Exception e) { 
		e.printStackTrace(); 
	}finally{
		if(excel!=null){
			//关闭资源文件
			Dispatch.call(excel, "Close", false); 
		}
		if(excels!=null){
			excels.safeRelease();
		}
		if(app!=null){
			//关闭word应用程序 
			app.invoke("Quit", new Variant[] {});
			app=null;
		}
		//释放主线程资源
		ComThread.Release();
		ComThread.quitMainSTA();
	} 
	}
	}
