package com.tedu.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.tedu.common.HttpContext;
import com.tedu.common.ServerContext;
import com.tedu.http.HttpRequest;
import com.tedu.http.HttpResponse;
import com.tedu.utils.JDBCUtils;

/**
 * 修改WebServer类, 开启线程来处理客户端的请求
 * 1.创建com.tedu.core.ClientHandler类, 并实现Runnable接口，提供构造方法接受socket并将其保存在类的内部。
 * 2.在WebServer中创建 ExecutorService实例, 用于管理处理客户端请求的线程,并在构造方法中对其进行初始化.
 * 
 * 重构ClientHandler类, 将客户端发送的请求消息封装在HttpRequest对象中
 * 重构ClientHandler类, 需要发送给浏览器的响应信息封装在HttpResponse对象中
 * 在ClientHandler类中, 将资源文件发送给客户端的代码抽取到一个responseFile方法中, 便于后期复用.
 * 
 * 修改ClientHandler类中的代码, 接受注册和登录请求
 * 1.如果浏览器请求的uri是以”/Regist”开头的表明是注册请求, 如果是以”/Login”开头, 表明是登录请求
 * @author wangchaofan
 *
 */
public class ClientHandler implements Runnable {
	private Socket socket;
	//接收socket,保存在类的内部
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		/**
		 * 实现ClientHandler类中的run方法来处理请求，并做出回应
		 * 	1.获取客户端的输入流, 用于获取客户端发送的请求信息, 并获取指向客户端的输出流, 向客户端发送响应信息
		 * 	#2.获取客户端发送的请求信息(先只获取第一行信息)
		 * 	2.修改ClientHandler类中run方法中的代码, 解析请求行信息, 获知浏览器所请求的资源文件.
		 *  3.向客户端发送响应信息
		 */
		try {
			InputStream in= socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			/**
			 * 创建HttpRequest对象,用于表示Http请求消息
			 */
			HttpRequest request = new HttpRequest(in);
			//创建HttpResponse对象,表示HTTP响应信息
			HttpResponse response = new HttpResponse(out);
			if(request.getUri()!=null){
				/**
				 * 如果uri是以/Regist开头，表明是注册请求
				 * 如果uri是以/Login开头，表明是登录请求
				 */
				if(request.getUri().startsWith("/Regist") || request.getUri().startsWith("/Login")){
					service(request,response);
					return;
				}
				
				//创建File对象表示浏览器所请求的资源文件
				File file = new File(ServerContext.WEB_ROOT+request.getUri());
				//如果请求的资源存在
				if(file.exists()){
					//设置状态码
					response.setStatus(HttpContext.STATUS_CODE_OK);
				}else{//如果请求的资源不存在
					file = new File(ServerContext.WEB_ROOT+"/"+ServerContext.NotFoundPage);
					response.setStatus(HttpContext.STATUS_CODE_NOT_FOUND);
				}
				//设置响应头
				response.setContentType(getContentType(file));//响应的数据格式
				response.setContentLength((int)file.length());//响应的数据长度
				//输出资源文件(即响应实体内容)
				responseFile(file, response);
				socket.close();
				System.out.println("响应客户端完毕...");
			}
			
			
			/*
			
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			//获取请求中信息的第一行
			String line = br.readLine();
			*//**
			 * 解析line(请求行)
			 *//*
			if(line!=null && line.length()>0){
				//浏览器请求资源的路径
				String uri = line.split("\\s")[1];//uri:/index.html
				System.out.println("uri:"+uri);
				
				//创建File对象表示浏览器所请求的资源文件
				//"F:/tedu/workspace/JAVA_SEE/HttpService/src/main/webapp/WEB-INF/webapps"+uri
				File file = new File("webapps"+uri);	//webappps/index.html
				System.out.println(file);
				
				//打印第一行信息
				//System.out.println("line:"+line);
				*//**
				 * 向客户端浏览器发送响应结果
				 * PrintStream用于包装其他输出流,能够为输出流提供便捷的功能,
				 * 便于打印各种值的输出形式
				 *//*
				PrintStream ps = new PrintStream(out);
				ps.println("HTTP/1.1 200 OK");//发送状态行
				ps.println("Context-Type:text/html");//发送响应头
				ps.println("content-Length:"+"HTTP/1.1 200 OK".length());//发送状态行
				ps.println();//发送一个空行
				
				//输出资源文件(即响应实体内容)
				//创建指向资源的流,用于读取文件的内容
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
				//将文件一次性读取到字节数组中保存
				byte[] buff = new byte[(int) file.length()];
				bis.read(buff);
				
				//输出资源文件(即响应实体内容)
				//ps.print("hello 2019...");
				ps.write(buff);
				socket.close();
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void service(HttpRequest request, HttpResponse response) throws IOException {
		if(request.getUri().startsWith("/Regist")){
			//处理注册请求
			System.out.println("处理注册请求");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			//通过JDBC技术奖用户名和密码保存到数据库中
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn =JDBCUtils.getConn();
				String sql = "insert into user values(null,?,?)";
				ps=conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, password);
				ps.executeUpdate();
				//响应浏览器(提示用户注册成功)
				File file = new File(ServerContext.WEB_ROOT+"/reg_success.html");
				response.setStatus(HttpContext.STATUS_CODE_OK);
				response.setContentType(getContentType(file));
				response.setContentLength((int)file.length());
				responseFile(file, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			//处理登录请求
			System.out.println("处理登录请求");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			//通过JDBC技术查询用户名和密码是否存在
			Connection conn = null;
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				conn = JDBCUtils.getConn();
				String sql = "select * from user where username=? and password=?";
				ps=conn.prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, password);
				rs = ps.executeQuery();
				if(rs.next()){//提示登录成功
					//响应浏览器(提示登录成功)
					File file = new File(ServerContext.WEB_ROOT+"/log_success.html");
					response.setStatus(HttpContext.STATUS_CODE_OK);
					response.setContentType(getContentType(file));
					response.setContentLength((int)file.length());
					responseFile(file, response);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}

	private void responseFile(File file,HttpResponse response) throws IOException{
		/**
		 * 向客户端浏览器发送响应信息
		 * 输出资源文件(即响应实体内容)
		 */
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));//创建指向资源的流，用于读取文件内容
		//将文件一次性读取到字节数组中保存
		byte[] buff = new byte[(int)file.length()];
		bis.read(buff);
		//在调用getOutputStream方法时会先将状态行和响应头发送给客户端
		PrintStream ps = new PrintStream(response.getOutputStream());
		//输出资源文件(即响应实体内容)
		ps.write(buff);
		ps.flush();
	}
	
	//根据文件的后缀名，获取服务器响应数据的格式
	private String getContentType(File file){
		//获取文件的后缀名 index.html
		String fileName= file.getName();	//获取文件名称
		String ext = fileName.substring(fileName.lastIndexOf(".")+1);	//获取文件的后缀名
		//根据后缀名从ServerContext.types中获取对应的数据类型
		return ServerContext.types.get(ext);
	}
}
