package com.emailUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmailUtils {
	static Properties properties=null;
	static String formAddr=null;
	static String pwd=null;
	static{
		properties=new Properties();
		InputStream iStream=SendEmailUtils.class.getResourceAsStream("config.properties");
		try {
			properties.load(iStream);
			formAddr=properties.getProperty("formAddr");
			pwd=properties.getProperty("pwd");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @Description:STMP协议发送邮件
	 * @author SShi11
	 * @param sendName  发送人昵称
	 * @param subject   主题
	 * @param content   内容
	 * @param filePath  附件路径
	 * @param addrs     收件人地址
	 * @throws UnsupportedEncodingException
	 * @throws MessagingException
	 * @date Apr 19, 20184:28:03 PM
	 */
	public static void SendEmail(String sendName,String subject,String content,String filePath,Address[] addrs) throws UnsupportedEncodingException, MessagingException{
		Session session = Session.getInstance(properties); // 创建会话对象
		MimeMessage message = new MimeMessage(session); // 创建邮件对象
		// From: 发件人
		message.setFrom(new InternetAddress(formAddr, sendName, "UTF-8"));
		//  To: 收件人
		message.setRecipients(MimeMessage.RecipientType.TO, addrs);
		// 邮件主题
		message.setSubject(subject, "UTF-8");
		//设置内容
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(content);
		// 附件
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		if(filePath != null){
			messageBodyPart = new MimeBodyPart();
	        DataSource source = new FileDataSource(new File(filePath));
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(filePath);
	        multipart.addBodyPart(messageBodyPart);
		}
		message.setContent(multipart);
		//  设置显示的发件时间
		message.setSentDate(new Date());
		//  保存前面的设置
		message.saveChanges();
		Transport transport=session.getTransport();
		transport.connect(formAddr, pwd);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	
	public static void main(String[] args) throws IOException, MessagingException {
		Address[] addresses=new InternetAddress[]{new InternetAddress("hothoot@126.com", "126邮箱", "UTF-8"),new InternetAddress("zxc2462006@qq.com", "QQ邮箱", "UTF-8")};
		SendEmail("MES", "LongiMES 附件", "Longi MES 正式上线通知：内含有附件，请仔细查阅...",null, addresses);
	}
}
