package com.lc.platform.system.service;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.system.dao.DictDao;
import com.lc.platform.system.dao.MenuDao;
import com.lc.platform.system.dao.PermDao;
import com.lc.platform.system.dao.UserDao;
import com.lc.platform.system.domain.Dict;
import com.lc.platform.system.domain.Menu;
import com.lc.platform.system.domain.Perm;
import com.lc.platform.system.domain.User;

/**
 * 系统服务类
 * 
 * @author chenjun
 *
 */
@Service
public class SystemService implements InitializingBean {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private UserService userService;
	@Autowired 
	private MenuService menuService;
	@Autowired
	private PermService permService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private PermDao permDao;
	@Autowired
	private DictDao dictDao;
	
	ObjectMapper mapper = new ObjectMapper();

	ResourcePatternResolver resPatternResolver = new PathMatchingResourcePatternResolver();
	 
	@Override
	public void afterPropertiesSet() throws Exception {
		initUserData();
		initMenuData();
		initPermData();
		initDictData();
	}

	/**
	 * 初始化数据词典信息，检查系统是否有数据词典信息，没有就进行初始化
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	protected void initDictData()throws IOException{
		long dictCount = dictDao.count();
		if(dictCount==0){
			logger.info("------------------system init dict data------------");
			Resource[] resources = resPatternResolver.getResources("classpath*:data/dicts.json");
			for (int i = 0; i < resources.length; i++) {
				URL url = resources[i].getURL();
				String dicts = IOUtils.toString(url);
				List<Map<String, Object>> list = mapper.readValue(dicts, List.class);
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> item = list.get(j);
					Dict dict = new Dict();
					String letterCode = item.get("letterCode").toString();
					String codeName = item.get("codeName").toString();
					String dictDesc = item.get("dictDesc").toString();
					item.put("id", letterCode);
					dict.setId(letterCode);
					dict.setLetterCode(letterCode);
					dict.setCodeName(codeName);
					dict.setCodeType(1);
					dict.setCreateDate(new Date());
					dict.setDefaultVal(false);
					dict.setDictOrder(j);
					dict.setDictDesc(dictDesc);
					dict.setNumberCode(letterCode);
					dict.setParentId("0");
					buildChildDict(item);
					dictDao.save(dict);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void buildChildDict(Map<String, Object> parent){
		Object children = parent.get("children");
		if(children instanceof List){
			List<Map<String, Object>> childrenList = (List<Map<String, Object>>)children;
			for (int i = 0; i < childrenList.size(); i++) {
				Map<String, Object> item = childrenList.get(i);
				String codeName = item.get("codeName").toString();
				String numberCode = item.get("numberCode").toString();
				String parentId = parent.get("id").toString();
				String id = parentId + "-" + numberCode;
				item.put("id", id);
				Dict dict = new Dict();
				dict.setId(id);
				dict.setCodeName(codeName);
				dict.setCodeType(1);
				dict.setCreateDate(new Date());
				dict.setDefaultVal(false);
				dict.setDictOrder(i);
				dict.setDictDesc(codeName);
				dict.setNumberCode(numberCode);
				dict.setParentId(parentId);
				buildChildDict(item);
				dictDao.save(dict);
			}
			
			
		}
	}
	
	/**
	 * 初始化权限信息，检查系统是否有权限信息，没有就进行初始化
	 * @throws IOException 
	 */
	protected void initPermData() throws IOException{
		long permCount = permDao.count();
		if(permCount==0){
			logger.info("------------------system init perm data------------");
			Resource[] resources = resPatternResolver.getResources("classpath*:data/perms.json");
			for (int i = 0; i < resources.length; i++) {
				URL url = resources[i].getURL();
				String perms = IOUtils.toString(url);
				Perm[]list = mapper.readValue(perms, Perm[].class);
				for (Perm perm : list) {
					permService.savePerm(perm);
				}
			}
		}
	}
	
	/**
	 * 初始化用户信息,检查系统是否有用户信息，没有就进行初始化
	 * 
	 * @throws IOException
	 */
	protected void initUserData() throws IOException {
		long userCount = userDao.count();
		if (userCount == 0) {
			logger.info("------------------system init user data------------");
			URL url = ClassLoader.getSystemResource("data/users.json");
			String users = IOUtils.toString(url);
			User[] list = mapper.readValue(users, User[].class);
			for (User user : list) {
				userService.saveUser(user);
			}
		}
	}

	/**
	 * 初始化菜单信息，检查系统是否有菜单信息，没有就进行初始化
	 * @throws IOException 
	 */
	protected void initMenuData() throws IOException{
		long menuCount = menuDao.count();
		if(menuCount==0){
			logger.info("------------------system init menu data------------");
			Resource[] resources = resPatternResolver.getResources("classpath*:data/menus.json");
			for (int i = 0; i < resources.length; i++) {
				URL url = resources[i].getURL();
				String menus = IOUtils.toString(url);
				Menu[]list = mapper.readValue(menus, Menu[].class);
				for (Menu menu : list) {
					menu.setMenuLevel(1);
					menuService.saveMenu(menu);
				}
			}
		}
	}
}

