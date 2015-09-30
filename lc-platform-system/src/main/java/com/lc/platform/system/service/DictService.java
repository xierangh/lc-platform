package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.system.domain.Dict;

public interface DictService {

	List<Dict> findDictByParentId(String parentId);

	void saveDict(Dict dict);

	void deleteDict(String id);

}
