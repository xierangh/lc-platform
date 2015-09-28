package com.lc.platform.system.vo;

import com.lc.platform.system.domain.Role;

public class RoleVO extends Role {

	private static final long serialVersionUID = 3275332926442509809L;

	/**
	 * 是否授权
	 */
	protected boolean grant;

	public boolean isGrant() {
		return grant;
	}

	public void setGrant(boolean grant) {
		this.grant = grant;
	}

}
