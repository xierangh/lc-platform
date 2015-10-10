package com.lc.platform.system.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lc.platform.commons.CalNextNum;
import com.lc.platform.commons.spring.BeanUtils;
import com.lc.platform.system.dao.DeptDao;
import com.lc.platform.system.dao.UserDao;
import com.lc.platform.system.dao.UserDeptDao;
import com.lc.platform.system.domain.Dept;
import com.lc.platform.system.domain.User;
import com.lc.platform.system.service.DeptService;

@Transactional
@Service
public class DeptServiceImpl implements DeptService{
	
	protected Log logger = LogFactory.getLog(getClass());
	@Autowired
	private DeptDao deptDao;
	@Autowired
	private UserDeptDao userDeptDao;
	@Autowired
	private UserDao userDao;
	@Override
	public List<Dept> getAllDept() {
		Sort sort = new Sort(new Order(Direction.ASC, "parentId"),new Order(Direction.ASC, "deptOrder"));
		return deptDao.findAll(sort);
	}
	@Override
	public void saveDept(Dept dept) {
		if(dept!=null){
			if(StringUtils.isBlank(dept.getId())){
				PageRequest pageable = new PageRequest(0, 1);
				Page<Dept> page = deptDao.findByParentIdOrderByCreateDateDesc(dept.getParentId(), pageable);
				CalNextNum calNextNum = new CalNextNum();
				if(page.getTotalElements()>0){
					Dept currDept = page.getContent().get(0);
					String nextId = calNextNum.nextNum(currDept.getId());
					dept.setId(nextId);
				}else if("0".equals(dept.getParentId())){
					dept.setId("001");
				}else{
					dept.setId(dept.getParentId()+"-001");
				}
				dept.setCreateDate(new Date());
				
				Dept parent = deptDao.findOne(dept.getParentId());
				if(parent!=null && parent.getLeaf()){
					parent.setLeaf(false);
					deptDao.saveAndFlush(parent);
				}
				dept.setLeaf(true);
				deptDao.saveAndFlush(dept);
			}else{
				Dept oldDept = deptDao.findOne(dept.getId());
				oldDept.setDeptName(dept.getDeptName());
				oldDept.setDeptOrder(dept.getDeptOrder());
				oldDept.setDeptCode(dept.getDeptCode());
				oldDept.setBz(dept.getBz());
				deptDao.saveAndFlush(oldDept);
			}
		}
	}
	@Override
	public void updateDept(Dept dept) {
		Dept oldDept = deptDao.findById(dept.getId());
		if(oldDept!=null){
			String deptName = dept.getDeptName();
			String oldDeptName = oldDept.getDeptName();
			BeanUtils.copyNotNullProperties(dept, oldDept);
			deptDao.saveAndFlush(oldDept);
			if(deptName!=null && !oldDeptName.equals(deptName)){
				List<User> users = userDeptDao.findUserByDept(dept.getId());
				for (User user : users) {
					List<String> deptInfoList = deptDao.findDeptNameByUserId(user.getUserId());
					String deptInfo = StringUtils.join(deptInfoList, ",");
					user.setDeptInfo(deptInfo);
					userDao.saveAndFlush(user);
				}
			}
		}
	}
	@Override
	public void moveDept(String deptIds, String parentId) {
		deptDao.updateDeptParentId(parentId,deptIds.split(","));
	}
	@Override
	public void deleteDept(String deptIds) {
		if(deptIds!=null){
			String[]deptIdArray = deptIds.split(",");
			for (String deptId : deptIdArray) {
				deptDao.deleteChildDept(deptId + "-%");
			}
			deptDao.deleteDept(deptIdArray);
		}
	}
	@Override
	public List<String> findAllGrantDept(String[] userIds) {
		return deptDao.findAllGrantDept(userIds,userIds.length);
	}
	
}
