package com.notepad.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
* The MenuDTO to store a folder info
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class MenuDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "The database generated folder ID")
	private Long menuId;
	@ApiModelProperty(value = "The name of the folder")
	private String menuName;
	@ApiModelProperty(value = "The order id for sorting the folder")
	private Integer orderId;
	@ApiModelProperty(value = "The type of the assigned user")
	private Integer userType;
	
	@ApiModelProperty(value = "The id of the assigned user")
	private Long uId;
	@ApiModelProperty(value = "The name of the assigned user")
	private String userName;
	
	@ApiModelProperty(value = "The parent id of a folder")
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
