package com.Batman.service.impl;

import java.util.Optional;

import com.batman.exception.wrapper.InputFieldException;
import com.batman.helpers.CommonMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.Batman.dto.gameowner.GameOwnerRequest;
import com.Batman.dto.gameowner.GameOwnerResponse;
import com.Batman.entity.GameOwner;
import com.Batman.exception.wrapper.AlreadyExistsException;
import com.Batman.exception.wrapper.GameOwnerNotFoundException;
import com.Batman.exception.wrapper.VerificationMissingException;
import com.Batman.repository.IGameOwnerRepository;
import com.Batman.service.IGameOwnerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service("GameOwnerService")
@RequiredArgsConstructor
@Transactional
@Slf4j
public class GameOwnerService implements IGameOwnerService {

	private final CommonMapper mapper;

	private final IGameOwnerRepository gameOwnerRepository;

	@Override
	public GameOwnerResponse registerOwner(GameOwnerRequest gameOwnerRequest, BindingResult bindingResult) {
		log.info("Entering registerOwner in GameOwner Service ..." );
		if (bindingResult.hasErrors()) {
			log.error("Game Owner Request Has Binding Error...");
			String errorMessage = Optional.ofNullable(bindingResult.getFieldError()).map(FieldError::getDefaultMessage).orElse("Unknown Error in GameOwner Request");
			throw new InputFieldException(errorMessage);
		}
		if (!gameOwnerRequest.getIsVerified())
			throw new VerificationMissingException("NOT_VERIFIED_USER");
		Optional<GameOwner> gameOwnerByMail = gameOwnerRepository.findByEmail(gameOwnerRequest.getEmail());
		if(gameOwnerByMail.isPresent())
			throw new AlreadyExistsException("USER_ALREADY_EXISTS");

		GameOwner gameOwner = mapper.convertToEntity(gameOwnerRequest, GameOwner.class);

		GameOwner savedGameowner = gameOwnerRepository.save(gameOwner);

		GameOwnerResponse gameOwnerResponse = mapper.convertToResponse(savedGameowner, GameOwnerResponse.class);
		log.info("Entering registerOwner in GameOwner Service ..." );
		return gameOwnerResponse;
	}

	@Override
	public GameOwnerResponse getGameOwnerById(Integer id) {
		GameOwner gameOwner =   gameOwnerRepository.findById(id).orElseThrow(()-> new GameOwnerNotFoundException("OWNER_NOT_FOUND_FOR_ID"));
		return mapper.convertToResponse(gameOwner, GameOwnerResponse.class);
	}

}
