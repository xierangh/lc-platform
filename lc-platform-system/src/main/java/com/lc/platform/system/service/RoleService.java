package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.dao.PageBean;
import com.lc.platform.system.domain.Role;

/**
 * 角色服务接口
 * @author chenjun
 *
 */
public interface RoleService {

	void readRolesByPageInfo(PageBean pageBean);

	void updateRole(Role... roles);

	void deleteRole(String[] ids);
	
	/**
	 * 批量更新角色权限信息
	 * @param roleIds 角色集合
	 * @param permIds 权限集合
	 * @param grant true授予，false撤销
	 */
	void permsUpdate(String[] roleIds, String[] permIds, boolean grant);

	/**
	 * 获取指定用户集合的共有角色信息
	 * @param userIds
	 * @return
	 */
	List<String> findAllGrantRole(String[] userIds);
	/**
	 * 创建角色基本信息
	 * @param role
	 */
	void saveRole(Role role);

}
