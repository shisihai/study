package com.traveler.print.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
/**
 * 异常打印
 * @author SShi11
 *
 */
public final class ExceptionUtil {
	public static String formatStackTrace(Throwable e) {
        StringWriter sw = new StringWriter();
        try {
            PrintWriter p = new PrintWriter(sw);
            e.printStackTrace(p);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }
        return sw.toString();
    }
}
