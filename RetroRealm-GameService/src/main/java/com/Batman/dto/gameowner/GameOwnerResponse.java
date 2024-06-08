package com.Batman.dto.gameowner;

import java.util.List;

import com.Batman.dto.game.BaseGameResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameOwnerResponse extends BaseGameOwnerResponse {
	private List<BaseGameResponse> games;
}
