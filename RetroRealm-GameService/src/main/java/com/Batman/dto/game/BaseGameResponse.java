package com.Batman.dto.game;

import java.time.LocalDate;
import java.util.Set;

import com.Batman.enums.GameGenre;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;

@Data
public class BaseGameResponse {
	private String gameId;

	private String gameName;
	private Double gamePrice;
	private Double gameVersion;
	
	@JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate gameReleasedDate;
	private Double gameDiscount;
	private Set<GameGenre> gameGenre;
	
	public double getDiscountedGamePrice() {
	    return this.gamePrice * (1 - this.gameDiscount / 100.0);
	}
	
	
}
