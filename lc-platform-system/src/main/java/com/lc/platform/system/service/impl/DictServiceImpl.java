package com.lc.platform.system.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.commons.CalNextNum;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.system.dao.DictDao;
import com.lc.platform.system.domain.Dict;
import com.lc.platform.system.exception.DictException;
import com.lc.platform.system.service.DictService;

@Transactional
@Service
public class DictServiceImpl implements DictService{
	@Autowired
	private DictDao dictDao;
	
	
	@Override
	public List<Dict> findDictByParentId(String parentId) {
		return dictDao.findDictByParentId(parentId);
	}


	@Override
	public void saveDict(Dict dict) {
		if(dict!=null){
			if(StringUtils.isBlank(dict.getId())){
				PageRequest pageable = new PageRequest(0, 1);
				Page<Dict> page = dictDao.findByParentIdOrderByCreateDateDesc(dict.getParentId(), pageable);
				CalNextNum calNextNum = new CalNextNum();
				if(page.getTotalElements()>0){
					Dict currDict = page.getContent().get(0);
					String nextId = calNextNum.nextNum(currDict.getId());
					dict.setId(nextId);
				}else{
					dict.setId(dict.getParentId()+"-001");
				}
				dict.setCodeType(2);
				dict.setLeaf(true);
				dict.setCreateDate(new Date());
				Dict parent = dictDao.findOne(dict.getParentId());
				if(parent.getLeaf()){
					parent.setLeaf(false);
					dictDao.saveAndFlush(parent);
				}
				dictDao.saveAndFlush(dict);
			}else{
				Dict oldDict = dictDao.findOne(dict.getId());
				oldDict.setCodeName(dict.getCodeName());
				oldDict.setDictDesc(dict.getDictDesc());
				oldDict.setDefaultVal(dict.getDefaultVal());
				oldDict.setDictOrder(dict.getDictOrder());
				oldDict.setNumberCode(dict.getNumberCode());
				dictDao.saveAndFlush(oldDict);
			}
		}
	}


	@Override
	public void deleteDict(String id) {
		Dict dict = dictDao.findOne(id);
		if(dict.getCodeType()==1){
			throw new DictException(MessageUtil.getMessage("13003"));
		}
		dictDao.deleteChildDict(id + "-%");
		dictDao.delete(dict);
	}
	
}
