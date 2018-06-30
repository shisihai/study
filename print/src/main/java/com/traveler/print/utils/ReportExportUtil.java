package com.traveler.print.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.print.PrintService;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.ExporterInput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;
import net.sf.jasperreports.export.SimplePrintServiceReportConfiguration;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
/**
 * 导出报表工具
 * @author SShi11
 *
 */
public class ReportExportUtil {
	private final static Logger loger = LoggerFactory.getLogger(ReportExportUtil.class);
	public static final String  REPORTDATAKEY="reportDataKey";// 数据集合key
	public static final String  EXPORTFILESUFFIX="exportFileSuffix";// 导出文件的后缀  支持 html，pdf，doc，xls
	public static final String  EXPORTFILENAME="exportFileName";//导出的文件名称
	public static final String  ONEPAGEPERSHEET="onePagePerSheet";//excel导出是否每页占一个sheet 默认false
	public static final String SQLPARAMS="sqlParams";//sql过滤语句
	public static final String SHOWINFOPARAMS="showInfoParams";//页面显示的过滤条件信息
	public static final String HTMLPATH="htmlPath";//生成的html的路径
	/**
	 * @Description:
	 * @author travler
	 * @param data 数据集或数据库连接
	 * @param jasperPath  模板地址
	 * @param fileProperties  变量
	 * @return
	 * @throws OperationException
	 * @date 2018年6月28日下午7:57:15
	 */
	@SuppressWarnings("unchecked")
	public static JasperPrint IreportInjectionData(Object data, String jasperPath, Map<String,Object> fileProperties) throws OperationException{
		JasperPrint jasperPrint=null;
		//数据集合形式注入数据到模板
		JRDataSource jrDataSource=null;
		//模板中的参数Map
		Map<String,Object> model=new HashMap<>();
		try {
			//填充fields字段数据map值
			if(fileProperties != null && !fileProperties.isEmpty()) {
				model.putAll(fileProperties);
			}
			//1.初始化打印对象，根据传入的data类型判断是否为数据集，如果是，使用数据集合形式初始化打印对象。如果是数据库连接，则使用数据库连接形式初始化
			if( data instanceof Connection ){
				//对于注入数据集合
				jasperPrint=JasperFillManager.fillReport(jasperPath, model, (Connection) data);
			}else{
				//对于注入数据集合
				if(data != null) {
					jrDataSource = new JRMapCollectionDataSource((Collection<Map<String, ?>>) data);
				}else {
					jrDataSource = new JRMapCollectionDataSource(null);
				}
				model.put(REPORTDATAKEY, jrDataSource);
				jasperPrint = JasperFillManager.fillReport(jasperPath, model, jrDataSource);
			}
		} catch (JRException e) {
			e.printStackTrace();
			loger.error(ExceptionUtil.formatStackTrace(e), "报表导出工具方法IreportTools",null);
			throw new OperationException("报表导出异常！");
		}finally {
			//关闭连接
			if(data instanceof Connection ){
				closeConn((Connection) data);
			}
		}
		return jasperPrint;
	}

