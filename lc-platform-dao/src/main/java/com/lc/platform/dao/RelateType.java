package com.lc.platform.dao;

/**
 * QL条件关系
 * @version 0.0.1
 */
public enum RelateType{

	AND(" AND "), OR(" OR ");

	private String desc;

	private RelateType(String desc) {
		this.desc = desc;
	}

	public String toString() {
		return desc;
	}
}
