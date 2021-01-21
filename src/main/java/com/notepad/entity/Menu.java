package com.notepad.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pre_xiaomy_cus_todo_menu")
public class Menu implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long menuId;
	
	private String menuName;
	private Integer orderId;
	private Integer userType;
	
//	@ManyToOne
//	@JoinColumn(name = "uid")
//	private User userId;
	
	@ManyToOne
	//@JoinColumn(name = "username")
	@JoinColumns({
		@JoinColumn(name = "username", referencedColumnName = "userName"),
        @JoinColumn(name = "uid", referencedColumnName = "userId")
    })
	private User user;
	
	@ManyToOne()
	@JoinColumn(name = "pid", referencedColumnName = "menuId")
//	@Nullable
	private Menu parentMenu;
	
	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Menu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}

	@Override
	public String toString() {
		return "Menu [menuId=" + menuId + ", menuName=" + menuName + ", orderId=" + orderId + ", userType=" + userType
				+ ", user=" + user + ", parentMenu=" + parentMenu + "]";
	}
	
}
