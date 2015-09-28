package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.Dept;

public interface DeptDao extends GenericRepository<Dept, String>{

	Page<Dept> findByParentIdOrderByCreateDateDesc(String parentId,
			Pageable pageable);

	Dept findById(String id);

	@Modifying
	@Query("update Dept set parentId=?1 where id in ?2")
	void updateDeptParentId(String parentId,String[] ids);

	@Modifying
	@Query("delete Dept where id like ?1")
	void deleteChildDept(String deptId);

	@Modifying
	@Query("delete Dept where id in ?1")
	void deleteDept(String[] deptIds);
	
	@Query("select deptName from Dept where id in ?1")
	List<String> findDeptNameById(String[] deptIds);
	
	@Query(nativeQuery=true,value="select deptId from (select deptId,count(1) deptIdCount from sys_user_dept where userId in ?1 group by deptId) t where deptIdCount = ?2 ")
	List<String> findAllGrantDept(String[] userIds, int length);

	@Query("select dept.deptName from UserDept where userId in ?1")
	List<String> findDeptNameByUserId(String userId);

}
