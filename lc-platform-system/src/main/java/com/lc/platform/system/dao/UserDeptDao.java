package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.domain.UserDept;

/**
 * 用户部门数据接口
 * @author chenjun
 *
 */
public interface UserDeptDao extends GenericRepository<UserDept, String>{

	@Modifying
	@Query("delete UserDept where userId in ?1")
	void deleteByUserId(String[] ids);

	@Modifying
	@Query("delete UserDept where userId = ?1 and deptId = ?2 ")
	void deleteByUserIdAndDeptId(String userId, String deptId);
	
	@Query("select ud from UserDept ud where userId=?1 and deptId=?2")
	UserDept findByUserIdAndDeptId(String userId, String deptId);

	@Query("select ud.user from UserDept ud where deptId=?1")
	List<User> findUserByDept(String deptId);
	
	

}
