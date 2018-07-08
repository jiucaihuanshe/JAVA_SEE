package com.tedu.http;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import com.tedu.common.HttpContext;
import com.tedu.common.ServerContext;

/**
 * 创建一个HttpResponse对象, 用于封装Http响应消息.
 * 
 * 1.创建HttpResponse类, 用于表示Http响应信息
 * 2.提供构造方法, 接收指向客户端的输出流，并保存在类的内部. 用于将响应结果发送给浏览器.
 * 3.提供getOutputStream方法, 用于获取指向客户端的输出流
 * 4.声明status、contentType、contentLength属性, 分别用于表示状态码、响应的数据类型、响应的数据长度。
 * 5.在类中声明一个Map集合,用于存储所有的状态码及对应的描述信息
 * 并提供对应的getter和setter方法
 * 6.创建com.tedu.common.HttpContext类, 用于定义HTTP协议相关的信息
 * 7.在HttpResponse的构造函数中对statusMap进行初始化
 * 8.修改getOutputStream方法, 在返回指向客户端的输出流之前, 先发送状态行和响应头给浏览器
 * @author wangchaofan
 *
 */
public class HttpResponse {
	private OutputStream out;
	private int status;
	private String contentType;
	private int contentLength;
	//用于保存常用的状态码及对应的描述信息
	private Map<Integer, String> statusMap;
	//是否发送状态行、响应头标识,false表示还没有发送过
	private boolean hasPrintHeader = false;
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public HttpResponse(OutputStream out) {
		this.out = out;
		//对statusMap进行初始化操作
		statusMap = new HashMap<>();
		statusMap.put(HttpContext.STATUS_CODE_OK, HttpContext.STATUS_REASON_OK);
		statusMap.put(HttpContext.STATUS_CODE_NOT_FOUND, HttpContext.STATUS_REASON_NOT_FOUND);
		statusMap.put(HttpContext.STATUS_CODE_ERROR, HttpContext.STATUS_REASON_ERROR);
	}
	public OutputStream getOutputStream(){
		/**
		 * 该方法用于将指向客户端的输出流返回,在返回之前自动将状态行、响应头发送给客户端
		 * 需要注意的是，该方法可以被多次调用,而状态行、响应头应该只被发送一次(提供一个是否发送过的表示)
		 */
		if(!hasPrintHeader){
			PrintStream ps = new PrintStream(out);
			ps.println(ServerContext.PROTOCOL+" "+status+" "+statusMap.get(status));//发送状态行
			ps.println("Context-Type:"+getContentType()+";charset=UTF-8");//发送响应头
			ps.println("context-Length:"+getContentLength());
			ps.println("");//发送一个空行,表示响应头发送完毕
			hasPrintHeader = true;//值为true,表示状态码和响应头已经发送过
		}
		return out;
	}
}
