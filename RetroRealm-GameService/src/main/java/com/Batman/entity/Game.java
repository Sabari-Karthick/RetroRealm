package com.Batman.entity;

import java.time.LocalDate;
import java.util.Set;

import com.Batman.enums.GameGenre;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer gameID;

	private String gameName;
	@ManyToOne
	@JoinColumn(name = "game_owner_id",nullable = false)
	private GameOwner gameOwner;
	private Double gamePrice;
	private Double gameVersion;
	private LocalDate gameReleasedDate;
	private Integer gameDiscount;
	
	@ElementCollection(targetClass = GameGenre.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "gameID"))
	@Enumerated(EnumType.STRING)
	private Set<GameGenre> gameGenere;

}
