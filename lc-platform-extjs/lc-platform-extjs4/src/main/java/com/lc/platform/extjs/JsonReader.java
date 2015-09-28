package com.lc.platform.extjs;

import java.util.ArrayList;
import java.util.List;

public class JsonReader {
	/**
	 * json中代表实际模型数据的入口
	 */
	protected List<Object> rows = new ArrayList<Object>();
	/**
	 * json中代表页码总数的数据
	 */
	protected long total;
	/**
	 * json中代表数据行总数的数据
	 */
	protected long records;

	public List<Object> getRows() {
		return rows;
	}

	public void setRows(List<Object> rows) {
		this.rows = rows;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getRecords() {
		return records;
	}

	public void setRecords(long records) {
		this.records = records;
	}

}
