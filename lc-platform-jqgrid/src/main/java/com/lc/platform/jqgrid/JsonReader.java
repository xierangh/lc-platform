package com.lc.platform.jqgrid;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {

	/**
	 * json中代表实际模型数据的入口
	 */
	protected List<Object> rows = new ArrayList<Object>();
	/**
	 * json中代表当前页码的数据
	 */
	protected long page;
	/**
	 * json中代表页码总数的数据
	 */
	protected long total;
	/**
	 * json中代表数据行总数的数据
	 */
	protected long records;
	/**
	 * 用户数据
	 */
	protected Object userdata;

	public List<Object> getRows() {
		return rows;
	}

	public JsonReader setRows(List<Object> rows) {
		this.rows = rows;
		return this;
	}
	
	public JsonReader addRows(RowItem rowItem){
		this.rows.add(rowItem);
		return this;
	}

	public long getPage() {
		return page;
	}

	public JsonReader setPage(long page) {
		this.page = page;
		return this;
	}

	public long getTotal() {
		return total;
	}

	public JsonReader setTotal(long total) {
		this.total = total;
		return this;
	}

	public long getRecords() {
		return records;
	}

	public JsonReader setRecords(long records) {
		this.records = records;
		return this;
	}

	public Object getUserdata() {
		return userdata;
	}

	public JsonReader setUserdata(Object userdata) {
		this.userdata = userdata;
		return this;
	}

}
