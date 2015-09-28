package com.lc.platform.dao.jpa;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.lc.platform.dao.PageBean;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends
		JpaRepository<T, ID> {
	
	/**
	 * 结合提供的分页信息，获取指定条件下的数据对象
	 * @param pageBean 分页信息
	 * @param qlString 基于jpa标准的ql语句
	 */
	public void doPager(PageBean pageBean, String qlString);
}
