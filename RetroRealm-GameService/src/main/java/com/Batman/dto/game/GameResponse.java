package com.Batman.dto.game;

import com.Batman.dto.gameowner.BaseGameOwnerResponse;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GameResponse extends BaseGameResponse{
	private BaseGameOwnerResponse gameOwner;
}
