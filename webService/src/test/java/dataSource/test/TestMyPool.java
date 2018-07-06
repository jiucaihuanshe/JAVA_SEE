package dataSource.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import dataSource.pool.MyPool;
import dataSource.utils.JDBCUtils;

/**
 * 测试自定义连接池
 * @author wangchaofan
 *
 */
public class TestMyPool {
	public static void main(String[] args) {
		Connection conn =null;
		Statement stat = null;
		ResultSet rs = null;
		MyPool pool = new MyPool();
		try {
			//从连接池中获取一个链接
			conn = pool.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from account where id=1");
			if(rs.next()){
				System.out.println(rs.getInt("id")+":"+rs.getString("name")+":"+rs.getDouble("money"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		} finally {
			JDBCUtils.close(conn, stat, rs);
			pool.returnConn(conn);
		}
	}
}
