package com.Batman.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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

import static com.Batman.constants.GameConstants.GAME_INDEX_NAME;
import static com.Batman.constants.GameConstants.GAME_OWNER;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"gameName"}))
public class Game extends BaseModel {

    public Game() {
        this.setIndexName(GAME_INDEX_NAME);
    }

    @Id
    @GameId
    private String gameId;

    @Column(nullable = false)
    private String gameName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_owner_id", nullable = false)
    private GameOwner gameOwner;

    @Column(nullable = false)
    private Double gamePrice;

    private Double gameVersion;

    private LocalDate gameReleasedDate;

    private String gameDescription;

    private Double gameRating;

    // Lazy is mainly added as internal requests no longer need to fetch the genres, and it can be fetched on demand when needed. This can help improve performance by reducing unnecessary data loading, especially if the genre information is not always required in every request. However, it does require careful handling to avoid LazyInitializationException, which can occur if the genres are accessed outside of a transactional context.
    // To mitigate this, we can use fetch joins in our queries when we know we will need the genre information, ensuring that it is loaded efficiently without causing exceptions.
    @ElementCollection(targetClass = GameGenre.class, fetch = FetchType.LAZY) // Lazy fetching for better performance , Needs to modify the getGame calls to use a fetch join to avoid LazyInitializationException
    @CollectionTable(name = "game_genres", joinColumns = @JoinColumn(name = "game_id", referencedColumnName = "gameID"))
    @Enumerated(EnumType.STRING)
    private Set<GameGenre> gameGenre;

    @Override
    public String getPrimaryKeyValue() {
        return this.gameId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(gameDescription, gameGenre, gameId, gameName, gameOwner,
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
                && Objects.equals(gameGenre, other.gameGenre)
                && Objects.equals(gameId, other.gameId) && Objects.equals(gameName, other.gameName)
                && Objects.equals(gameOwner, other.gameOwner) && Objects.equals(gamePrice, other.gamePrice)
                && Objects.equals(gameRating, other.gameRating)
                && Objects.equals(gameReleasedDate, other.gameReleasedDate)
                && Objects.equals(gameVersion, other.gameVersion);
    }


    @Override
    public Map<String, List<String>> getFlattenFieldsConfig() {
        return Map.of(GAME_OWNER, List.of("gameOwnerID", "companyName", "email"));
    }
}
