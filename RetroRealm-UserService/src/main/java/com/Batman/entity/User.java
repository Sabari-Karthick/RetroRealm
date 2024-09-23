package com.Batman.entity;


import java.time.LocalDate;
import java.util.Set;

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
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "user_details",uniqueConstraints = @UniqueConstraint(columnNames = {"name","email"}))
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userID;
	
	
	private String name;
	
	@Column(name = "dob")
	private LocalDate dateOfBirth;
	
	private String email;
	
	private AuthenticationProiver authenticationProvider;
	
	private String password;
	
	@ElementCollection(targetClass = Role.class,fetch = FetchType.EAGER)
	@CollectionTable(name = "user_roles",joinColumns = @JoinColumn(name="user_ID", referencedColumnName = "userID"))
	@Enumerated(EnumType.STRING)
	Set<Role> roles;
	
}
