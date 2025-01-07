package com.Batman.entity;


import java.time.LocalDate;
import java.util.Set;
import java.util.List;

import com.Batman.enums.AuthenticationProiver;
import com.Batman.enums.Role;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "user_details",uniqueConstraints = @UniqueConstraint(columnNames = {"name","email"}))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userID;
	
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "dob",nullable = false)
	private LocalDate dateOfBirth;
	
	@Column(nullable = false)
	private String email;
	
	@Enumerated(EnumType.STRING)
	private AuthenticationProiver authenticationProvider;
	
	@Column(nullable = false)
	private String password;
	
	@ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name="user_ID", referencedColumnName = "userID"))
	@Enumerated(EnumType.STRING)
	private Set<Role> roles;
	
//	private List<Integer> gameIds; // User Library.
}
