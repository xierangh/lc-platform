package com.lc.platform.system.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 组织机构基本信息
 * 
 */
@Entity
@Table(name = "sys_dept")
public class Dept implements Serializable {
	private static final long serialVersionUID = 8743845685886266999L;

	public Dept() {
	}

	public Dept(String id) {
		this.id = id;
	}

	@Id
	private String id;

	/**
	 * 机构名称
	 */
	private String deptName;

	/**
	 * 机构编号
	 */
	private String deptCode;

	/**
	 * 父级机构
	 */
	private String parentId;

	/**
	 * 同级机构排序
	 */
	private Integer deptOrder;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createDate;
	/**
	 * 备注
	 */
	private String bz;

	/**
	 * 是否是叶子节点
	 */
	protected Boolean leaf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getDeptOrder() {
		return deptOrder;
	}

	public void setDeptOrder(Integer deptOrder) {
		this.deptOrder = deptOrder;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dept other = (Dept) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

}
