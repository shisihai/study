package jcom;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Doc2Html {
	//pdf�ĸ�ʽ����
	private static final int WORD_HTML = 8;
	public static void main(String[] args) {
        word2PDF("D:/P5CNS00011-FAT-�����ĵ�07-�豸����.doc", "D:/P5CNS00011-FAT-�����ĵ�07-�豸����.html");
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
	        //��ʽת��
			Dispatch.invoke(doc, "SaveAs", Dispatch.Method, new Object[] {                
	        		 pdfFile, new Variant(WORD_HTML) }, new int[1]);       
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
