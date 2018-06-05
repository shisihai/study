package jcom;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Doc2Html {
	//pdf的格式代号
	private static final int WORD_HTML = 8;
	public static void main(String[] args) {
        word2PDF("D:/P5CNS00011-FAT-测试文档07-设备管理.doc", "D:/P5CNS00011-FAT-测试文档07-设备管理.html");
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
	        //格式转换
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(WORD_HTML) }, new int[1]);       
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
