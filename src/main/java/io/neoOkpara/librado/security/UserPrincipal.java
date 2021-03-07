package io.neoOkpara.librado.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.neoOkpara.librado.ws.entity.User;

public class UserPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private User userEntity;

	public UserPrincipal(User userEntity) {
		this.userEntity = userEntity;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		
		// Implement Multi Role System Here
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
	}

	@Override
	public String getPassword() {
		return this.userEntity.getEncryptedPassword();
	}

	@Override
	public String getUsername() {
		return this.userEntity.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.userEntity.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.userEntity.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.userEntity.isCredentialsNonExpired();
	}

	@Override
	public boolean isEnabled() {
		return this.userEntity.isEnabled();
	}

	public Optional<User> getUser() {
		return Optional.of(this.userEntity);
	}

}
