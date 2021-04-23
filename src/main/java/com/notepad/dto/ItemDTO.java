package com.notepad.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;

/**
* The ItemDTO to store a note info
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class ItemDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "The database generated note ID")
	private Long itemId;

	@ApiModelProperty(value = "The content of a note")
	private String content;

	@ApiModelProperty(value = "The order id for the sorting of notes")
	private Integer orderId;
	
	@ApiModelProperty(value = "The id of the assigned user")
	private Long uId;

	@ApiModelProperty(value = "The name of the assigned user")
	private String userName;
	
	@ApiModelProperty(value = "The parent folder id of a note")
	private Long pId; // pId = parentId
	
	@ApiModelProperty(value = "The created date of a note")
	private LocalDateTime created;
	
	@ApiModelProperty(value = "The updated date of a note")
	private LocalDateTime updated;
	
	public Long getItemId() {
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setCreated(LocalDateTime created) {
		this.created = created;
	}
	
	public LocalDateTime getUpdated() {
		return updated;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	@Override
	public String toString() {
		return "ItemDTO [itemId=" + itemId + ", content=" + content + ", orderId=" + orderId + ", uId=" + uId
				+ ", userName=" + userName + ", pId=" + pId + ", created=" + created + ", updated=" + updated + "]";
	}
	
}
