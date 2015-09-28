package com.lc.platform.xzqh.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_xzqh")
public class Xzqh {

	public static final String _parentId = "parentId";
	public static final String _px = "px";

	@Id
	private String id;
	/**
	 * 简短名称
	 */
	protected String shortName;
	/**
	 * 名称
	 */
	protected String codeName;
	/**
	 * 字母代码
	 */
	protected String letterCode;
	/**
	 * 数字代码
	 */
	protected String numberCode;

	/**
	 * 代码拼音
	 */
	@Column(length = 8192)
	protected String pinyin;

	/**
	 * 代码简拼
	 */
	@Column(length = 2048)
	protected String jianpin;

	/**
	 * 代码排序
	 */
	protected Integer px;

	/**
	 * 父级代码
	 */
	protected String parentId;

	/**
	 * 创建时间
	 */
	protected Long createDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodeName() {
		return codeName;
	}

	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}

	public String getLetterCode() {
		return letterCode;
	}

	public void setLetterCode(String letterCode) {
		this.letterCode = letterCode;
	}

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getJianpin() {
		return jianpin;
	}

	public void setJianpin(String jianpin) {
		this.jianpin = jianpin;
	}

	public Integer getPx() {
		return px;
	}

	public void setPx(Integer px) {
		this.px = px;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Long createDate) {
		this.createDate = createDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((numberCode == null) ? 0 : numberCode.hashCode());
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
		Xzqh other = (Xzqh) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (numberCode == null) {
			if (other.numberCode != null)
				return false;
		} else if (!numberCode.equals(other.numberCode))
			return false;
		return true;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

}
