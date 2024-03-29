package com.lc.platform.system.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.system.domain.Dict;
import com.lc.platform.system.service.DictService;
import com.lc.platform.system.vo.DictNode;

@Controller
@RequestMapping("/system/dicts")
public class DictController {
	
	@Autowired
	private DictService dictService;
	
	/**
	 * 根据给定的数据字典ID获取该字典下面的所有数据项数据
	 * @param dictId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="data",method={RequestMethod.GET})
	public @ResponseBody List<Dict> data(String dictId)throws Exception{
		List<Dict> list = dictService.findAllDictByParentId(dictId);
		return list;
	}
	
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody List<DictNode> readDicts(String parentId) throws Exception{
		List<Dict> list = dictService.findDictByParentId(parentId);
		List<DictNode> childrenList = new ArrayList<DictNode>();
		for (Dict dict : list) {
			DictNode node = new DictNode();
			BeanUtils.copyProperties(node, dict);
			if(dict.getLeaf()){
				if(dict.getCodeType()==1){
					node.setIcon("system/images/system-leaf.gif");
				}else{
					node.setIcon("system/images/user-leaf.gif");
				}
			}
			childrenList.add(node);
		}
		return childrenList;
	}
	
	
	@RequestMapping(value="create",method={RequestMethod.POST})
	public @ResponseBody Message create(Dict dict){
		dictService.saveDict(dict);
		return MessageUtil.message(13001, dict.getId());
	}
	
	@RequestMapping(value="delete",method={RequestMethod.POST})
	public @ResponseBody Message delete(String id){
		dictService.deleteDict(id);
		return MessageUtil.message(13002);
	}
	
	
	@RequestMapping(value="reset",method={RequestMethod.POST})
	public @ResponseBody Message reset(String id) throws Exception{
		dictService.resetDict(id);
		return MessageUtil.message(13004);
	}
	
	/**
	 * 重置字典数据
	 * @return
	 * @throws Exception
	 */
	@RolesAllowed("ROLE_SUPERADMIN")
	@RequestMapping(value="resetAll",method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody Message resetAll()throws Exception{
		dictService.resetAllDict();
		return MessageUtil.message(13005);
	}
}
