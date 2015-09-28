package com.lc.platform.spring;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * Spring工具类
 * @author chenjun
 *
 */
@Component
public class SpringUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringUtil.applicationContext = applicationContext;
	}
	
	/**
	 * 获取request
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest(){
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
		return requestAttributes==null? null : requestAttributes.getRequest();
	}
	
	/**
	 * 获取session
	 * @return HttpSession
	 */
	public static HttpSession getSession(){
		HttpServletRequest request = getRequest();
		if(request!=null){
			return request.getSession(false);
		}
		return null;
	}
	
	/**
	 * 获取指定类型的bean
	 * @param requiredType
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType){
		return applicationContext.getBean(requiredType);
	}
	/**
	 * 获取指定类型的bean
	 * @param requiredType
	 * @return
	 */
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
	/**
	 * 获取web容器的根路径
	 * @return
	 */
	public static String getRealRootPath(){
		return getRequest().getServletContext().getRealPath("/");
	}
	
	/**
	 * 获取请求的ip地址
	 * @return
	 */
	public static String getIp() {
		HttpServletRequest request = getRequest();
		if(request!=null){
			return request.getRemoteAddr();
		}
		return null;
	}
	
	/**
	 * 获取session属性的值
	 * @param name
	 * @return
	 */
	public static Object getSessionAttribute(String name){
		HttpSession session = getSession();
		return session == null?null:session.getAttribute(name);
	}
	
	/**
	 * 设置session属性值
	 * @param name
	 * @param value
	 */
	public static void setSessionAttribute(String name,Object value){
		HttpSession session = getSession();
		if(session!=null){
			session.setAttribute(name, value);	
		}
	}
	/**
	 * 获取request属性的值
	 * @param name
	 * @return
	 */
	public static Object getRequestAttribute(String name){
		HttpServletRequest request = getRequest();
		return request == null?null:request.getAttribute(name);
	}
	
	/**
	 * 设置request属性值
	 * @param name
	 * @param value
	 */
	public static void setRequestAttribute(String name,Object value){
		HttpServletRequest request = getRequest();
		if(request!=null){
			request.setAttribute(name, value);	
		}
	}

	/**
	 * 获取容器上下文路径
	 * @return
	 */
	public static String getContextPath() {
		HttpServletRequest request = getRequest();
		if(request!=null){
			return request.getContextPath();
		}
		return null;
	}

	/**
	 * 移除session属性值
	 * @param name
	 */
	public static void removeSessionAttribute(String name) {
		getRequest().getSession().removeAttribute(name);
	}
	
	/**
	 * 检查浏览器的版本是否满足指定要求
	 * @return
	 */
	public static boolean checkBrowser(){
		String userAgent = SpringUtil.getRequest().getHeader("user-agent");
		return isChrome(userAgent) || isIE(userAgent) || isFirefox(userAgent);
	}
	
	/**
	 * 是否是chrome
	 * @param userAgent
	 * @return
	 */
	public static boolean isChrome(String userAgent){
		return userAgent.contains("Chrome");
	}
	
	
	public static boolean isFirefox(String userAgent){
		return userAgent.contains("Firefox");
	}
	
	/**
	 * 是否满足IE条件(9-11)
	 * @param userAgent
	 * @return
	 */
	public static boolean isIE(String userAgent){
		return userAgent.contains("rv:11.0") ||userAgent.contains("MSIE 10.0") || userAgent.contains("MSIE 9.0");
	}

	/**
	 * 构建服务器传输给客户端的文件信息
	 * @param file 文件信息
	 * @param fileName 文件名为空则显示当前时间
	 * @return ResponseEntity
	 * @throws Exception
	 */
	public static ResponseEntity<byte[]> getResponseEntityByFile(File file,String fileName) throws Exception {
        try(FileInputStream stream = new FileInputStream(file)){
        	byte[] cbyte = IOUtils.toByteArray(stream);
    		HttpHeaders responseHeaders = new HttpHeaders();
        	responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        	responseHeaders.setContentLength(cbyte.length);
        	responseHeaders.set("Content-Disposition", "attachment;filename=\""+URLEncoder.encode(fileName,"UTF-8")+"\"");
    		return new ResponseEntity<byte[]>(cbyte, responseHeaders, HttpStatus.OK);
        }
	}
	/**
	 * 构建服务器传输给客户端的文件信息
	 * @param file 文件信息
	 * @return ResponseEntity
	 * @throws Exception
	 */
	public static ResponseEntity<byte[]> getResponseEntityByFile(File file) throws Exception {
		return getResponseEntityByFile(file, file.getName());
	}

	
}
