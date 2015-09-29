package com.lc.platform.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.platform.system.dao.DictDao;
import com.lc.platform.system.domain.Dict;
import com.lc.platform.system.service.DictService;

@Service
public class DictServiceImpl implements DictService{
	@Autowired
	private DictDao dictDao;
	
	
	@Override
	public List<Dict> findDictByParentId(String parentId) {
		return dictDao.findDictByParentId(parentId);
	}

	
	
}
