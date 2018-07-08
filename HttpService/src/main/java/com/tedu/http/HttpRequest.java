package com.tedu.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 利用面向对象的思想, Http请求和Http响应都可以使用对象进行表示, 因此我们在这里创建一个HttpRequest类,
 * 用于封装Http请求消息.
 * 
 * 1.创建HttpRequest类
 * 2.在HttpRequest类中, 提供私有属性method、uri、protocol, 分别表示请求方式、请求资源的路径、所遵循协议及版本。
 * 并提供getter方法便于外界获取。
 * GET /index.html HTTP/1.1
 * 3.提供构造方法, 对method、uri、protocol进行初始化.
 * 
 * 修改HttpRequest类中的代码, 提供一个getParameter方法用来返回请求中指定名称的参数。
 * 1.在HttpRequest类中创建一个Map<String, String> parameters来保存请求中所有的请求参数,并在构造方法中进行初始化.
 * 2.提供getParameter方法, 根据参数名称获取对应的参数值
 * @author wangchaofan
 *
 */
public class HttpRequest {
	private String method;	//请求方式
	private String uri;	//请求资源的路径
	private String protocol;	//协议及版本
	/**
	 * 用来封装请求中的所有参数信息
	 * http://localhost:8080/Login?username=wer&password=qwe
	 */
	private Map<String, String> parameters;
	public String getMethod() {
		return method;
	}
	public String getUri() {
		return uri;
	}
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * 构造方法,接收客户端的输入流,读取请求信息并保存在类的内部
	 * @param in
	 */
	public HttpRequest(InputStream in){
		try {
			//将字节流包装成字符流,便于读取请求信息
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			/**
			 * 获取请求中信息的第一行
			 * GET /index.html HTTP/1.1
			 */
			String line = br.readLine();
			System.out.println(line);
			//读取的请求信息不能为null或""
			if(line != null && line.length()>0){
				String[] data = line.split("\\s");//以空格切割
				this.method=data[0];
				this.uri=data[1];
				this.protocol=data[2];
				if(uri.equals("/")){
					//设置默认的主页
					uri = "/index.html";
				}
			}
			parameters = new HashMap<>();
			if(uri!=null && uri.contains("?")){
				String[] params = uri.split("\\?")[1].split("\\&");
				for(String param : params){
					//param:username=wer password=qwe
					parameters.put(param.split("=")[0], param.split("=")[1]);
				}
 			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据参数的名称获取对应的参数值
	 * @param name	参数名
	 * @return	String	参数的值
	 */
	public String getParameter(String name){
		//username=wer
		if(parameters!=null){
			return parameters.get(name);
		}
		return null;
	}
}
