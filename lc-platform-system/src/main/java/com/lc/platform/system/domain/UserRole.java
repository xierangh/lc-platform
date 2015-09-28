package com.lc.platform.system.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户角色信息
 * 
 * @author chenjun
 *
 */
@Entity
@Table(name = "sys_user_role")
public class UserRole implements Serializable {

	private static final long serialVersionUID = 130440839557687402L;

	@Id
	protected String id;

	@ManyToOne
	@JoinColumn(name = "userId")
	protected User user;

	@ManyToOne
	@JoinColumn(name = "roleId")
	protected Role role;

	public UserRole() {

	}

	public UserRole(String userId, String roleId) {
		role = new Role();
		role.setId(roleId);
		user = new User();
		user.setUserId(userId);
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
