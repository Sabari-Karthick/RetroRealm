package com.Batman.service;

import org.springframework.validation.BindingResult;

import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;

public interface IGameOwnerService {
      GameOwnerResponse registerOwner(GameOwnerRequest gameOwnerRequest,BindingResult bindingResult);
      GameOwnerResponse getGameOwnerById(Integer id);
}
