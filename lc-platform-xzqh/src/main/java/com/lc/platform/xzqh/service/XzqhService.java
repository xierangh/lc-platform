package com.lc.platform.xzqh.service;

import java.util.List;
import java.util.Set;

import com.lc.platform.xzqh.domain.Xzqh;

public interface XzqhService {

	List<Xzqh> getChildsByPid(String pid);
	
	/**
	 * 根据条件检索数据，返回所有匹配的数据（包括该数据的父级数据）
	 * @param content
	 * @return
	 */
	Set<Xzqh> searchXzqh(String content);

}
