package com.notepad.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

/**
* The Item entity to align note info with DB
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Entity
@Table(name = "pre_xiaomy_cus_todo_item")
public class Item implements Serializable {

	/**
	 * default serial version id
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long itemId;
	private String content;
	private Integer orderId;
	
//	@ManyToOne
//	@JoinColumn(name = "uid")
//	private User userId;
	
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name = "username", referencedColumnName = "userName"),
        @JoinColumn(name = "uid", referencedColumnName = "userId")
    })
	private User user;

	@ManyToOne
	@JoinColumn(name = "pid", referencedColumnName = "menuId",nullable=true)
	private Menu menu;

	private LocalDateTime created;
	private LocalDateTime updated;

	@PrePersist
	protected void onCreate() {
		created = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updated = LocalDateTime.now();
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

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Item [itemId=" + itemId + ", content=" + content + ", orderId=" + orderId + ", user=" + user + ", menu="
				+ menu + ", created=" + created + ", updated=" + updated + "]";
	}

}
