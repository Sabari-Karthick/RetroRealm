package com.Batman.entity;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import com.Batman.annotations.GameId;
import com.Batman.enums.GameGenre;
import com.batman.model.BaseModel;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "gameName" }))
public class Game extends BaseModel {

	public Game() {
		this.setIndexName("idx_game");
	}

	@Id
	@GameId
	private String gameId;

	private String gameName;
	@ManyToOne
	@JoinColumn(name = "game_owner_id", nullable = false)
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

	@Override
	public String getPrimaryKeyValue() {
		return this.gameId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(gameDescription, gameDiscount, gameGenre, gameId, gameName, gameOwner,
				gamePrice, gameRating, gameReleasedDate, gameVersion);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		return Objects.equals(gameDescription, other.gameDescription)
				&& Objects.equals(gameDiscount, other.gameDiscount) && Objects.equals(gameGenre, other.gameGenre)
				&& Objects.equals(gameId, other.gameId) && Objects.equals(gameName, other.gameName)
				&& Objects.equals(gameOwner, other.gameOwner) && Objects.equals(gamePrice, other.gamePrice)
				&& Objects.equals(gameRating, other.gameRating)
				&& Objects.equals(gameReleasedDate, other.gameReleasedDate)
				&& Objects.equals(gameVersion, other.gameVersion);
	}

}
