package com.Batman.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameOwner {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameOwnerID;
	@Column(unique = true)
	private String companyName;
	
	@Column(unique = true)
	private String email;
	
	@OneToMany(mappedBy = "gameOwner",cascade = CascadeType.ALL)
	private List<Game> games;
	
	private Boolean isVerified;
	
}
