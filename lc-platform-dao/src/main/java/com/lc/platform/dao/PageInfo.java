package com.lc.platform.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * 针对spring-data-jpa进行分页信息
 * 
 */
public class PageInfo<T> {

	private Pageable pageable;
	private Specification<T> specs;
	private Page<T> page;

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public Specification<T> getSpecs() {
		return specs;
	}

	public void setSpecs(Specification<T> specs) {
		this.specs = specs;
	}

	public Page<T> getPage() {
		return page;
	}

	public void setPage(Page<T> page) {
		this.page = page;
	}

}
