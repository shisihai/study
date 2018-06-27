package com.traveler.print.utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.ConnectionPoolDataSource;

import biz.source_code.miniConnectionPoolManager.MiniConnectionPoolManager;

public class ExecuteSql {
	private static MiniConnectionPoolManager poolMgr;
	static {
		initDBPool();
	}

	/**
	 * <p>
	 * 功能描述：初始化连接�?
	 * </p>
	 * 作�?�：SShi11 日期：Apr 6, 2017 12:45:31 PM
	 */
	private static void initDBPool() {
		try {
			ConnectionPoolDataSource dataSource = DataSourceFactory.createDataSource();
			poolMgr = new MiniConnectionPoolManager(dataSource, 20, 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 功能描述：执行增删改操作sql
	 * </p>
	 * 
	 * @param sql
	 * @param params
	 * @throws SQLException
	 *             作�?�：SShi11 日期：Apr 6, 2017 1:03:38 PM
	 */
	public static void execSql(String sql, Object... params) throws SQLException {
		Connection conn = getConn();
		PreparedStatement prst = null;
		prst = conn.prepareStatement(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				prst.setObject(i + 1, params[i]);
			}
		}
		prst.executeUpdate();
		closeResouse(conn, prst, null);
	}

	/**
	 * <p>
	 * 功能描述：查�?
	 * </p>
	 * 
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 *             作�?�：SShi11 日期：Apr 24, 2017 10:19:43 AM
	 */
	public static List<Map<String, Object>> queryBySql(String sql, Object... params) throws Exception {
		Connection connection = getConn();
		PreparedStatement prst = null;
		prst = connection.prepareStatement(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				prst.setObject(i + 1, params[i]);
			}
		}
		ResultSet resultSet = prst.executeQuery();
		ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
		int columnCount = resultSetMetaData.getColumnCount();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> rowData = null;
		while (resultSet.next()) {
			rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				rowData.put(resultSetMetaData.getColumnName(i).toUpperCase(), resultSet.getObject(i));
			}
			result.add(rowData);
		}
		closeResouse(connection, prst, null);
		return result;
	}

	/**
	 * <p>
	 * 功能描述：关闭资�?
	 * </p>
	 * 
	 * @param conn
	 * @param prst
	 * @param stmt
	 * @throws SQLException
	 *             作�?�：SShi11 日期：Apr 6, 2017 1:03:25 PM
	 */
	private static void closeResouse(Connection conn, PreparedStatement prst, Statement stmt) throws SQLException {
		if (stmt != null) {
			stmt.close();
		}
		if (prst != null) {
			prst.close();
		}
		if (conn != null) {
			conn.close();
		}
	}

	/**
	 * <p>
	 * 功能描述：获取数据库连接资源
	 * </p>
	 * 
	 * @return
	 * @throws SQLException
	 *             作�?�：SShi11 日期：Apr 6, 2017 12:50:51 PM
	 */
	private static Connection getConn() throws SQLException {
		Connection connection = null;
		if (poolMgr == null)
			initDBPool();
		connection = poolMgr.getConnection();
		return connection;
	}

}
