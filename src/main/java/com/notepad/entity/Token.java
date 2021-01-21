package com.notepad.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "token_master")
public class Token {

	private static final long RESET_PASSWORD_EXPIRATION = 5*60*1000; // for 5 minutes
	
	private static final long VISITOR_EXPIRATION = 24*7*60*60*1000; // for 7 days
	
	@Id
    @GeneratedValue
    private Long id;
  
    private String token;
    
    private Long expiryDate;
    
    private boolean status;
    
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
