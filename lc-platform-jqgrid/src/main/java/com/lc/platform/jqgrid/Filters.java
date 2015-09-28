package com.lc.platform.jqgrid;

import java.util.List;


public class Filters {
	private String groupOp;
	private List<RuleItem> rules;
	protected List<GroupItem> groups;
	
	public String getGroupOp() {
		return groupOp;
	}
	public void setGroupOp(String groupOp) {
		this.groupOp = groupOp;
	}
	public List<RuleItem> getRules() {
		return rules;
	}
	public void setRules(List<RuleItem> rules) {
		this.rules = rules;
	}
	public List<GroupItem> getGroups() {
		return groups;
	}
	public void setGroups(List<GroupItem> groups) {
		this.groups = groups;
	}
	
	
}
