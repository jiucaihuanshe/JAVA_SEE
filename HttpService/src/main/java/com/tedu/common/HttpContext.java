package com.tedu.common;
/**
 * 用于定义HTTP协议相关的信息
 * @author wangchaofan
 */
public class HttpContext {
	//状态码:服务器成功的处理了请求
	public static final int STATUS_CODE_OK=200;
	//状态描述信息：服务器成功的处理了请求
	public static final String STATUS_REASON_OK="OK";
	//状态码:客户端请求的资源不存在
	public static final int STATUS_CODE_NOT_FOUND=404;
	//状态描述信息：客户端请求的资源不存在
	public static final String STATUS_REASON_NOT_FOUND="Not Found";
	//状态码:服务器内部错误
	public static final int STATUS_CODE_ERROR=500;
	//状态描述信息：服务器内部错误
	public static final String STATUS_REASON_ERROR="Internal Server Error";
}
