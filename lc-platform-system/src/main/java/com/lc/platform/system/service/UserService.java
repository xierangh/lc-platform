package com.lc.platform.system.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.lc.platform.dao.PageBean;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.exception.EmailUniqueException;
import com.lc.platform.system.exception.UserNameUniqueException;


/**
 * 用户业务接口信息
 */
public interface UserService extends UserDetailsService{

	/**
	 * 读取用户列表信息
	 * @param pageBean
	 */
	public void readUsersByPageInfo(PageBean pageBean);

	/**
	 * 保存用户基本信息
	 * @param user
	 */
	public void saveUser(User user);

	/**
	 * 检查邮箱是否可用
	 * @param email 邮箱
	 * @throws EmailUniqueException 邮箱已经存在，抛出该异常
	 */
	public void checkEmailUnique(String email)throws EmailUniqueException;
	/**
	 * 检查用户名是否可用
	 * @param username 用户名
	 * @throws UserNameUniqueException 用户名已经存在，抛出该异常
	 */
	public void checkUserNameUnique(String username)throws UserNameUniqueException;

	/**
	 * 删除用户信息，伪删除，可通过超级管理员恢复
	 * @param ids 用户id集合
	 */
	public void deleteUser(String[] ids);

	/**
	 * 保存用户基本信息,角色信息，部门信息
	 * @param user 用户对象
	 * @param roleIds 角色集合
	 * @param deptIds 部门集合
	 */
	public void saveUser(User user, String[] roleIds, String[] deptIds);

	/**
	 * 更新用户基本信息
	 * @param user 用户对象
	 */
	public void updateUser(User user);
	
	/**
	 * 更新用户角色信息
	 * @param userIds 用户集合
	 * @param roleId 角色信息
	 * @param grant true 授权，false 撤销
	 */
	public void updateUserRole(String[] userIds, String[] roleIds,boolean grant);

	/**
	 * 更新用户部门信息
	 * @param userIds 用户集合
	 * @param deptId 部门信息
	 * @param grant true 授权，false 撤销
	 */
	public void updateUserDept(String[] userIds, String deptId, boolean grant);

	/**
	 * 更新用户的头像信息
	 * @param userId 用户id
	 * @param headImageBase64 头像图片信息
	 */
	public void updateUserHeadImage(String userId, String headImageBase64);

}
