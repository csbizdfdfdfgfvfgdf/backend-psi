package com.notepad.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ItemDTO implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;
	
	private Long itemId;
	private String content;
	private Integer orderId;
	
	private Long uId;
	private String userName;
	
	private Long pId; // pId = parentId
	
	private LocalDateTime created;
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
