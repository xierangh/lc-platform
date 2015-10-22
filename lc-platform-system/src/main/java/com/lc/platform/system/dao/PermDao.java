package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.system.domain.Perm;
/**
 * 权限数据接口
 * @author chenjun
 *
 */
public interface PermDao extends JpaRepository<Perm, String>{

	
	@Query(nativeQuery=true,value="select permId from (select permId,count(1) permIdCount from sys_role_perm where roleId in ?1 group by permId) t where permIdCount = ?2 ")
	List<String> findAllGrantPerm(String[] roleIds,int count);

	@Query("select id from Perm")
	List<String> findAllGrantPerm();

	@Query("select p from Perm p where menuId=?1")
	List<Perm> findAllPermByMenuId(String menuId);

}
