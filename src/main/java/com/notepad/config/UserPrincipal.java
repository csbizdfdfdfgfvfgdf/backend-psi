package com.notepad.config;

import com.notepad.entity.User;
import com.notepad.entity.enumeration.ERole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = -7530187709860249942L;
	
	private User userEntity;
	private Long userId;
	private String dbUserName;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(User userEntity) {
		this.userEntity = userEntity;
		this.userId = userEntity.getUserId();
		this.password = userEntity.getPassword();
		this.dbUserName = userEntity.getUserName();
		String roleName = userEntity.getRoleName() != null ? userEntity.getRoleName().toString() : ERole.ROLE_USER.toString();
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(roleName));
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDbUserName() {
		return dbUserName;
	}

	public void setDbUserName(String dbUserName) {
		this.dbUserName = dbUserName;
	}
}
