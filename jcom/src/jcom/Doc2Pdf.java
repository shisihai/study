package jcom;

import java.io.File;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Doc2Pdf {
	//pdf�ĸ�ʽ����
	private static final int word_pdf=17;
	public static void main(String[] args) {
        word2PDF("D:/����ɽ���̳�MESϵͳ-��װ˵����_v1.0.docx", "D:/����ɽ���̳�MESϵͳ-��װ˵����_v1.0.pdf");
	}
	
	public static void word2PDF(String inputFile,String pdfFile){ 
		//��ʼ��
		ComThread.InitMTA(true);
        //��wordӦ�ó��� 
		ActiveXComponent app=null;
		//����Documents������Open�������ĵ��������ش򿪵��ĵ�����Document 
		Dispatch doc=null;
		Dispatch docs =null;
		try {
			app = new ActiveXComponent("Word.Application"); 
			//����word���ɼ�������ᵯ��word���� 
			app.setProperty("Visible", false); 
			app.setProperty("DisplayAlerts", new Variant(false)); //�ܾ�������ʾ
			//���word�����д򿪵��ĵ�,����Documents���� 
			docs = app.getProperty("Documents").toDispatch(); 
			doc = Dispatch.invoke(docs,"Open",Dispatch.Method,new Object[] {                    
					inputFile, new Variant(false),new Variant(true) }, new int[1]).toDispatch();  
			 File tofile = new File(pdfFile);      
	            if (tofile.exists()) {      
	                tofile.delete();      
	            }  
	            
	         Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(word_pdf) }, new int[1]);       
			//����Document�����SaveAs���������ĵ�����Ϊpdf��ʽ 
//			Dispatch.call(doc, 
//			        "ExportAsFixedFormat", 
//			        pdfFile, 
//			        word_pdf        //word����Ϊpdf��ʽ 
//			        );
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
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
			ComThread.quitMainSTA();
		} 
    }
	
}
