package com.lc.platform.system.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 角色权限信息
 * 
 * @author chenjun
 *
 */
@Entity
@Table(name = "sys_role_perm")
public class RolePerm implements Serializable {

	private static final long serialVersionUID = 6792025844588716042L;

	@Id
	protected String id;

	@ManyToOne
	@JoinColumn(name = "roleId")
	protected Role role;

	protected String permId;

	public RolePerm() {

	}

	public RolePerm(String roleId, String permId) {
		role = new Role();
		role.setId(roleId);
		this.permId = permId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getPermId() {
		return permId;
	}

	public void setPermId(String permId) {
		this.permId = permId;
	}

}
