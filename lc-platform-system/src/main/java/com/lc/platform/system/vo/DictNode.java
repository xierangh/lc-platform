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

}
