package dataSource.utils;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * JDBC工具类
 * @author wangchaofan
 *
 */
import java.util.Properties;
public class JDBCUtils {
	//私有化构造函数
	private JDBCUtils(){}
	private static Properties prop = new Properties();
	static {
		try {
			//读取conf.properties配置文件
			String path = "F:/tedu/workspace/JAVA_SEE/webService/src/main/java/dataSource/utils/conf.properties";
			prop.load(new FileInputStream(new File(path)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取数据库连接对象
	 */
	public static Connection getConn() throws Exception{
		String driverClass = prop.getProperty("driverClass");
		String jdbcUtil = prop.getProperty("jdbcUrl");
		String user = prop.getProperty("user");
		String password= prop.getProperty("password");
		//注册数据库驱动
		Class.forName(driverClass);
		//获取连接
		Connection conn = DriverManager.getConnection(jdbcUtil, user, password);
		return conn;
	}
	/**
	 * 释放资源
	 */
	public static void close(Connection conn, 
			Statement stat, ResultSet rs){
		if(rs != null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				rs = null;
			}
		}
		if(stat != null){
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				stat = null;
			}
		}
		if(conn != null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				conn = null;
			}
		}
	}
}
