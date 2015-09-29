package com.lc.platform.system.vo;

import javax.persistence.Id;

import com.lc.platform.extjs.NodeInterface;

public class DictNode extends NodeInterface {

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
	 * 代码类型
	 */
	protected Object codeType;

	/**
	 * 代码排序
	 */
	protected Integer dictOrder;

	/**
	 * 是否是默认值
	 */
	protected Boolean defaultVal;

	public Integer getDictOrder() {
		return dictOrder;
	}

	public void setDictOrder(Integer dictOrder) {
		this.dictOrder = dictOrder;
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

	public Object getCodeType() {
		return codeType;
	}

	public void setCodeType(Object codeType) {
		this.codeType = codeType;
	}

	public Boolean getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(Boolean defaultVal) {
		this.defaultVal = defaultVal;
	}

}
