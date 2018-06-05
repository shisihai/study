package jcom;

import java.io.File;
import java.util.Scanner;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

/**
 * ��doc��docxת����pdf��xls��xlsxת����html�Դﵽ����Ԥ���Ĺ���
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
		//��ʼ��
		ComThread.InitMTA(true);
		app.setProperty("Visible", false); //���ɼ�
		app.setProperty("DisplayAlerts", new Variant(false)); //�ܾ�������ʾ
	}
	/**
	 *˵����
	 * @param type 
	 * 				1����doc��docx�ĵ���			
	 * 				2����xls��xlsx
	 * @return
	 * shisihai  
	 * 20162016��1��7������12:54:48
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
	 *˵������doc��docxת����pdf
	 * @param inputFile ԭ�ĵ�·��
	 * @param pdfFile   ת�����ĵ�·��
	 * shisihai  
	 * 20162016��1��7������1:05:30
	 */
	public  void formatDoc2Pdf(String inputFile,String pdfFile){
		//����Documents������Open�������ĵ��������ش򿪵��ĵ�����Document 
		Dispatch doc=null;
		Dispatch docs =null;
		try {
			//���word�����д򿪵��ĵ�,����Documents���� 
			docs = app.getProperty("Documents").toDispatch(); 
			doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] {                    
					inputFile, new Variant(false),new Variant(true) }, new int[1]).toDispatch(); 
			//ɾ���Ѵ��ڵ�pdf�ĵ�
			File f=new File(pdfFile);
			if(f.exists() && !f.isDirectory()){
				f.delete();
			}
			//����ת������ĵ�
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(word_pdf) }, new int[1]);       
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			closeAll(doc, docs);
		} 
	}
	
	/**
	 *˵������xls��xlsxת��html����ʽ���ܻᶪʧ IE������
	 * @param xlsfile   ԭ�ĵ�·��
	 * @param htmlfile  ת�����ĵ�·��
	 * shisihai  
	 * 20162016��1��7������1:08:59
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
	 *˵�����ر���Դ
	 * @param doc
	 * @param docs
	 * shisihai  
	 * 20162016��1��7������1:04:04
	 */
	private  void closeAll(Dispatch doc, Dispatch docs) {
		if(doc!=null){
			//�ر��ĵ� 
			Dispatch.call(doc, "Close",false); 
			doc=null;
		}
		if(docs!=null){
			docs.safeRelease();
			docs=null;
		}
		if(app!=null){
			//�ر�wordӦ�ó��� 
			app.invoke("Quit",new Variant[]{}); 
			app=null;
		}
		//�ͷ�com��Դ
		ComThread.Release();
		//�˳����߳�
		ComThread.quitMainSTA();
	}
	
	
	
	public static void main(String[] args) {
		FormatOfficeUtil u=FormatOfficeUtil.getInstance(1);
		Scanner input=new Scanner(System.in);
		System.out.println("����doc·����");
		String doc=input.nextLine();
		u.formatDoc2Pdf(doc, "D:/1.pdf");
		System.out.println("����xls·����");
		String xls=input.nextLine();
		u=FormatOfficeUtil.getInstance(2);
		u.formatExcel2Html(xls, "D:/2.html");
//		u=FormatOfficeUtil.getInstance(2);
//		u.formatExcel2Html("D:/1.xlsx", "D:/1.html");
		input.close();
	}
	
	
	
	
	
	
}
