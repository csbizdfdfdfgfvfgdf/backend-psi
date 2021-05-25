package com.notepad.entity;

import javax.persistence.*;

/**
* The Token entity to align token info with DB
*
* @author  Zohaib Ali
* @version 1.0
* @since   2021-04-22 
*/
@Entity
@Table(name = "token_master")
public class Token {

	private static final long RESET_PASSWORD_EXPIRATION = 5*60*1000; // for 5 minutes
	
	private static final long VISITOR_EXPIRATION = 24*7*60*60*1000; // for 7 days
	
	@Id
    @GeneratedValue
    private Long id;
  
    private String token;
    
    // expiry date and time of token
    private Long expiryDate;
    
    // active of deactive status of token
    private boolean status;
    
    // the user to which token is linked
    @ManyToOne
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Long expiryDate) {
		this.expiryDate = expiryDate;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public static long getResetPasswordExpiration() {
		return RESET_PASSWORD_EXPIRATION;
	}

	public static long getVisitorExpiration() {
		return VISITOR_EXPIRATION;
	}

	@Override
	public String toString() {
		return "Token [id=" + id + ", token=" + token + ", expiryDate=" + expiryDate + ", status=" + status + ", user="
				+ user + "]";
	}
    
}
