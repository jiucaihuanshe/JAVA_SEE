package dataSource.c3p0;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import dataSource.utils.JDBCUtils;

public class C3P0 {
	public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			/**
			 * 1.创建一个连接池对象(实例)	ComboPooledDataSource
			 * 由于这是别人开发好的连接池，在开发时不知道要连接哪一个数据库，也不知道连接的数据库的用户名和密码，
			 * 因此这里我们需要告诉c3p0程序连接数据库的四个基本信息
			 * 通过set方法设置连接数据库的基本信息
			 * 2.从连接池中获取一个连接
			 * 3.获取传输器并设置参数
			 * 4.执行sql语句，并返回执行结果
			 */
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setJdbcUrl("jdbc:mysql:///jt_db");
			cpds.setUser("root");
			cpds.setPassword("root");
			conn = cpds.getConnection();
			String sql = "select * from account where id=?";
			ps = conn.prepareStatement(sql);
			ps.setInt(1, 3);
			rs = ps.executeQuery();//这里不要在传sql
			if(rs.next()){
				System.out.println(rs.getInt("id")+":"+rs.getString("name")+":"+rs.getDouble("money"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			/**
			 * 如果连接是从连接池中获取的，用完之后一定要还回连接池中！！
			 * conn.close();
			 * 如果连接是从连接池中获取的，这个连接对象已经被改造过了，调用close方法
			 * 就是将连接还回连接池中，反之，如果哦连接对象是自己跟数据库要的，连接对象
			 * 就是原生的没有经过改造，调用close方法就是将连接关闭还给数据库
			 */
			JDBCUtils.close(conn, ps, rs);
		}
	}
}
