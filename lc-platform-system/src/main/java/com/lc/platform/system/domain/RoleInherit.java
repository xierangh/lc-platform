package com.lc.platform.system.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 角色继承信息
 * @author chenjun
 *
 */
@Entity
@Table(name = "sys_role_inherit")
public class RoleInherit implements Serializable{

	private static final long serialVersionUID = 3922104072816193520L;
	@Id
	@ManyToOne
	@JoinColumn(name = "fartherId")
	protected Role fartherRole;
	@Id
	@ManyToOne
	@JoinColumn(name = "childId")
	protected Role childRole;

	public Role getFartherRole() {
		return fartherRole;
	}

	public void setFartherRole(Role fartherRole) {
		this.fartherRole = fartherRole;
	}

	public Role getChildRole() {
		return childRole;
	}

	public void setChildRole(Role childRole) {
		this.childRole = childRole;
	}

}
