package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.RolePerm;

/**
 * 角色权限数据接口
 * @author chenjun
 *
 */
public interface RolePermDao extends GenericRepository<RolePerm, String>{

	@Modifying
	@Query("delete RolePerm where roleId = ?1 and permId = ?2")
	void delete(String roleId, String permId);
	
	@Query("select rp from RolePerm rp where roleId=?1 and permId=?2")
	RolePerm find(String roleId, String permId);

	@Modifying
	@Query("delete RolePerm where roleId in ?1")
	void deleteByRoleId(String[] ids);

	@Query("select distinct permId from RolePerm where roleId in ?1")
	List<String> findPermByRole(List<String> roleList);

}
