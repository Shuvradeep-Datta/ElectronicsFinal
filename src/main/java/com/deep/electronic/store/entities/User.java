package com.deep.electronic.store.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8928526851779069173L;
	@Id
	private String userId;
	@Column(name = "user_name")
	private String name;
	@Column(name = "user_email", unique = true)
	private String email;
	@Column(name = "user_password")
	private String password;
	private String gender;
	@Column(length = 10000)
	private String about;
	@Column(name = "user_image_name")
	private String imageName;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Cart cart;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@Builder.Default
	private List<Order> orders = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Role> roles = new ArrayList<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> grantedAuthority = roles.stream()
				.map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
		return grantedAuthority;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
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

}
