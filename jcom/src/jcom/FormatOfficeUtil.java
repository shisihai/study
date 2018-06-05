package jcom;

import java.io.File;
import java.util.Scanner;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * 将doc、docx转换成pdf，xls、xlsx转换成html以达到在线预览的功能
 * @author shisihai
 */
public class FormatOfficeUtil {
	private static final int word_pdf=17;
	private static final int EXCEL_HTML = 44;
	private static FormatOfficeUtil f=null;
	private static ActiveXComponent app=null;
	private FormatOfficeUtil(Integer type){
		if(type==1 && app==null){
			app = new ActiveXComponent("Word.Application"); 
		}else if(type==2 && app==null){
			app=new ActiveXComponent("Excel.Application"); 
		}
		//初始化
		ComThread.InitMTA(true);
		app.setProperty("Visible", false); //不可见
		app.setProperty("DisplayAlerts", new Variant(false)); //拒绝弹框提示
	}
	/**
	 *说明：
	 * @param type 
	 * 				1代表doc、docx文档，			
	 * 				2代表xls、xlsx
	 * @return
	 * shisihai  
	 * 20162016年1月7日下午12:54:48
	 */
	public static FormatOfficeUtil getInstance(Integer type){
		if(app==null && type==1){
			f=new FormatOfficeUtil(1);
		}else if(app==null && type==2){
			f=new FormatOfficeUtil(2);
		}
		return f;
	}
	/**
	 *说明：将doc、docx转换成pdf
	 * @param inputFile 原文档路径
	 * @param pdfFile   转换后文档路径
	 * shisihai  
	 * 20162016年1月7日下午1:05:30
	 */
	public  void formatDoc2Pdf(String inputFile,String pdfFile){
		//调用Documents对象中Open方法打开文档，并返回打开的文档对象Document 
		Dispatch doc=null;
		Dispatch docs =null;
		try {
			//获得word中所有打开的文档,返回Documents对象 
			docs = app.getProperty("Documents").toDispatch(); 
			doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] {                    
					inputFile, new Variant(false),new Variant(true) }, new int[1]).toDispatch(); 
			//删除已存在的pdf文档
			File f=new File(pdfFile);
			if(f.exists() && !f.isDirectory()){
				f.delete();
			}
			//保存转换后的文档
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(word_pdf) }, new int[1]);       
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(doc, docs);
		} 
	}
	
	/**
	 *说明：将xls、xlsx转成html（格式可能会丢失 IE正常）
	 * @param xlsfile   原文档路径
	 * @param htmlfile  转换后文档路径
	 * shisihai  
	 * 20162016年1月7日下午1:08:59
	 */
	public void formatExcel2Html(String xlsfile, String htmlfile){
		Dispatch excel=null;
		Dispatch excels =null;
		try { 
			excels = app.getProperty("Workbooks").toDispatch(); 
			excel = Dispatch.invoke(     
	                 excels,     
	                 "Open",     
	                 Dispatch.Method,     
	                 new Object[] { xlsfile, new Variant(false),new Variant(true) },
	                 new int[1]).toDispatch();     
	       Dispatch.call(excel, "SaveAs", htmlfile, new Variant(EXCEL_HTML));
		}catch (Exception e) { 
			e.printStackTrace(); 
		}finally{
			closeAll(excel, excels);
		} 
	}
	/**
	 *说明：关闭资源
	 * @param doc
	 * @param docs
	 * shisihai  
	 * 20162016年1月7日下午1:04:04
	 */
	private  void closeAll(Dispatch doc, Dispatch docs) {
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
		//退出主线程
		ComThread.quitMainSTA();
	}
	
	
	
	public static void main(String[] args) {
		FormatOfficeUtil u=FormatOfficeUtil.getInstance(1);
		Scanner input=new Scanner(System.in);
		System.out.println("输入doc路径：");
		String doc=input.nextLine();
		u.formatDoc2Pdf(doc, "D:/1.pdf");
		System.out.println("输入xls路径：");
		String xls=input.nextLine();
		u=FormatOfficeUtil.getInstance(2);
		u.formatExcel2Html(xls, "D:/2.html");
//		u=FormatOfficeUtil.getInstance(2);
//		u.formatExcel2Html("D:/1.xlsx", "D:/1.html");
		input.close();
	}
	
	
	
	
	
	
}
