package com.lc.platform.commons;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 计算下一个数字,达到最大数字之后，用a-z进行补充</br>
 * 比如000的下一个数字是001,999的下一个数字是000a</br>
 * 999a的下一个数字是000b,001-002的下一个数字是001-003</br>
 * 001a-001a的下一个数字是001a-002a
 */
public class CalNextNum {
	/**
	 * 最小数字
	 */
	private String minNum;
	/**
	 * 最大数字
	 */
	private int maxNum;
	
	DecimalFormat df;
	
	public CalNextNum(){
		this(3);
	}
	
	public CalNextNum(int len){
		minNum = "";
		for (int i = 0; i < len; i++) {
			minNum+="0";
		}
		df = new DecimalFormat(minNum);
		maxNum = Integer.parseInt("1"+minNum)-1;
	}
	
	/**
	 * 返回给定数字的下一个数字
	 * @param number
	 * @return
	 */
	public String nextNum(String number){
		Matcher matcher = Pattern.compile("^(\\d+[a-z]*-*)+$").matcher(number);
		if(matcher.find()){
			String lastNumber = matcher.group(1);
			String firstNum = number.substring(0,number.length()-lastNumber.length());
			Matcher matcher2 = Pattern.compile("^(\\d+)([a-z]*)$").matcher(lastNumber);
			if(matcher2.find()){
				String numStr = matcher2.group(1);
				int num = Integer.parseInt(numStr);
				String letters = matcher2.group(2);
				if(num<maxNum){
					return firstNum+df.format(num+1)+letters;
				}
				if(StringUtils.isBlank(letters)){
					return firstNum+minNum+"a";
				}
				int letterLen = letters.length();
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < letterLen; i++) {
					char letter = letters.charAt(i);
					if(letter!='z'){
						return firstNum+minNum+ letters.substring(0,i)
								+(char)(letter+1)+
								letters.substring(i+1);
					}
					sb.append("a");
				}
				return firstNum+minNum+sb.toString()+"a";
			}
		}
		return null;
	}

}
