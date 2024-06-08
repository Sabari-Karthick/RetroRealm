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
	@NotNull(message = "Genere cannot be empty")
	private Set<GameGenre> gameGenere;
}
