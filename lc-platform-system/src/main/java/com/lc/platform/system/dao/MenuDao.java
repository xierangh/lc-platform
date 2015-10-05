package com.lc.platform.system.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.Menu;

/**
 * 菜单数据接口
 * @author chenjun
 *
 */
public interface MenuDao extends GenericRepository<Menu, String>{

	Page<Menu> findByParentIdOrderByCreateDateDesc(String parentId,
			Pageable pageable);

}
