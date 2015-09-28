package com.lc.platform.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分页对象
 * @version 0.0.1 
 */
public class PageBean implements Serializable{

	private static final long serialVersionUID = 7679460027367239482L;

	/**
	 * 当前页数
	 */
	protected int currentPage = 1;

	/**
	 * 满足条件总记录数
	 */
	protected int totalRows;

	/**
	 * 每页显示记录
	 */
	protected int rowsPerPage = 10;

	/**
	 * 查询出来的数据集
	 */
	protected List<Object> items = new ArrayList<Object>();
	/**
	 * 条件集合
	 */
	protected List<Condition> conditions = new ArrayList<Condition>();
	/**
	 * 排序集合
	 */
	protected List<Order> orders = new ArrayList<Order>();

	private Map<Object, Object> userdata = new HashMap<Object, Object>();

	public Map<Object, Object> getUserdata() {
		return userdata;
	}

	public void setUserdata(Map<Object, Object> userdata) {
		this.userdata = userdata;
	}

	public PageBean() {
	}

	public PageBean(int currentPage, int totalRows, int rowsPerPage,
			List<Object> items) {
		this.currentPage = currentPage;
		this.totalRows = totalRows;
		this.rowsPerPage = rowsPerPage;
		this.items = items;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public PageBean setCurrentPage(int currentPage) {
		if (currentPage < 1) {
			currentPage = 1;
		}
		this.currentPage = currentPage;
		return this;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getRowsPerPage() {
		return rowsPerPage;
	}

	public PageBean setRowsPerPage(int rowsPerPage) {
		if (rowsPerPage <= 0) {
			rowsPerPage = 10;
		}
		this.rowsPerPage = rowsPerPage;
		return this;
	}

	public List<Object> getItems() {
		return items;
	}

	public <T> void setItems(List<T> items) {
		this.items.clear();
		this.items.addAll(items);
	}

	public int getTotalPages() {
		return (totalRows + rowsPerPage - 1) / rowsPerPage;
	}

	public PageBean addCondition(Condition condition) {
		conditions.add(condition);
		return this;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public PageBean addOrder(Order order) {
		orders.add(order);
		return this;
	}

	public List<Order> getOrders() {
		return orders;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void setPropPrefix(String propPrefix) {
		for (Order order : getOrders()) {
			String name = order.getPropertyName();
			if(!name.contains(".")){
				order.setPropertyName(propPrefix + name);
			}
		}
		for (Condition condition : getConditions()) {
			String name = condition.getPropertyName();
			if(!name.contains(".")){
				condition.setPropertyName(propPrefix+name);
			}
		}
	}
	
	public void setMustPropPrefix(String propPrefix) {
		for (Order order : getOrders()) {
			String name = order.getPropertyName();
			order.setPropertyName(propPrefix + name);
		}
		for (Condition condition : getConditions()) {
			String name = condition.getPropertyName();
			condition.setPropertyName(propPrefix+name);
		}
	}

}
