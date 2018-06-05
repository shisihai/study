package jcom;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Excel2Pdf {
	private static final int excel_Pdf=57;
//	public static void main(String[] args) {
//		excelToHtml("D:/1.xlsx", "D:/1.pdf");
//	}
	public static void excelToHtml(String xlsfile, String htmlfile){ 
	ActiveXComponent app =null; 
	Dispatch excel=null;
	Dispatch excels =null;
	try { 
		app=new ActiveXComponent("Excel.Application"); 
		app.setProperty("Visible", new Variant(false));
		app.setProperty("DisplayAlerts", new Variant(false)); //�ܾ�������ʾ
		excels = app.getProperty("Workbooks").toDispatch(); 
		excel = Dispatch.invoke(     
                 excels,     
                 "Open",     
                 Dispatch.Method,     
                 new Object[] { xlsfile, new Variant(false),     
                         new Variant(true) }, new int[1]).toDispatch();     
       Dispatch.call(excel, "SaveAs", htmlfile, new Variant(excel_Pdf));
	}catch (Exception e) { 
		e.printStackTrace(); 
	}finally{
		if(excel!=null){
			//�ر���Դ�ļ�
			Dispatch.call(excel, "Close", false); 
		}
		if(excels!=null){
			excels.safeRelease();
		}
		if(app!=null){
			//�ر�wordӦ�ó��� 
			app.invoke("Quit", new Variant[] {});
			app=null;
		}
		//�ͷ����߳���Դ
		ComThread.Release();
		ComThread.quitMainSTA();
	} 
	}
	}
