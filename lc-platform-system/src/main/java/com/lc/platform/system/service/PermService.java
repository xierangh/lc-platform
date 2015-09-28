package com.lc.platform.system.service;

import java.util.List;

import com.lc.platform.system.domain.Perm;

/**
 * 权限信息服务接口
 * @author chenjun
 *
 */
public interface PermService {

	/**
	 * 保存权限代码信息
	 * @param perm
	 */
	void savePerm(Perm perm);

	List<Perm> findAllPerm();

	List<String> findAllGrantPerm(String[] roleIds);

}
