package com.lc.platform.system.domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 用户信息
 */
@Entity
@Table(name = "sys_user")
public class User implements UserDetails {
	private static final long serialVersionUID = 5028788453157667074L;

	public User() {
	}

	public User(String userId) {
		this.userId = userId;
	}

	@Id
	protected String userId;
	/**
	 * 用户名
	 */
	@Column(unique = true)
	protected String username;

	/**
	 * 电子邮箱
	 */
	@Column(unique = true)
	protected String email;

	/**
	 * 用户角色信息
	 */
	@Column(length = 4000)
	protected String roleInfo;

	/**
	 * 部门信息
	 */
	@Column(length = 4000)
	private String deptInfo;

	/**
	 * 密码
	 */
	protected String password;

	/**
	 * 用户是否可用
	 */
	protected Boolean enabled;

	/**
	 * 账号是否没有过期
	 */
	protected Boolean accountNonExpired;

	/**
	 * 账户是否锁定
	 */
	protected Boolean accountLocked;

	/**
	 * 密码是否没有过期
	 */
	protected Boolean credentialsNonExpired;

	/**
	 * 是否是超级管理员
	 */
	private Boolean superAdmin;

	/**
	 * 删除状态
	 */
	private Boolean delStatus;

	/**
	 * 真实姓名
	 */
	private String xm;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	protected Date createDate;

	/**
	 * 代码拼音
	 */
	@Column(length = 2048)
	private String pinyin;
	/**
	 * 代码简拼
	 */
	@Column(length = 2048)
	private String jianpin;

	/**
	 * 用户首页头像图片
	 */
	@Lob
	protected String headImage24;

	@JsonIgnore
	@Transient
	private Collection<GrantedAuthority> grantedAuthoritys;

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return grantedAuthoritys;
	}

	public void setGrantedAuthoritys(
			Collection<GrantedAuthority> grantedAuthoritys) {
		this.grantedAuthoritys = grantedAuthoritys;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		if(accountNonExpired == null){
			return true;
		}else{
			return accountNonExpired;
		}
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		if(accountLocked==null){
			return true;
		}
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if(credentialsNonExpired == null){
			return true;
		}else{
			return credentialsNonExpired;
		}
	}

	@Override
	public boolean isEnabled() {
		if(enabled == null){
			return true;
		}else{
			return enabled;
		}
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public void setAccountNonExpired(Boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public Boolean getAccountLocked() {
		return accountLocked;
	}

	public void setAccountLocked(Boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(String roleInfo) {
		this.roleInfo = roleInfo;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Boolean getSuperAdmin() {
		return superAdmin;
	}

	public void setSuperAdmin(Boolean superAdmin) {
		this.superAdmin = superAdmin;
	}

	public String getDeptInfo() {
		return deptInfo;
	}

	public void setDeptInfo(String deptInfo) {
		this.deptInfo = deptInfo;
	}

	public Boolean getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(Boolean delStatus) {
		this.delStatus = delStatus;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
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

	public String getHeadImage24() {
		return headImage24;
	}

	public void setHeadImage24(String headImage24) {
		this.headImage24 = headImage24;
	}

}
