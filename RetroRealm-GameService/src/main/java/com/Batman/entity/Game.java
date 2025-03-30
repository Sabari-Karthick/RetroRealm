package com.Batman.entity;

import com.Batman.annotations.GameId;
import com.Batman.enums.GameGenre;
import com.batman.model.BaseModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"gameName"}))
public class Game extends BaseModel {

	@Id
	@GameId
	private String gameId;

	private String gameName;
	@ManyToOne
	@JoinColumn(name = "game_owner_id",nullable = false)
	private GameOwner gameOwner;
	@Column(nullable = false)
	private Double gamePrice;
	private Double gameVersion;
	private LocalDate gameReleasedDate;
	private Double gameDiscount;
	private String gameDescription;
	private Double gameRating;
	
	@ElementCollection(targetClass = GameGenre.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "gameID"))
	@Enumerated(EnumType.STRING)
	private Set<GameGenre> gameGenre;
	
	public double getDiscountedGamePrice() {
	    return this.gamePrice * (1 - this.gameDiscount / 100.0);
	}

}
