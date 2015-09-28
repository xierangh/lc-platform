package com.lc.platform.commons;


import java.util.UUID;

/**
 * UUID工具类
 * @author 陈均
 *
 */
public class UUIDUtil {

	/**
	 * 随机获取一个uuid数据，该数据不带-
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
}
