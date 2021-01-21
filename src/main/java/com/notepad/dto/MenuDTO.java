package com.notepad.dto;

import java.io.Serializable;

public class MenuDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	private Long menuId;
	private String menuName;
	private Integer orderId;
	private Integer userType;
	
	private Long uId;
	private String userName;
	
	private Long pId; // pId = parentId

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

	public Long getuId() {
		return uId;
	}

	public void setuId(Long uId) {
		this.uId = uId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getpId() {
		return pId;
	}

	public void setpId(Long pId) {
		this.pId = pId;
	}

	@Override
	public String toString() {
		return "MenuDTO [menuId=" + menuId + ", menuName=" + menuName + ", orderId=" + orderId + ", userType="
				+ userType + ", uId=" + uId + ", userName=" + userName + ", pId=" + pId + "]";
	}
	
}
