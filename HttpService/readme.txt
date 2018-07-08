1.此项目是一个webServer
2.乱码解决：
发送响应头与html设置编码相同，而servlet中设置编码response.setContextType("text/html;charset=UTF-8");
java	:ps.println("Context-Type:"+getContentType()+";charset=UTF-8");//发送响应头
html	:<meta charset="utf-8"/>