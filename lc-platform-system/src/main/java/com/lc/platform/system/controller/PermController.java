package com.lc.platform.system.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.system.domain.Menu;
import com.lc.platform.system.domain.Perm;
import com.lc.platform.system.service.MenuService;
import com.lc.platform.system.service.PermService;
import com.lc.platform.ztree.TreeNode;

@Controller
@RequestMapping("/system/perms")
public class PermController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private PermService permService;
	
	@RequestMapping("tree")
	public @ResponseBody List<TreeNode> tree(String[]roleIds){
		List<String> permGrantList = permService.findAllGrantPerm(roleIds);
		
		List<Menu> menuList = menuService.findAllMenu();
		List<TreeNode> deptNodes = new ArrayList<TreeNode>();
		for (Menu menu : menuList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(menu.getMenuId());
			treeNode.setpId(menu.getParentId());
			treeNode.setName(menu.getMenuName());
			treeNode.setIsParent(true);
			treeNode.setOpen(true);
			if(permGrantList.contains(menu.getPermCode())){
				treeNode.setChecked(true);
			}
			treeNode.setData(menu);
			deptNodes.add(treeNode);
		}
		
		List<Perm> permList = permService.findAllPerm();
		for (Perm perm : permList) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(perm.getId());
			treeNode.setpId(perm.getMenuId());
			treeNode.setName(perm.getPermName());
			treeNode.setIsParent(false);
			if(permGrantList.contains(perm.getId())){
				treeNode.setChecked(true);
			}
			treeNode.setData(perm);
			deptNodes.add(treeNode);
		}
		return deptNodes;
	}
	
}
