package com.traveler.print.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.UUID;
import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.db.DBHelper;
/**
 * 系统自动日志记录
 * <p>
 * 功能描述：日志处理类 参数说明：第一个：日志、模块 、 操作人       loger.info("日志内容", "操作模块","操作人");
 * </p>
 * @author SShi11
 */
public class Logger extends DBAppender {
	// 日志插入语句
	private static final String insertSql = "INSERT INTO PRINT_SYS_LOGGERINFO (ID,LEVEL_CODE,MODULE_CODE,DOUSER,CREATE_TIME,NOTE_TEXT,NOTE_TEXT2) VALUES (?,?, ?, ?,?,?,?)";
	@Override
	public void append(ILoggingEvent eventObject) {
		Object[] baseMsg = eventObject.getArgumentArray();
		if (isSave(baseMsg)) {
			String id = UUID.randomUUID().toString();
			this.save(
					id, 
					eventObject.getLevel().levelStr,
					StringUtils.obj2Str(baseMsg[0]),
					StringUtils.obj2Str(baseMsg[1]==null?"printSys":baseMsg[1]),
					DateUtil.nowTimeStr(new Date(), DateUtil.YYYYMMDD_TIME),
					eventObject.getMessage()
					);
		}

	}

	/**
	 * <p>
	 * 功能描述：保存操作日志
	 * </p>
	 * 
	 * @param infoLeavel
	 * @param module
	 * @param doUser
	 * @param createDate
	 * @param msg
	 * @author SShi11
	 */
	private void save(String id, String infoLeavel, String module, String doUser,String nowTime, String msg) {
		Connection connection = null;
		PreparedStatement insertStatement = null;
		try {
			connection = super.connectionSource.getConnection();
			insertStatement = connection.prepareStatement(insertSql);
			synchronized (this) {
				int charLength=msg.length();
				String context=null;
				String nextContext=null;
				// 保存数据
				insertStatement.setObject(1, id);
				insertStatement.setObject(2, infoLeavel);
				insertStatement.setObject(3, module);
				insertStatement.setObject(4, doUser);
				insertStatement.setObject(5, nowTime);
				//最大只保存8000字符
				if(charLength <= 4000){
					context=msg.substring(0, charLength);
				}else{
					context=msg.substring(0, 4000);
					nextContext=msg.substring(4000,charLength-4000>4000?8000:charLength);
				}
				insertStatement.setObject(6, context);
				insertStatement.setObject(7, nextContext);
				insertStatement.execute();
			}
		} catch (Exception sqle) {
			sqle.printStackTrace();
		} finally {
			DBHelper.closeStatement(insertStatement);
			DBHelper.closeConnection(connection);
		}
	}

	/**
	 * <p>
	 * 功能描述：判断是否需要保存改日志信息
	 * </p>
	 * 
	 * @param baseMsg
	 * @return
	 * @author SShi11
	 */
	private boolean isSave(Object[] baseMsg) {
		if (baseMsg == null || baseMsg.length != 2) {
			return false;
		} else {
			return true;
		}
	}
}
