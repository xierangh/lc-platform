package com.lc.platform.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.dao.PageBean;
import com.lc.platform.extjs.ExtUtil;
import com.lc.platform.extjs.JsonReader;
import com.lc.platform.extjs.QueryParams;
import com.lc.platform.system.domain.Role;
import com.lc.platform.system.service.RoleService;
import com.lc.platform.system.vo.RoleVO;

@Controller
@RequestMapping("/system/roles")
public class RoleController {
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST})
	public @ResponseBody JsonReader readUsers(QueryParams queryParams,String userIds)throws Exception{
		PageBean pageBean = ExtUtil.getPageBean(queryParams);
		if(StringUtils.isNotBlank(userIds)){
			pageBean.setRowsPerPage(Integer.MAX_VALUE);
		}
		roleService.readRolesByPageInfo(pageBean);
		if(StringUtils.isNotBlank(userIds)){
			List<String> roleGrantList = roleService.findAllGrantRole(userIds.split(","));
			List<RoleVO> list = new ArrayList<RoleVO>();
			for (Object item : pageBean.getItems()) {
				Role role = (Role)item;
				RoleVO roleVO = new RoleVO();
				BeanUtils.copyProperties(roleVO, role);
				roleVO.setGrant(roleGrantList.contains(role.getId()));
				list.add(roleVO);
			}
			pageBean.setItems(list);
		}
		JsonReader jsonReader = ExtUtil.getJsonReader(pageBean);
		return jsonReader;
	}
	
	/**
	 * 更新角色信息
	 * @param role
	 * @return Message
	 * @throws Exception
	 */
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public @ResponseBody
	Message updateRole(@RequestBody Role role)
			throws Exception {
		roleService.updateRole(role);
		return MessageUtil.message(15001);
	}
	
	
	/**
	 * 批量更新角色信息
	 * @param role
	 * @return Message
	 * @throws Exception
	 */
	@RequestMapping(value = "batchUpdate", method = RequestMethod.POST)
	public @ResponseBody
	Message batchUpdate(@RequestBody Role[] roles)
			throws Exception {
		roleService.updateRole(roles);
		return MessageUtil.message(15001);
	}
	
	/**
	 * 批量删除角色信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="batchDelete",method = RequestMethod.POST)
	public @ResponseBody
	Message batchDelete(String[] ids) throws Exception {
		roleService.deleteRole(ids);
		return MessageUtil.message(15002);
	}
	
	/**
	 * 批量角色授权信息
	 * @param rolesId
	 * @param permsId
	 * @param grant
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="perms/update",method = RequestMethod.POST)
	public @ResponseBody
	Message permsUpdate(String[] roleIds,String[] permIds,boolean grant) throws Exception {
		roleService.permsUpdate(roleIds,permIds,grant);
		if(grant){
			return MessageUtil.message(15003);
		}else{
			return MessageUtil.message(15004);
		}
	}
	
	/**
	 * 创建角色信息
	 * @param role
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="create",method = RequestMethod.POST)
	public @ResponseBody Message create(@RequestBody Role role) throws Exception {
		roleService.saveRole(role);
		return MessageUtil.message(15005, role.getId());
	}
	
}
