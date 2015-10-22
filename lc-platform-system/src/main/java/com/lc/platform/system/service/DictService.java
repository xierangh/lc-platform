package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.system.domain.Dict;

public interface DictService {

	List<Dict> findDictByParentId(String parentId);

	/**
	 * 保存用户数据字典
	 * @param dict
	 */
	void saveDict(Dict dict);

	/**
	 * 删除用户的数据字典
	 * @param id
	 */
	void deleteDict(String id);
	/**
	 * 重置指定的系统数据字典信息
	 * @param id
	 */
	void resetDict(String id)throws Exception;
	
	/**
	 * 重置所有的数据字典
	 * @throws Exception
	 */
	void resetAllDict()throws Exception;

	List<Dict> findAllDictByParentId(String dictId);

}
