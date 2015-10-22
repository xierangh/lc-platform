package com.lc.platform.system.service.impl;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.commons.CalNextNum;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.dao.PageBean;
import com.lc.platform.system.dao.MenuDao;
import com.lc.platform.system.dao.PermDao;
import com.lc.platform.system.domain.Menu;
import com.lc.platform.system.domain.Perm;
import com.lc.platform.system.service.MenuService;
@Transactional
@Service
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private PermDao permDao;
	protected Log logger = LogFactory.getLog(getClass());
	ResourcePatternResolver resPatternResolver = new PathMatchingResourcePatternResolver();
	ObjectMapper mapper = new ObjectMapper();
	
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

	@Override
	public void resetAllMenu() throws Exception {
		logger.info(MessageUtil.getMessage(11006));
		permDao.deleteAll();
		menuDao.deleteAll();
		initMenuData();
	}

	
	@SuppressWarnings("unchecked")
	protected void initMenuData() throws Exception{
		Resource[] resources = resPatternResolver.getResources("classpath*:data/menus.json");
		Date createDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createDate);
		for (int i = 0; i < resources.length; i++) {
			URL url = resources[i].getURL();
			String menus = IOUtils.toString(url);
			List<Map<String, Object>> list = mapper.readValue(menus, List.class);
			for (int j = 0; j < list.size(); j++) {
				Map<String, Object> item = list.get(j);
				Menu menu = new Menu();
				menu.setMenuLevel(1);
				menu.setParentId("0");
				menu.setCreateDate(calendar.getTime());
				calendar.add(Calendar.SECOND, 1);
				BeanUtils.populate(menu, item);
				saveMenu(menu);
				item.put("menuId", menu.getMenuId());
				buildChildMenu(item);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void buildChildMenu(Map<String, Object> parent) throws Exception {
		String menuId = parent.get("menuId")+"";
		Object children = parent.get("children");
		Object perms = parent.get("perms");
		if(perms instanceof List){
			List<Map<String, Object>> permsList = (List<Map<String, Object>>)perms;
			for (int i = 0; i < permsList.size(); i++) {
				Map<String, Object> item = permsList.get(i);
				Perm perm = new Perm();
				perm.setId(item.get("permCode").toString());
				perm.setMenuId(menuId);
				perm.setPermName(item.get("permName").toString());
				if(item.get("permDesc")!=null){
					perm.setPermDesc(item.get("permDesc").toString());
				}else{
					perm.setPermDesc(perm.getPermName());
				}
				permDao.save(perm);
			}
		}
		
		if(children instanceof List){
			List<Map<String, Object>> childrenList = (List<Map<String, Object>>)children;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			for (int i = 0; i < childrenList.size(); i++) {
				Map<String, Object> item = childrenList.get(i);
				Menu menu = new Menu();
				menu.setMenuLevel(1);
				menu.setCreateDate(calendar.getTime());
				calendar.add(Calendar.SECOND, 1);
				BeanUtils.populate(menu, item);
				menu.setParentId(menuId);
				saveMenu(menu);
				item.put("menuId", menu.getMenuId());
				buildChildMenu(item);
			}
		}
	}
}
