package com.lc.platform.commons;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

import org.apache.commons.lang.StringUtils;

/**
 * @className:PinyingUtil.java
 * @classDescription:拼音操作工具类
 */
public class PinYinUtil {
	private static final String DEFAULT_SEPARATOR = "";
	private static final String SPACE_SEPARATOR = " ";
	static HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();

	static{
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	}
	
	public PinYinUtil() {
		
	}
	
	/**
	 * 根据汉字返回全拼和简拼的集合，默认用空格分隔
	 * @param hanyu
	 * @return
	 * @throws Exception
	 */
	public static Set<String> hanyuToPyAndJp(String hanyu)throws Exception{
		return hanyuToPyAndJp(hanyu, DEFAULT_SEPARATOR);
	}

	/**
	 * 根据汉字返回全拼和简拼的集合，按指定分隔符分隔
	 * @param hanyu
	 * @param separator
	 * @return
	 * @throws Exception
	 */
	public static Set<String> hanyuToPyAndJp(String hanyu, String separator)
			throws Exception {
		Set<String> resultSet = hanyuToPy(hanyu, separator);
		separator = " ";
		Set<String> resultSet2 = hanyuToPy(hanyu, " ");
		for (String pinyin : resultSet2) {
			String[] items = pinyin.split(separator);
			StringBuffer sb = new StringBuffer();
			for (String item : items) {
				sb.append(item.substring(0, 1));
			}
			resultSet.add(sb.toString());
		}
		return resultSet;
	}

	/**
	 * 根据汉字返回简拼的集合
	 * @param hanyu
	 * @return
	 * @throws Exception
	 */
	public static Set<String> hanyuToJp(String hanyu) throws Exception {
		String separator = " ";
		Set<String> resultSet = hanyuToPy(hanyu,separator);
		Set<String> jpSet = new HashSet<String>();
		for (String pinyin : resultSet) {
			String[] items = pinyin.split(separator);
			StringBuffer sb = new StringBuffer();
			for (String item : items) {
				sb.append(item.substring(0, 1));
			}
			jpSet.add(sb.toString());
		}
		return jpSet;
	}
	
	/**
	 * 根据汉字返回简拼的集合字符串，默认空格关联比如: ab bd 
	 * @param hanyu
	 * @return
	 * @throws Exception
	 */
	public static String hanyuToJpStr(String hanyu) throws Exception {
		return hanyuToJpStr(hanyu, " ");
	}
	
	/**
	 * 根据汉字返回简拼的集合字符串，按照指定关联符进行关联比如ab,bd
	 * @param hanyu
	 * @param join
	 * @return
	 * @throws Exception
	 */
	public static String hanyuToJpStr(String hanyu,String join) throws Exception {
		String separator = " ";
		Set<String> resultSet = hanyuToPy(hanyu,separator);
		StringBuffer result = new StringBuffer(join);
		Set<String> jpSet = new HashSet<String>();
		for (String pinyin : resultSet) {
			String[] items = pinyin.split(separator);
			StringBuffer sb = new StringBuffer();
			for (String item : items) {
				if(StringUtils.isNotEmpty(item))
				sb.append(item.substring(0, 1));
			}
			jpSet.add(sb.toString());
		}
		for (String jp : jpSet) {
			result.append(jp).append(join);
		}
		return result.toString();
	}
	
	
	/**
	 * 根据汉字返回拼音的集合
	 * @param hanyu
	 * @return
	 * @throws Exception
	 */
	public static Set<String> hanyuToPy(String hanyu)
			throws Exception {
		return hanyuToPy(hanyu, DEFAULT_SEPARATOR);
	}
	
	/**
	 * 根据汉字返回拼音的集合字符串,默认空格关联比如 ab bd
	 * @param hanyu
	 * @return
	 * @throws Exception
	 */
	public static String hanyuToPyStr(String hanyu) throws Exception{
		return hanyuToPyStr(hanyu, DEFAULT_SEPARATOR, SPACE_SEPARATOR);
	}
	
	
	public static String hanyuToPyStr(String hanyu,String separator,String dySeparator) throws Exception{
		Set<String> set = hanyuToPy(hanyu, separator);
		StringBuffer sb = new StringBuffer();
		for (String dy : set) {
			sb.append(dy).append(dySeparator);
		}
		if(set.size()>0){
			int index = sb.lastIndexOf(dySeparator);
			sb.replace(index, sb.length(), "");
		}
		return sb.toString();
	}
	
	public static Set<String> hanyuToPy(String hanyu, String separator)
			throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		Set<String> resultSet = new HashSet<String>();
		if(StringUtils.isEmpty(hanyu))return resultSet;
		for (int i = 0; i < hanyu.length(); i++) {
			String[] pinyinArray = null;
			char chineseChar = hanyu.charAt(i);
			if (isHanzi(chineseChar)) {
				pinyinArray = PinyinHelper.toHanyuPinyinStringArray(
						chineseChar, outputFormat);
			} else {
				pinyinArray = new String[] { chineseChar + DEFAULT_SEPARATOR };
			}
			if(pinyinArray!=null){
				list.add(pinyinArray);
			}
		}
		
		buildItem(list, 0, DEFAULT_SEPARATOR, separator, resultSet);
		return resultSet;
	}

	protected static boolean isHanzi(char oneChar) {
		if ((oneChar >= '\u4e00' && oneChar <= '\u9fa5')
				|| (oneChar >= '\uf900' && oneChar <= '\ufa2d')) {
			return true;
		}
		return false;
	}

	private static void buildItem(List<String[]> list, int index, String sb,
			String separator, Set<String> resultSet) {
		if (index == list.size()) {
			if (StringUtils.isNotEmpty(sb)) {
				if (StringUtils.isNotEmpty(separator)) {
					resultSet.add(sb.substring(1));
				} else {
					resultSet.add(sb);
				}
			}
			return;
		}
		String[] items = list.get(index);
		for (String item : items) {
			buildItem(list, index + 1, sb + separator + item, separator,
					resultSet);
		}
	}


}