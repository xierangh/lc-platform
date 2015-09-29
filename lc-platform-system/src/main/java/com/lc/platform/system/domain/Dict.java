package com.lc.platform.system.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 系统数据词典
 * 
 */
@Entity
@Table(name = "sys_dict")
public class Dict implements Serializable {

	private static final long serialVersionUID = -8372286897129189089L;

	@Id
	protected String id;

	/**
	 * 数字代码
	 */
	protected String numberCode;

	/**
	 * 代码名称
	 */
	protected String codeName;

	/**
	 * 字母代码，唯一标识
	 */
	protected String letterCode;

	/**
	 * 代码拼音
	 */
	@Column(length = 2028)
	protected String pinyin;

	/**
	 * 代码简拼
	 */
	@Column(length = 2048)
	protected String jianpin;

	/**
	 * 代码排序
	 */
	protected Integer dictOrder;

	/**
	 * 代码类型(1:系统代码 2.用户代码)
	 */
	protected int codeType;

	/**
	 * 说明
	 */
	protected String dictDesc;

	/**
	 * 父级代码
	 */
	protected String parentId;

	/**
	 * 是否是默认值
	 */
	protected Boolean defaultVal;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createDate;

	/**
	 * 是否是叶子节点
	 */
	protected Boolean leaf;

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNumberCode() {
		return numberCode;
	}

	public void setNumberCode(String numberCode) {
		this.numberCode = numberCode;
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

	public Integer getDictOrder() {
		return dictOrder;
	}

	public void setDictOrder(Integer dictOrder) {
		this.dictOrder = dictOrder;
	}

	public int getCodeType() {
		return codeType;
	}

	public void setCodeType(int codeType) {
		this.codeType = codeType;
	}

	public String getDictDesc() {
		return dictDesc;
	}

	public void setDictDesc(String dictDesc) {
		this.dictDesc = dictDesc;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Boolean getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(Boolean defaultVal) {
		this.defaultVal = defaultVal;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
