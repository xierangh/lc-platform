package com.lc.platform.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.commons.CalNextNum;
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
			PageRequest pageable = new PageRequest(0, 1);
			Page<Menu> page = menuDao.findByParentIdOrderByCreateDateDesc(menu.getParentId(), pageable);
			CalNextNum calNextNum = new CalNextNum();
			if(page.getTotalElements()>0){
				Menu currMenu = page.getContent().get(0);
				String nextId = calNextNum.nextNum(currMenu.getMenuId());
				menu.setMenuId(nextId);
				menu.setMenuOrder(currMenu.getMenuOrder()+1);
			}else if("0".equals(menu.getParentId())){
				menu.setMenuId("001");
			}else{
				menu.setMenuId(menu.getParentId()+"-001");
			}
			menuDao.saveAndFlush(menu);
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

	@Override
	public void deleteMenu(String menuId) {
		Menu menu = menuDao.findOne(menuId);
		if(menu!=null && menu.getMenuLevel()==2){
			menuDao.delete(menuId);
		}
	}

	@Override
	public void swapMenuOrder(String destId, String srcId) {
		Menu destMenu = menuDao.findOne(destId);
		Menu srcMenu = menuDao.findOne(srcId);
		Integer tempOrder = destMenu.getMenuOrder();
		destMenu.setMenuOrder(srcMenu.getMenuOrder());
		srcMenu.setMenuOrder(tempOrder);
		menuDao.saveAndFlush(destMenu);
		menuDao.saveAndFlush(srcMenu);
	}
}
