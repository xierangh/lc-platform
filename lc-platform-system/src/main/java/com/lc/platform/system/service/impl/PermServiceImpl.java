package com.lc.platform.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.system.dao.PermDao;
import com.lc.platform.system.domain.Perm;
import com.lc.platform.system.service.PermService;

@Transactional
@Service
public class PermServiceImpl implements PermService{
	@Autowired
	private PermDao permDao;
	
	@Override
	public void savePerm(Perm perm) {
		if(perm!=null){
			permDao.save(perm);
		}
	}

	@Override
	public List<Perm> findAllPerm() {
		return permDao.findAll();
	}

	@Override
	public List<String> findAllGrantPerm(String[] roleIds) {
		if(roleIds.length!=0){
			return permDao.findAllGrantPerm(roleIds,roleIds.length);
		}
		return new ArrayList<String>();
	}

}
