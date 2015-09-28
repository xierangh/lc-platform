package com.lc.platform.commons;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

/**
 * 静态资源构建工具，针对js和css引入的代码片段进行构建
 *
 */
public class StaticResUtil {
	String crlf = System.getProperty("line.separator");
	private String contextPath;
	
	
	public StaticResUtil(String contextPath){
		Assert.notNull(contextPath, "The contextPath must not be null");
		this.contextPath = contextPath;	
	}
	
	/**
	 * 根据提供的css和js文件路径构造完成的html代码片段
	 * @param paths
	 * @return
	 */
	public String buildStatic(String... paths) {
		if (paths != null) {
			StringBuilder sb = new StringBuilder();
			for (String path : paths) {
				if(StringUtils.isNotBlank(path)){
					if(path.endsWith(".css")){
						sb.append("<link rel=\"stylesheet\" href=\"" + contextPath
								+ "/" + path + "\" type=\"text/css\">" + crlf);
					}else if(path.endsWith(".js")){
						sb.append("<script type=\"text/javascript\" src=\""
								+ contextPath + "/" + path + "\"></script>" + crlf);
					}
				}
			}
			return sb.toString();
		}
		return "";
	}
}
