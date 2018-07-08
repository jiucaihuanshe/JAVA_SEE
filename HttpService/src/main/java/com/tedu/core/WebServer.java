package com.tedu.core;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.tedu.common.ServerContext;
/**
 * 1.创建com.tedu.core.WebServer类
 * 2.在WebServer类中创建成员变量 ServerSocket.
 * 3.提供构造方法, 用于对服务端程序进行初始化
 * 4.提供start方法, 用于接收客户端的请求, 并处理之.
 * 5.提供main函数, 对WebServer实例化, 调用start方法启动服务器开始接收客户端的请求并处理.
 * @author wangchaofan
 *
 */
public class WebServer {
	//创建ServerSocket,用于接收客户端的连接
	private ServerSocket server;
	//创建ExecutorService实例,管理用于处理客户端请求的线程
	private ExecutorService threadPool;
	public WebServer() {
		try {
			/** 对ServerSocket进行初始化,监听8080端口 */
			server = new ServerSocket(ServerContext.PORT);
			//创建线程池
			threadPool = Executors.newFixedThreadPool(ServerContext.MAX_THREAD);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("服务端启动失败...");
		}
	}
	public void start(){
		try {
			while(true){
				System.out.println("等待客户端连接...");
				Socket socket = server.accept();
				//启用线程处理客户端的请求
				threadPool.execute(new ClientHandler(socket));
				
				
				
				//向客户端做出响应
//				OutputStream out = socket.getOutputStream();
//				out.write("hello 2018...".getBytes());
//				out.flush();
//				PrintStream out = new PrintStream(socket.getOutputStream());
//				out.println("HTTP/1.1 200 OK");//发送状态行
//				out.println("Context-Type:text/html");//发送响应头
//				out.println("content-Length:"+"hello 2018...".length());//发送状态行
//				out.println();//发送一个空行
//				out.print("hello 2019...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WebServer webServer = new WebServer();
		webServer.start();
	}
	
}
