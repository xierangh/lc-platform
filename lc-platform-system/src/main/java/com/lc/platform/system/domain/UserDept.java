package com.lc.platform.system.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 用户部门信息
 * 
 * @author chenjun
 *
 */
@Entity
@Table(name = "sys_user_dept")
public class UserDept implements Serializable {

	private static final long serialVersionUID = -1544489408090677462L;

	@Id
	protected String id;

	@ManyToOne
	@JoinColumn(name = "userId")
	protected User user;

	@ManyToOne
	@JoinColumn(name = "deptId")
	protected Dept dept;

	public UserDept() {

	}

	public UserDept(String userId, String deptId) {
		dept = new Dept();
		dept.setId(deptId);
		user = new User();
		user.setUserId(userId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
