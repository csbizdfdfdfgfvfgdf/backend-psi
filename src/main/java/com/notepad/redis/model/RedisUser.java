package com.notepad.redis.model;

import java.io.Serializable;

/**
* The RedisUser model that have redis user info
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
public class RedisUser implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userName;
	private String uuid;
	private String password;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "RedisUser [userName=" + userName + ", uuid=" + uuid + ", password=" + password + "]";
	}
	
}
