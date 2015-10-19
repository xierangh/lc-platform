package com.lc.platform.system.service.impl;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	ResourcePatternResolver resPatternResolver = new PathMatchingResourcePatternResolver();
	
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
				if(dict.getDefaultVal()){
					dictDao.updateDefaultStatus(dict.getParentId(),dict.getId());
				}
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
		dictDao.delete(id);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void resetDict(String id) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		Resource[] resources = resPatternResolver.getResources("classpath*:data/dicts.json");
		for (int i = 0; i < resources.length; i++) {
			URL url = resources[i].getURL();
			String dicts = IOUtils.toString(url);
			List<Map<String, Object>> list = mapper.readValue(dicts, List.class);
			Date createDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(createDate);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> item = list.get(j);
				String letterCode = item.get("letterCode").toString();
				if(letterCode.equals(id)){
					dictDao.deleteChildDict(id + "-%");
					dictDao.delete(id);
					Dict dict = new Dict();
					String codeName = item.get("codeName").toString();
					String dictDesc = item.get("dictDesc").toString();
					item.put("id", letterCode);
					dict.setId(letterCode);
					dict.setLetterCode(letterCode);
					dict.setCodeName(codeName);
					dict.setCodeType(1);
					dict.setCreateDate(calendar.getTime());
					calendar.add(Calendar.SECOND, 1);
					dict.setDefaultVal(false);
					dict.setDictOrder(j);
					dict.setDictDesc(dictDesc);
					dict.setNumberCode(letterCode);
					dict.setParentId("0");
					buildChildDict(item);
					Boolean leaf = (Boolean) item.get("leaf");
					dict.setLeaf(leaf);
					dictDao.save(dict);
					return;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void buildChildDict(Map<String, Object> parent){
		Object children = parent.get("children");
		if(children instanceof List){
			parent.put("leaf", false);
			List<Map<String, Object>> childrenList = (List<Map<String, Object>>)children;
			Date createDate = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(createDate);
			for (int i = 0; i < childrenList.size(); i++) {
				Map<String, Object> item = childrenList.get(i);
				String codeName = item.get("codeName").toString();
				String numberCode = item.get("numberCode").toString();
				String parentId = parent.get("id").toString();
				String id = parentId + "-" + numberCode;
				item.put("id", id);
				Dict dict = new Dict();
				dict.setId(id);
				dict.setCodeName(codeName);
				dict.setCodeType(1);
				dict.setCreateDate(calendar.getTime());
				calendar.add(Calendar.SECOND, 1);
				dict.setDefaultVal(false);
				dict.setDictOrder(i);
				dict.setDictDesc(codeName);
				dict.setNumberCode(numberCode);
				dict.setParentId(parentId);
				buildChildDict(item);
				Boolean leaf = (Boolean) item.get("leaf");
				dict.setLeaf(leaf);
				dictDao.save(dict);
			}
		}else{
			parent.put("leaf", true);
		}
	}


	@Override
	public List<Dict> findAllDictByParentId(String dictId) {
		return dictDao.findAllDictByParentId(dictId + "-%");
	}
}
