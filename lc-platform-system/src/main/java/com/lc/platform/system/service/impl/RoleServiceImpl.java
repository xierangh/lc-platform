package com.lc.platform.system.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.commons.UUIDUtil;
import com.lc.platform.dao.PageBean;
import com.lc.platform.system.dao.RoleDao;
import com.lc.platform.system.dao.RolePermDao;
import com.lc.platform.system.dao.UserDao;
import com.lc.platform.system.dao.UserRoleDao;
import com.lc.platform.system.domain.Role;
import com.lc.platform.system.domain.RolePerm;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.service.RoleService;


@Transactional
@Service
public class RoleServiceImpl implements RoleService{

	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RolePermDao rolePermDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private UserDao userDao;
	
	@Override
	public void readRolesByPageInfo(PageBean pageBean) {
		roleDao.doPager(pageBean, "from Role where 1=1 ");
	}

	@Override
	public void updateRole(Role... roles) {
		for (Role role : roles) {
			Role oldRole = roleDao.findOne(role.getId());
			if(oldRole!=null){
				String roleName = role.getRoleName();
				String oldRoleName = oldRole.getRoleName();
				if(role.getRoleDesc()!=null){
					oldRole.setRoleDesc(role.getRoleDesc());
				}
				roleDao.saveAndFlush(oldRole);
				if(roleName!=null && !roleName.equals(oldRoleName)){
					oldRole.setRoleName(role.getRoleName());
					List<User> users = userRoleDao.findUserByRole(oldRole.getId());
					for (User user : users) {
						List<String> roleInfoList = roleDao.findRoleNameByUserId(user.getUserId());
						String roleInfo = StringUtils.join(roleInfoList,",");
						user.setRoleInfo(roleInfo);
						userDao.saveAndFlush(user);
					}
				}
			}
		}
	}

	@Override
	public void deleteRole(String[] ids) {
		rolePermDao.deleteByRoleId(ids);
		roleDao.batchDelete(ids);
	}

	@Override
	public void permsUpdate(String[] roleIds, String[] permIds, boolean grant) {
		for (String roleId : roleIds) {
			if(StringUtils.isNotBlank(roleId)){
				for (String permId : permIds) {
					if(StringUtils.isNotBlank(permId)){
						RolePerm rolePerm = new RolePerm(roleId,permId);
						if(grant){
							RolePerm oldRolePerm = rolePermDao.find(roleId,permId);
							if(oldRolePerm==null){
								rolePerm.setId(UUIDUtil.uuid());
								rolePermDao.save(rolePerm);
							}
						}else{
							rolePermDao.delete(roleId,permId);
						}
					}
				}
			}
		}
	}

	@Override
	public List<String> findAllGrantRole(String[] userIds) {
		return roleDao.findAllGrantRole(userIds,userIds.length);
	}

	@Override
	public void saveRole(Role role) {
		role.setId(UUIDUtil.uuid());
		roleDao.save(role);
	}
}
