package com.lc.platform.xzqh.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lc.platform.xzqh.dao.XzqhDao;
import com.lc.platform.xzqh.domain.Xzqh;

@Service
public class XzqhServiceImpl implements XzqhService {
	@Autowired
	private XzqhDao xzqhDao;
	@Override
	public List<Xzqh> getChildsByPid(String pid) {
		return xzqhDao.findXzqhByParent(pid);
	}
	@Transactional(readOnly=true)
	@Override
	public Set<Xzqh> searchXzqh(String content) {
		List<Xzqh> list = new ArrayList<Xzqh>();
		Set<Xzqh> result = new HashSet<Xzqh>();
		//判断是否是中文检索并且长度大于2
		if(content.matches("^[\\u4e00-\\u9fa5]{2,}$")){
			list = xzqhDao.findByCodeNameContaining(content);
		}else if(content.matches("^[a-zA-Z]{2,}$")){//判断是否是拼音检索并且长度大于2
			list = xzqhDao.findByPinyinContaining(content.toLowerCase());
		}else{
			return result;
		}
		//判断检索的内容是否过多
		//获取最终的结果集进行父级数据的追溯 
		for (Xzqh xzqh : list) {
			String numberCode = xzqh.getNumberCode();
			Assert.notNull(numberCode, "numberCode must not be null");
			String regex = null;
			List<String> values = new ArrayList<String>();
			if(numberCode.length()==6){
				regex = "^(\\d{2})(\\d{2})(\\d{2})$";
			}else if(numberCode.length()==12){
				regex = "^(\\d{2})(\\d{2})(\\d{2})\\d{6}$";
			}else if(numberCode.length()==17){
				regex = "^(\\d{2})(\\d{2})(\\d{2})(\\d{3})\\d{3}xx\\d{3}$";
			}
			if(regex!=null){
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(numberCode);
				if(matcher.find()){
					String first = matcher.group(1)+"0000";
					String second = matcher.group(1) + matcher.group(2)+"00";
					String third = matcher.group(1) + matcher.group(2)+matcher.group(3);
					values.add(first);
					values.add(second);
					values.add(third);
					try {
						String fourth = matcher.group(1) + matcher.group(2)+matcher.group(3)+matcher.group(4)+"000";
						values.add(fourth);
					} catch (Exception e) {
						
					}
				}
				List<Xzqh> parents = xzqhDao.findByNumberCodeIn(values);
				result.addAll(parents);
			}
			String codeName = xzqh.getCodeName();
			xzqh.setCodeName("<span style='color:red;'>"+codeName+"</span>");
			result.add(xzqh);
		}
		return result;
	}
	
}
