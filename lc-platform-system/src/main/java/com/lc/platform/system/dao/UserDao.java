package com.lc.platform.system.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.User;

/**
 * 用户数据接口
 * @author chenjun
 *
 */
public interface UserDao extends GenericRepository<User, String>{

	/**
	 * 根据用户名字查询用户信息
	 * @param username
	 * @return
	 */
	User findUserByUsername(String username);

	/**
	 * 根据用户的邮箱查询用户信息
	 * @param email
	 * @return
	 */
	User findUserByEmail(String email);

	@Modifying
	@Query("update User set delStatus=?1 where id in ?2")
	void batchDelete(boolean delStatus,String[] ids);

	@Modifying
	@Query("update User set headImage24 =?2 where id=?1")
	void updateUserHeadImage(String userId, String headImageBase64);
	
}
