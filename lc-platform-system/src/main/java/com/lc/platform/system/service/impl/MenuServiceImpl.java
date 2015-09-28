package com.lc.platform.system.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.dao.PageBean;
import com.lc.platform.system.dao.MenuDao;
import com.lc.platform.system.domain.Menu;
import com.lc.platform.system.service.MenuService;
@Transactional
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;
	
	@Override 
	public List<Menu> findAllMenu() {
		Sort sort = new Sort(new Order(Direction.ASC, "parentId"),new Order(Direction.ASC, "menuOrder"));
		return menuDao.findAll(sort);
	}

	@Override
	public void saveMenu(Menu menu) {
		if(menu!=null){
			menu.setCreateDate(new Date());
			menuDao.save(menu);
		}
	}

	@Override
	public void readMenusByPageInfo(PageBean pageBean) {
		pageBean.setPropPrefix("p.");
		menuDao.doPager(pageBean, "select p from Menu p where 1=1 ");
	}

	@Override
	public Menu findMenuById(String menuId) {
		return menuDao.findOne(menuId);
	}
	
}
