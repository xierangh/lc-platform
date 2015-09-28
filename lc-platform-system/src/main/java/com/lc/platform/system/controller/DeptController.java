package com.lc.platform.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.commons.spring.Message;
import com.lc.platform.commons.spring.MessageUtil;
import com.lc.platform.system.domain.Dept;
import com.lc.platform.system.service.DeptService;
import com.lc.platform.ztree.TreeNode;

@Controller
@RequestMapping("/system/depts")
public class DeptController {
	@Autowired
	private DeptService deptService;
	
	@RequestMapping("tree")
	public @ResponseBody List<TreeNode> tree(String userIds){
		List<Dept> list = deptService.getAllDept();
		List<String> deptGrantList = new ArrayList<String>();
		if(StringUtils.isNotBlank(userIds)){
			deptGrantList = deptService.findAllGrantDept(userIds.split(","));
		}
		List<TreeNode> deptNodes = new ArrayList<TreeNode>();
		for (Dept dept : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(dept.getId());
			treeNode.setpId(dept.getParentId());
			treeNode.setName(dept.getDeptName());
			treeNode.setIsParent(true);
			treeNode.setOpen(true);
			treeNode.setChecked(deptGrantList.contains(dept.getId()));
			treeNode.setData(dept);
			deptNodes.add(treeNode);
		}
		return deptNodes;
	}
	
	/**
	 * 创建组织机构信息
	 * @param dept
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="create",method = RequestMethod.POST)
	public @ResponseBody Message create(@RequestBody Dept dept) throws Exception {
		deptService.saveDept(dept);
		return MessageUtil.message(12001, dept.getId());
	}
	
	/**
	 * 更新组织机构信息
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update",method =RequestMethod.POST)
	public @ResponseBody
	Message update(@RequestBody Dept dept) throws Exception {
		deptService.updateDept(dept);
		return MessageUtil.message(12002);
	}
	
	/**
	 * 移动组织机构信息
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="move",method =RequestMethod.POST)
	public @ResponseBody
	Message move(String deptIds,String targetDeptId) throws Exception {
		deptService.moveDept(deptIds,targetDeptId);
		return MessageUtil.message(12003);
	}
	
	/**
	 * 移动组织机构信息
	 * @param dept
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="delete",method =RequestMethod.POST)
	public @ResponseBody
	Message delete(String deptIds) throws Exception {
		deptService.deleteDept(deptIds);
		return MessageUtil.message(12004);
	}
}
