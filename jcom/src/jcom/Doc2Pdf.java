package jcom;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Doc2Pdf {
	//pdf的格式代号
	private static final int word_pdf=17;
	public static void main(String[] args) {
        word2PDF("D:/井冈山卷烟厂MES系统-安装说明书_v1.0.docx", "D:/井冈山卷烟厂MES系统-安装说明书_v1.0.pdf");
	}
	
	public static void word2PDF(String inputFile,String pdfFile){ 
		//初始化
		ComThread.InitMTA(true);
        //打开word应用程序 
		ActiveXComponent app=null;
		//调用Documents对象中Open方法打开文档，并返回打开的文档对象Document 
		Dispatch doc=null;
		Dispatch docs =null;
		try {
			app = new ActiveXComponent("Word.Application"); 
			//设置word不可见，否则会弹出word界面 
			app.setProperty("Visible", false); 
			app.setProperty("DisplayAlerts", new Variant(false)); //拒绝弹框提示
			//获得word中所有打开的文档,返回Documents对象 
			docs = app.getProperty("Documents").toDispatch(); 
			doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] {                    
					inputFile, new Variant(false),new Variant(true) }, new int[1]).toDispatch();  
			 File tofile = new File(pdfFile);      
	            if (tofile.exists()) {      
	                tofile.delete();      
	            }  
	            
	         Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(word_pdf) }, new int[1]);       
			//调用Document对象的SaveAs方法，将文档保存为pdf格式 
//			Dispatch.call(doc, 
//			        "ExportAsFixedFormat", 
//			        pdfFile, 
//			        word_pdf        //word保存为pdf格式 
//			        );
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			if(doc!=null){
				//关闭文档 
				Dispatch.call(doc, "Close",false); 
				doc=null;
			}
			if(docs!=null){
				docs.safeRelease();
				docs=null;
			}
			if(app!=null){
				//关闭word应用程序 
				app.invoke("Quit",new Variant[]{}); 
				app=null;
			}
			//释放com资源
			ComThread.Release();
			ComThread.quitMainSTA();
		} 
    }
	
}
