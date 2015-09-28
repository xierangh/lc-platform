package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.Role;

/**
 * 角色数据接口
 * @author chenjun
 *
 */
public interface RoleDao extends GenericRepository<Role, String>{

	@Modifying
	@Query("delete Role where id in ?1")
	void batchDelete(String[] ids);

	@Query("select roleName from Role where id in ?1 ")
	List<String> findRoleNameById(String[] roleIds);

	@Query(nativeQuery=true,value="select roleId from (select roleId,count(1) roleIdCount from sys_user_role where userId in ?1 group by roleId) t where roleIdCount = ?2 ")
	List<String> findAllGrantRole(String[] userIds, int length);

	@Query("select role.roleName from UserRole where userId in ?1 ")
	List<String> findRoleNameByUserId(String userId);

}
