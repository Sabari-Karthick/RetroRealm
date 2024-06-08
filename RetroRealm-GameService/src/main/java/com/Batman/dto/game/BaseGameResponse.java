package com.Batman.dto.game;

import java.time.LocalDate;
import java.util.Set;

import com.Batman.enums.GameGenre;

import lombok.Data;

@Data
public class BaseGameResponse {
	private Integer gameID;

	private String gameName;
	private Double gamePrice;
	private Double gameVersion;
	private LocalDate gameReleasedDate;
	private Integer gameDiscount;
	
	private Set<GameGenre> gameGenere;
}
