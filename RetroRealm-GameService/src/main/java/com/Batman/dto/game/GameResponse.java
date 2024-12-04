package com.Batman.dto.game;

import com.Batman.dto.gameowner.BaseGameOwnerResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameResponse extends BaseGameResponse{
	private BaseGameOwnerResponse gameOwner;
}
