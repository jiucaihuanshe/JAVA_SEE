package dataSource.pool;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sql.DataSource;

import dataSource.utils.JDBCUtils;

/**
 * 自定义连接池
 * @author wangchaofan
 *
 */
public class MyPool implements DataSource{
	//1.类实现javax.sql.DataSource接口
	//2.创建一个容器（LinkedList）,当作连接池使用
	private static List<Connection> list = new LinkedList<>();
	//3.在静态代码块中，初始化一批连接放入连接池中
	static {
		try {
			//获取连接并存入连接池中
			for(int i=0;i<5;i++){
				Connection conn = JDBCUtils.getConn();
				list.add(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//4.实现getConnection方法，方便获取连接
	public Connection getConnection() throws SQLException{
		if(list.isEmpty()){
			//如果集合为空，即连接池中连接耗尽时，再次向数据库获取3个连接
			for(int i=0;i<3;i++){
				Connection conn;
				try {
					conn = JDBCUtils.getConn();
					list.add(conn);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Connection conn = list.remove(0);
		return conn;
	}
	//5.添加自定义方法returnConn,用于将连接还回连接池中
	public void returnConn(Connection conn){
		try {
			if(conn!=null && !conn.isClosed()){
				list.add(conn);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
}
