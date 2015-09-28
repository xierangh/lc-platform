package com.lc.platform.system.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 多级菜单信息
 */
@Entity
@Table(name = "sys_menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = -4674086167255280291L;
	public static final String menuType_class = "class";
	public static final String menuType_url = "url";
	public static final String menuType_add = "add";
	public static final String menuType_dir = "dir";

	@Id
	protected String menuId;
	/**
	 * 菜单名称
	 */
	protected String menuName;
	/**
	 * 菜单图片(48x48)
	 */
	protected String menuImage;

	/**
	 * 菜单图片(48x48)base64
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	protected String image48;

	/**
	 * 菜单类型
	 */
	protected String menuType;

	/**
	 * 菜单值
	 */
	protected String menuVal;

	/**
	 * 桌面序号
	 */
	protected int desktopNumber;

	/**
	 * 同桌菜单排序
	 */
	protected int menuOrder;
	/**
	 * 菜单备注
	 */
	protected String bz;

	/**
	 * 展示的宽度
	 */
	protected Integer showWidth;

	/**
	 * 展示的高度
	 */
	protected Integer showHeight;

	/**
	 * 创建时间
	 */
	@Temporal(TemporalType.DATE)
	protected Date createDate;

	/**
	 * 父级代码
	 */
	protected String parentId;

	/**
	 * 权限标志，拥有该权限者才能够看到
	 */
	protected String permCode;

	/**
	 * 菜单级别(1:系统级别的菜单)
	 */
	protected int menuLevel;

	public Menu() {
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}

	public String getMenuImage() {
		return menuImage;
	}

	public void setMenuImage(String menuImage) {
		this.menuImage = menuImage;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuVal() {
		return menuVal;
	}

	public void setMenuVal(String menuVal) {
		this.menuVal = menuVal;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}

	public String getImage48() {
		return image48;
	}

	public void setImage48(String image48) {
		this.image48 = image48;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getDesktopNumber() {
		return desktopNumber;
	}

	public void setDesktopNumber(int desktopNumber) {
		this.desktopNumber = desktopNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((menuId == null) ? 0 : menuId.hashCode());
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
		Menu other = (Menu) obj;
		if (menuId == null) {
			if (other.menuId != null)
				return false;
		} else if (!menuId.equals(other.menuId))
			return false;
		return true;
	}

	public String getPermCode() {
		return permCode;
	}

	public void setPermCode(String permCode) {
		this.permCode = permCode;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}

	public void setShowWidth(Integer showWidth) {
		this.showWidth = showWidth;
	}

	public void setShowHeight(Integer showHeight) {
		this.showHeight = showHeight;
	}

	public Integer getShowWidth() {
		return showWidth;
	}

	public Integer getShowHeight() {
		return showHeight;
	}

}
