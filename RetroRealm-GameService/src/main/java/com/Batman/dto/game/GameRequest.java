package com.Batman.dto.game;

import java.time.LocalDate;
import java.util.Set;

import com.Batman.enums.GameGenre;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for requesting game creation.
 * This class encapsulates the data required to create a new game,
 * including the game's name, price, version, release date, and genre.
 *
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@code @Data} - Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.</li>
 *   <li>{@code @NotNull} - Jakarta Validation annotation to ensure a field is not null.</li>
 *   <li>{@code @NotBlank} - Jakarta Validation annotation to ensure a string field is not null or empty.</li>
 *   <li>{@code @JsonSerialize} - Jackson annotation to customize the serialization of a field.</li>
 *   <li>{@code @JsonFormat} - Jackson annotation to specify the format of a date field.</li>
 * </ul>
 * </p>
 *
 * @author SK
 */



@Data
public class GameRequest {

    @NotNull(message = "Owner ID cannot be empty")
    private Integer gameOwnerID;

    @NotBlank(message = "Game name cannot be empty")
    private String gameName;

    @NotNull(message = "Price cannot be empty")
    private Double gamePrice;

    @NotNull(message = "Version cannot be empty")
    private Double gameVersion;

    @NotNull(message = "Release Date cannot be empty")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")

    private LocalDate gameReleasedDate;

    @NotNull(message = "Genre cannot be empty")
    private Set<GameGenre> gameGenre;
}
