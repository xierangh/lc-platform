package com.lc.platform.system.service.impl;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lc.platform.commons.PinYinUtil;
import com.lc.platform.commons.UUIDUtil;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.dao.Condition;
import com.lc.platform.dao.Operation;
import com.lc.platform.dao.PageBean;
import com.lc.platform.system.SystemUtil;
import com.lc.platform.system.dao.DeptDao;
import com.lc.platform.system.dao.RoleDao;
import com.lc.platform.system.dao.UserDao;
import com.lc.platform.system.dao.UserDeptDao;
import com.lc.platform.system.dao.UserRoleDao;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.domain.UserDept;
import com.lc.platform.system.domain.UserRole;
import com.lc.platform.system.exception.EmailUniqueException;
import com.lc.platform.system.exception.UserException;
import com.lc.platform.system.exception.UserNameUniqueException;
import com.lc.platform.system.security.AuthorityComparator;
import com.lc.platform.system.service.UserService;

@Transactional
@Service
public class UserServiceImpl implements UserService{
	
	protected Log logger = LogFactory.getLog(getClass());
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleDao userRoleDao;
	@Autowired
	private UserDeptDao userDeptDao;
	@Autowired
	private DeptDao deptDao;
	@Autowired
	private RoleDao roleDao;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(4, new SecureRandom());
	
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		User user = null;
		try {
			user = userDao.findUserByUsername(username);
		} catch (Exception e) {
			throw new UserException(e.getMessage());
		}
		String msg = "";
		if (user == null) {
			msg = MessageUtil.getMessage("14003",new Object[] { username });
			logger.debug(msg);
			throw new UsernameNotFoundException(msg);
		}
		Set<GrantedAuthority> dbAuths = new HashSet<GrantedAuthority>();
		if (dbAuths.size() == 0) {
			msg = MessageUtil.getMessage("14004",new Object[] { username });
			logger.info(msg);
		}
		user.setGrantedAuthoritys(Collections
				.unmodifiableSet(sortAuthorities(dbAuths)));
		return user;
	}
	
	private static SortedSet<GrantedAuthority> sortAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		Assert.notNull(authorities,
				"Cannot pass a null GrantedAuthority collection");
		SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet<GrantedAuthority>(
				new AuthorityComparator());
		for (GrantedAuthority grantedAuthority : authorities) {
			Assert.notNull(grantedAuthority,
					"GrantedAuthority list cannot contain any null elements");
			sortedAuthorities.add(grantedAuthority);
		}
		return sortedAuthorities;
	}

	@Override
	public void saveUser(User user) throws UserException {
		if(user!=null){
			checkUserNameUnique(user.getUsername());
			checkEmailUnique(user.getEmail());
		}else{
			return;
		}
		user.setUserId(UUIDUtil.uuid());
		String encryptPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encryptPassword);
		user.setAccountNonExpired(true);
		user.setEnabled(true);
		user.setCredentialsNonExpired(true);
		if(user.getSuperAdmin()==null){
			user.setSuperAdmin(false);
		}
		user.setAccountLocked(false);
		user.setDelStatus(false);
		try {
			user.setJianpin(PinYinUtil.hanyuToJpStr(user.getXm()));
			user.setPinyin(PinYinUtil.hanyuToPyStr(user.getXm()));
		} catch (Exception e) {
			logger.warn(MessageUtil.getMessage("14005"));
		}
		user.setCreateDate(new Date());
		userDao.save(user);
	}
	
	public void checkUserNameUnique(String username) throws UserNameUniqueException{
		if(StringUtils.isBlank(username)){
			throw new UserNameUniqueException(username);
		}
		User user = userDao.findUserByUsername(username);
		if(user!=null){
			throw new UserNameUniqueException(username);
		}
	}
	
	public void checkEmailUnique(String email)throws EmailUniqueException{
		if(StringUtils.isBlank(email)){
			throw new EmailUniqueException(email);
		}
		User user = userDao.findUserByEmail(email);
		if(user!=null){
			throw new EmailUniqueException(email);
		}
	}

	@Override
	public void readUsersByPageInfo(PageBean pageBean) {
		pageBean.setPropPrefix("user.");
		pageBean.setMustPropPrefix("ud.");
		if (!SystemUtil.isSuperAdmin()) {
			pageBean.addCondition(new Condition("superAdmin", false,
					Operation.EQ));
			pageBean.addCondition(new Condition("delStatus", false,
					Operation.EQ));
		}
		userDao.doPager(pageBean, "select ud.user from UserDept ud where 1=1");
		for(Object item : pageBean.getItems()){
			User user = (User)item;
			user.setPassword(null);
		}
	}

	@Override
	public void deleteUser(String[] ids) {
		//userRoleDao.deleteByUserId(ids);
		//userDeptDao.deleteByUserId(ids);
		userDao.batchDelete(true,ids);
	}

	@Override
	public void saveUser(User user, String[] roleIds, String[] deptIds) {
		
		List<String> deptInfoList = deptDao.findDeptNameById(deptIds);
		String deptInfo = StringUtils.join(deptInfoList, ",");
		
		List<String> roleInfoList = roleDao.findRoleNameById(roleIds);
		String roleInfo = StringUtils.join(roleInfoList,",");
		
		user.setDeptInfo(deptInfo);
		user.setRoleInfo(roleInfo);
		saveUser(user);
		if(roleIds!=null){
			for (String roleId : roleIds) {
				UserRole userRole = new UserRole(user.getUserId(), roleId);
				userRole.setId(UUIDUtil.uuid());
				userRoleDao.save(userRole);
			}
		}
		for (String deptId : deptIds) {
			UserDept userDept = new UserDept(user.getUserId(), deptId);
			userDept.setId(UUIDUtil.uuid());
			userDeptDao.save(userDept);
		}
	}


	@Override
	public void updateUser(User user) {
		if(user!=null && StringUtils.isNotBlank(user.getUserId())){
			
			User oldUser = userDao.findOne(user.getUserId());
			
			if(!oldUser.getEmail().equals(user.getEmail())){
				checkEmailUnique(user.getEmail());
				oldUser.setEmail(user.getEmail());
			}
			
			if(StringUtils.isNotBlank(user.getPassword())){
				String encryptPassword = bCryptPasswordEncoder.encode(user.getPassword());
				oldUser.setPassword(encryptPassword);
			}
			
			if(!oldUser.getXm().equals(user.getXm())){
				try {
					oldUser.setJianpin(PinYinUtil.hanyuToJpStr(user.getXm()));
					oldUser.setPinyin(PinYinUtil.hanyuToPyStr(user.getXm()));
				} catch (Exception e) {
					logger.warn(MessageUtil.getMessage("14005"));
				}
			}
			oldUser.setDelStatus(user.getDelStatus());
			
			userDao.save(oldUser);
		}
	}

	@Override
	public void updateUserRole(String[] userIds, String[] roleIds, boolean grant) {
		for (String userId : userIds) {
			for (String roleId : roleIds) {
				UserRole userRole = new UserRole(userId, roleId);
				if(grant){
					UserRole oldUserRole = userRoleDao.findByUserIdAndRoleId(userId,roleId);
					if(oldUserRole==null){
						userRole.setId(UUIDUtil.uuid());
						userRoleDao.save(userRole);
					}
				}else{
					userRoleDao.deleteByUserIdAndRoleId(userId,roleId);
				}
				List<String> roleInfoList = roleDao.findRoleNameByUserId(userId);
				String roleInfo = StringUtils.join(roleInfoList,",");
				User user = userDao.findOne(userId);
				user.setRoleInfo(roleInfo);
				userDao.saveAndFlush(user);
			}
		}
	}

	@Override
	public void updateUserDept(String[] userIds, String deptId, boolean grant) {
		for (String userId : userIds) {
			UserDept userDept = new UserDept(userId, deptId);
			if(grant){
				UserDept oldUserDept = userDeptDao.findByUserIdAndDeptId(userId,deptId);
				if(oldUserDept==null){
					userDept.setId(UUIDUtil.uuid());
					userDeptDao.save(userDept);	
				}
			}else{
				userDeptDao.deleteByUserIdAndDeptId(userId,deptId);
			}
			List<String> deptInfoList = deptDao.findDeptNameByUserId(userId);
			String deptInfo = StringUtils.join(deptInfoList, ",");
			User user = userDao.findOne(userId);
			user.setDeptInfo(deptInfo);
			userDao.saveAndFlush(user);
		}
	}

	@Override
	public void updateUserHeadImage(String userId, String headImageBase64) {
		userDao.updateUserHeadImage(userId,headImageBase64);
	}

	@Override
	public void updatePassword(String userId, String oldPassword,
			String newPassword) {
		User user = userDao.findOne(userId);
		if(bCryptPasswordEncoder.matches(oldPassword, user.getPassword())){
			String encryptPassword = bCryptPasswordEncoder.encode(newPassword);
			user.setPassword(encryptPassword);
			userDao.saveAndFlush(user);
		}else{
			throw new UserException(14015);
		}
	}
	
}