	/**
	 * @Description:将模板转化为文件 用于预览
	 * @author travler
	 * @param jasperPrint
	 * @param destFileName
	 * @return
	 * @throws OperationException
	 * @date 2018年6月28日下午4:09:16
	 */
	public static String preview(JasperPrint jasperPrint,String destFileName) throws OperationException {
		String htmlName = null;
		try {
			JasperExportManager.exportReportToHtmlFile(jasperPrint, destFileName);
			loger.info("报表已生成,路径为："+new String(destFileName.getBytes("ISO8859_1"), "utf-8"), "报表导出工具",null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			loger.error(ExceptionUtil.formatStackTrace(e), "preview",null);
			throw new OperationException("模板转化成文件失败！");
		} catch (JRException e) {
			e.printStackTrace();
			loger.error(ExceptionUtil.formatStackTrace(e), "preview",null);
			throw new OperationException("模板转化成文件失败！");
		}
		return htmlName;
	}
	
	/**
	 * @Description:发布打印任务
	 * @author travler
	 * @param printService
	 * @param jasperPrint
	 * @throws OperationException
	 * @date 2018年6月28日下午8:23:27
	 */
	public static void print(PrintService printService,JasperPrint jasperPrint) throws OperationException {
		try {
			JRPrintServiceExporter jrAbstractExporter=new JRPrintServiceExporter();
			SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();
			configuration.setPrintService(printService);
			configuration.setDisplayPageDialog(false);
			configuration.setDisplayPrintDialog(false);
			configuration.setOverrideHints(false);
			jrAbstractExporter.setConfiguration(configuration);
			SimplePrintServiceReportConfiguration configuration2=new SimplePrintServiceReportConfiguration();
			jrAbstractExporter.setConfiguration(configuration2);
			ExporterInput exporterInput=new SimpleExporterInput(jasperPrint);
			jrAbstractExporter.setExporterInput(exporterInput);
			jrAbstractExporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
			loger.error(ExceptionUtil.formatStackTrace(e), "print",null);
			throw new OperationException("打印任务失败！");
		}
	}

	/**
	 * 导出数据
	 * @param fileProperties
	 * @param response
	 * @param jasperPrint
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 * @throws JRException
	 * @author SShi11
	 */
	public static void exportReport(Map<String, Object> fileProperties, HttpServletResponse response,JasperPrint jasperPrint) throws IOException, UnsupportedEncodingException, JRException {
		//导出的文件名
		String exportFileName=StringUtils.obj2Str(fileProperties.get(EXPORTFILENAME));
		//文件后缀
		String exportFileSuffix=StringUtils.obj2Str(fileProperties.get(EXPORTFILESUFFIX));
		exportFileSuffix=exportFileSuffix.toUpperCase();
		//输出流
		OutputStream os=response.getOutputStream();
		//导出的文件名转码
		String fileName=null;
		switch (exportFileSuffix) {
		case "XLS":
			response.setContentType("application/vnd.ms-excel");
			response.setCharacterEncoding("UTF-8");
			fileName = exportFileName + ".xls";
			fileName = new String(fileName.getBytes("utf-8"), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			JRXlsExporter xlsExporter = new JRXlsExporter();
			xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(os));
			SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
			//导出exlcel 文件格式设置
			//每页占一个sheef
			Object onePagePerSheetObj=fileProperties.get(ONEPAGEPERSHEET);
			boolean onePagePerSheet=onePagePerSheetObj==null?false:(Boolean)onePagePerSheetObj;
			xlsReportConfiguration.setOnePagePerSheet(onePagePerSheet);//分sheet页导出
			xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
			xlsReportConfiguration.setDetectCellType(false);
			xlsReportConfiguration.setWhitePageBackground(false);
			xlsReportConfiguration.setCollapseRowSpan(false);
			xlsExporter.setConfiguration(xlsReportConfiguration);
			xlsExporter.exportReport();
			break;
		case "DOC":
			fileName = exportFileName + ".doc";
			response.setContentType("application/msword;charset=utf-8");
			fileName = new String(fileName.getBytes("utf-8"), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
			JRRtfExporter rtfRxporter = new JRRtfExporter();
			rtfRxporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			rtfRxporter.setExporterOutput(new SimpleWriterExporterOutput(os));
			rtfRxporter.exportReport();
			break;
		default:
			fileName = exportFileName + ".pdf";
			response.setContentType("application/pdf");
			response.setCharacterEncoding("UTF-8");
			fileName = new String(fileName.getBytes("utf-8"), "ISO8859_1");
			response.setHeader("Content-disposition", "attachment; filename=" + fileName);
			JasperExportManager.exportReportToPdfStream(jasperPrint, os);
		}
		os.flush();
		os.close();
		loger.info(new String(fileName.getBytes("ISO8859_1"), "utf-8")+"报表已生成", "报表导出工具",null);
	}
	/**
	 * 释放数据库连接
	 * @param conn
	 * @author SShi11
	 */
	private static void closeConn(Connection conn){
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				loger.error(ExceptionUtil.formatStackTrace(e), "报表导出工具关闭数据库连接异常",null);
			}
		}
	}
}
