package com.Batman.dto;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import lombok.Data;

@Data
public class UserPrincipal implements UserDetails/*,OAuth2User*/,Serializable{


	private static final long serialVersionUID = 1L;
	
	private final transient User user;
	private transient Map<String, Object> attributes;
	
	
	
	public UserPrincipal(User user) {
		this.user = user;
	}
	
	
	
	public UserPrincipal(User user, Map<String, Object> attributes) {
		this.user = user;
		this.attributes = attributes;
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		 return user.getRoles().stream().map(role->new SimpleGrantedAuthority(role.toString())).toList();
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
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

//	@Override
//	public Map<String, Object> getAttributes() {
//		return attributes;
//	}
//
//	@Override
//	public String getName() {
//		return user.getEmail();
//	}

}
