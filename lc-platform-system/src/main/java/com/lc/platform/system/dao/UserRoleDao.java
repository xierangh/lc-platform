package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.domain.UserRole;

/**
 * 用户角色数据接口
 * @author chenjun
 *
 */
public interface UserRoleDao extends GenericRepository<UserRole, String>{

	@Modifying
	@Query("delete UserRole where userId in ?1")
	void deleteByUserId(String[] ids);

	@Modifying
	@Query("delete UserRole where userId = ?1 and roleId = ?2 ")
	void deleteByUserIdAndRoleId(String userId, String roleId);

	@Query("select ur from UserRole ur where userId=?1 and roleId=?2")
	UserRole findByUserIdAndRoleId(String userId, String roleId);
	
	@Query("select ur.role.id from UserRole ur where userId=?1")
	List<String> findRoleByUser(String userId);

	@Query("select ur.user from UserRole ur where roleId=?1")
	List<User> findUserByRole(String roleId);

}
