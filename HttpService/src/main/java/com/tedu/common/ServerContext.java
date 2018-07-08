package com.tedu.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 创建ServerContext类, 用于封装服务器相关的参数信息
 * 1.创建变量分别表示服务器遵循的协议、监听的端口、线程池大小以及服务器存储资源的目录
 * 2.提供静态代码块, 在程序启动时, 初始化ServerContext类中的成员
 * 3.导入dom4j的开发包, 实现init方法
 * 4.在ServerContext类中, 创建Map集合, 用于存储客户端请求资源的后缀名及对应的响应资源的类型
 * 5.在init方法中对types进行初始化
 * @author wangchaofan
 *
 */
public class ServerContext {
	//协议及版本
	public static String PROTOCOL;
	//监听端口
	public static int PORT;
	//线程池大小
	public static int MAX_THREAD;
	//服务器存储资源的目录
	public static String WEB_ROOT;
	//存储客户端请求资源的后缀名及对应的响应资源类型
	public static Map<String, String> types;
	//表示404页面
	public static String NotFoundPage;
	static{
		init();
	}
	private static void init() {
		/**
		 * 解析config.xml文件，读取其中的配置参数信息
		 * 对ServerContext类中的成员进行初始化
		 */
		try {
			/**
			 * 1.创建解析器
			 * 2.利用解析器解析xml文档,返回dom对象
			 * 3.获取根元素(根节点)
			 * 4.解析connector标签
			 * 设置协议、设置端口、设置线程池大小
			 * 5.解析webroot标签
			 * 6.解析type-mappings
			 * 7.解析not-found-page
			 */
			SAXReader reader = new SAXReader();
			Document dom = reader.read("src/main/resources/config/server.xml");
			System.out.println(dom);
			Element root = dom.getRootElement();
			Element connEle = root.element("service").element("connector");
			PROTOCOL = connEle.attributeValue("protocol");
			PORT = Integer.parseInt(connEle.attributeValue("port"));
			MAX_THREAD = Integer.parseInt(connEle.attributeValue("maxThread"));
			WEB_ROOT = root.element("service").elementText("webroot");
			
			types = new HashMap<>();
			List<Element> mappingEles = root.element("type-mappings").elements();
			for(Element mappingEle : mappingEles){
				types.put(mappingEle.attributeValue("ext"), mappingEle.attributeValue("type"));
			}
			NotFoundPage = root.element("service").elementText("not-found-page");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
