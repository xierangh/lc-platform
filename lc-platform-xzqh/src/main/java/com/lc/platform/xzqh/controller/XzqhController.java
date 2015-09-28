package com.lc.platform.xzqh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lc.platform.xzqh.domain.Xzqh;
import com.lc.platform.xzqh.service.XzqhService;
import com.lc.platform.ztree.TreeNode;

@Controller
@RequestMapping(value = "/xzqhs")
public class XzqhController {
	
	@Autowired
	private XzqhService xzqhService;
	
	@RequestMapping(value="manager")
	public String index()throws Exception{
		return XzqhConstant.rootPath  + "manager";
	}
	
	@RequestMapping("/{pid}/childs")
	public @ResponseBody List<TreeNode> getChildsByPid(@PathVariable String pid){
		List<Xzqh> childs = xzqhService.getChildsByPid(pid);
		List<TreeNode> zNodes = new ArrayList<TreeNode>();
		for (Xzqh xzqh : childs) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(xzqh.getId());
			treeNode.setName(xzqh.getCodeName());
			treeNode.setpId(xzqh.getParentId());
			boolean isParent = xzqh.getNumberCode().length()!=17;
			treeNode.setIsParent(isParent);
			treeNode.setData(xzqh);
			zNodes.add(treeNode);
		}
		return zNodes;
	}
	
	@RequestMapping("childs")
	public @ResponseBody List<TreeNode> childs(String id){
		if(StringUtils.isBlank(id)){
			id = "0";
		}
		return getChildsByPid(id);
	}
	
	@RequestMapping("search")
	public @ResponseBody List<TreeNode> search(String content){
		if(StringUtils.isBlank(content)){
			return getChildsByPid("0");
		}
		long start = System.currentTimeMillis();
		Set<Xzqh> list = xzqhService.searchXzqh(content);
		long end = System.currentTimeMillis();
		System.out.println("所花时间：" + (end-start));
		List<TreeNode> zNodes = new ArrayList<TreeNode>();
		for (Xzqh xzqh : list) {
			TreeNode treeNode = new TreeNode();
			treeNode.setId(xzqh.getId());
			treeNode.setName(xzqh.getCodeName());
			treeNode.setpId(xzqh.getParentId());
			boolean isParent = xzqh.getNumberCode().length()!=17;
			treeNode.setIsParent(isParent);
			if(xzqh.getCodeName().contains("span")){
				treeNode.setOpen(false);
			}else{
				treeNode.setOpen(true);
			}
			treeNode.setData(xzqh);
			zNodes.add(treeNode);
		}
		return zNodes;
	}
	
}
