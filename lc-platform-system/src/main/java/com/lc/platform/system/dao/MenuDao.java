package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

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

	@Query("select m from Menu m where m.menuId=?1 and m.desktopNumber=?2 and (m.userId IS NULL or m.userId=?3) order by menuOrder")
	List<Menu> findSystemMenu(String menuId, int desktopNumber,String userId);
	@Query("select m from Menu m where m.menuType!='dir' and (m.userId IS NULL or m.userId=?1) order by menuOrder")
	List<Menu> findAllMenu(String userId);

}
