package com.traveler.print.core.service;

import javax.jms.Destination;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import com.traveler.print.entity.JasperParams;
import com.traveler.print.entity.ResultMsg;
import com.traveler.print.utils.SysConstans;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MqService {
	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	
	@Autowired
	private PrintTaskService printTaskService;
	/**
	 * @Description:发送消息到mq
	 * @author travler
	 * @param mqQueuesName  队列名称
	 * @param payload       内容
	 * @return
	 * @date 2018年7月2日下午2:59:34
	 */
	public ResultMsg sendMsg(String mqQueuesName,Object payload) {
		ResultMsg resultMsg=new ResultMsg();
		String msg="";
		try {
			Destination destination=new ActiveMQQueue(mqQueuesName);
			jmsMessagingTemplate.convertAndSend(destination, payload);
			resultMsg.setFlag(true);
			msg="打印任务已发布！";
		} catch (MessagingException e) {
			e.printStackTrace();
			msg="发送消息到队列："+mqQueuesName+" 异常！";
			log.error("发送消息到队列："+mqQueuesName+" 异常！","MqService.sendMsg",null);
		}
		resultMsg.setMsg(msg);
		return resultMsg;
	}
	
	/**
	 * @Description:监听打印任务
	 * @author travler
	 * @param jasperParams
	 * @date 2018年7月2日下午5:38:45
	 */
	@JmsListener(destination=SysConstans.PRINTMQQUEUENAME)
	public void printQueueListenter(JasperParams jasperParams) {
		printTaskService.print(jasperParams);
	}
}
