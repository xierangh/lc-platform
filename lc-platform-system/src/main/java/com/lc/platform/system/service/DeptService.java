package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.system.domain.Dept;

/**
 * 部门业务接口
 * @author chenjun
 *
 */
public interface DeptService {

	List<Dept> getAllDept();

	void saveDept(Dept dept);

	void updateDept(Dept dept);

	void moveDept(String deptIds, String targetDeptId);

	public void deleteDept(String deptIds);

	List<String> findAllGrantDept(String[] userIds);

}
