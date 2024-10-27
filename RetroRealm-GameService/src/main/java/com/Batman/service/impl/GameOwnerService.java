package com.Batman.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;
import com.Batman.entity.GameOwner;
import com.Batman.exception.wrapper.AlreadyExistsException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.InputFieldException;
import com.Batman.exception.wrapper.VerificationMissingException;
import com.Batman.helper.CommonMapper;
import com.Batman.repository.IGameOwnerRepository;
import com.Batman.service.IGameOwnerService;

import lombok.RequiredArgsConstructor;

@Service("GameOwnerService")
@RequiredArgsConstructor
@Transactional
public class GameOwnerService implements IGameOwnerService {

	private final CommonMapper mapper;

	private final IGameOwnerRepository gameOwnerRepository;

	@Override
	public GameOwnerResponse registerOwner(GameOwnerRequest gameOwnerRequest, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InputFieldException(bindingResult.getFieldError().getDefaultMessage());
		}
		if (!gameOwnerRequest.getIsVerified())
			throw new VerificationMissingException("NOT_VERIFIED_USER");
		Optional<GameOwner> gameOwnerByMail = gameOwnerRepository.findByEmail(gameOwnerRequest.getEmail());
		if(gameOwnerByMail.isPresent())
			throw new AlreadyExistsException("USER_ALREADY_EXISTS");

		GameOwner gameOwner = mapper.convertToEntity(gameOwnerRequest, GameOwner.class);

		GameOwner savedGameowner = gameOwnerRepository.save(gameOwner);

		return mapper.convertToResponse(savedGameowner, GameOwnerResponse.class);
	}

	@Override
	public GameOwnerResponse getGameOwnerById(Integer id) {
		GameOwner gameOwner =   gameOwnerRepository.findById(id).orElseThrow(()-> new GameOwnerNotFoundException("OWNER_NOT_FOUND_FOR_ID"));
		return mapper.convertToResponse(gameOwner, GameOwnerResponse.class);
	}

}
