package com.tedu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * JDBCUtils工具类
 * @author lenovo
 *
 */
public class JDBCUtils {
	//私有化构造函数
	/**
	 * 为了防止创建该类的实例将构造方法私有化，由于该类中的方法是静态的，直接通过类名点方法就可以调用，所以不需要创建该类的实例。
	 */
	private JDBCUtils() {}
	private static Properties prop = new Properties();
	//读取配置文件
	static {
		try {
			String path = "conf.properties";
			//File需要绝对路径	文件conf.properties应该放在项目下
			prop.load(new FileInputStream(new File("F:/tedu/workspace/JAVA_SEE/HttpService/src/main/resources/"+path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//获取数据库连接对象
	public static Connection getConn() throws Exception{
		String driverClass = prop.getProperty("driverClass");
		String jdbcUrl = prop.getProperty("jdbcUrl");
		String user = prop.getProperty("user");
		String password = prop.getProperty("password");
		//注册数据库驱动
		Class.forName(driverClass);
		//获取连接
		Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
		return conn;
	}
	//释放资源
	public static void close(Connection conn,Statement stat,ResultSet rSet){
		if(rSet!=null){
			try {
				rSet.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				rSet =null;
			}
		}
		if(stat!=null){
			try {
				stat.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				stat=null;
			}
		}
		if(conn!=null){
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				conn=null;
			}
		}
	}
}
