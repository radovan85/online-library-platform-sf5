package com.radovan.spring.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.LoyaltyCardRequestRepository;
import com.radovan.spring.service.LoyaltyCardService;

@Service
@Transactional
public class LoyaltyCardServiceImpl implements LoyaltyCardService {

	@Autowired
	private LoyaltyCardRequestRepository requestRepository;

	@Autowired
	private LoyaltyCardRepository cardRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Override
	public List<LoyaltyCardRequestDto> listAllCardRequests() {
		// TODO Auto-generated method stub
		List<LoyaltyCardRequestDto> returnValue = new ArrayList<LoyaltyCardRequestDto>();
		Optional<List<LoyaltyCardRequestEntity>> allRequestsOpt = Optional.ofNullable(requestRepository.findAll());
		if (!allRequestsOpt.isEmpty()) {
			allRequestsOpt.get().forEach((request) -> {
				LoyaltyCardRequestDto requestDto = tempConverter.cardRequestEntityToDto(request);
				returnValue.add(requestDto);
			});
		}
		return returnValue;
	}

	@Override
	public void authorizeRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		Optional<LoyaltyCardRequestEntity> cardRequestOpt = requestRepository.findById(cardRequestId);
		if (cardRequestOpt.isPresent()) {
			LoyaltyCardEntity cardEntity = new LoyaltyCardEntity();
			cardEntity.setDiscount(0);
			cardEntity.setPoints(0);
			Optional<CustomerEntity> customerOpt = Optional.ofNullable(cardRequestOpt.get().getCustomer());
			if (customerOpt.isPresent()) {

				CustomerEntity customerEntity = customerOpt.get();
				LoyaltyCardEntity storedCard = cardRepository.save(cardEntity);

				customerEntity.setLoyaltyCard(storedCard);
				CustomerEntity updatedCustomer = customerRepository.saveAndFlush(customerEntity);

				storedCard.setCustomer(updatedCustomer);
				cardRepository.saveAndFlush(storedCard);

				requestRepository.deleteById(cardRequestId);
				requestRepository.flush();
			}

		}

	}

	@Override
	public void rejectRequest(Integer cardRequestId) {
		// TODO Auto-generated method stub
		requestRepository.deleteById(cardRequestId);
		requestRepository.flush();
	}

	@Override
	public LoyaltyCardRequestDto addCardRequest() {
		// TODO Auto-generated method stub
		LoyaltyCardRequestDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerOpt.isPresent()) {
			LoyaltyCardRequestEntity cardRequestEntity = new LoyaltyCardRequestEntity();
			cardRequestEntity.setCustomer(customerOpt.get());
			LoyaltyCardRequestEntity storedRequest = requestRepository.save(cardRequestEntity);
			returnValue = tempConverter.cardRequestEntityToDto(storedRequest);
		}

		return returnValue;
	}

	@Override
	public LoyaltyCardRequestDto getRequestByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		LoyaltyCardRequestDto returnValue = null;
		Optional<LoyaltyCardRequestEntity> cardRequestOpt = Optional
				.ofNullable(requestRepository.findByCustomerId(customerId));
		if (cardRequestOpt.isPresent()) {
			returnValue = tempConverter.cardRequestEntityToDto(cardRequestOpt.get());
		}

		return returnValue;
	}

	@Override
	public List<LoyaltyCardDto> listAllLoyaltyCards() {
		// TODO Auto-generated method stub
		List<LoyaltyCardDto> returnValue = new ArrayList<LoyaltyCardDto>();
		Optional<List<LoyaltyCardEntity>> allCardsOpt = Optional.ofNullable(cardRepository.findAll());
		if (!allCardsOpt.isEmpty()) {
			allCardsOpt.get().forEach((card) -> {
				LoyaltyCardDto cardDto = tempConverter.loyaltyCardEntityToDto(card);
				returnValue.add(cardDto);
			});
		}
		return returnValue;
	}

	@Override
	public LoyaltyCardDto getCardByCardId(Integer cardId) {
		// TODO Auto-generated method stub
		LoyaltyCardDto returnValue = null;
		Optional<LoyaltyCardEntity> cardOpt = cardRepository.findById(cardId);
		if (cardOpt.isPresent()) {
			returnValue = tempConverter.loyaltyCardEntityToDto(cardOpt.get());
		}
		return returnValue;
	}

	@Override
	public LoyaltyCardDto updateLoyaltyCard(Integer cardId, LoyaltyCardDto card) {
		// TODO Auto-generated method stub
		LoyaltyCardEntity cardEntity = tempConverter.loyaltyCardDtoToEntity(card);
		cardEntity.setLoyalityCardId(cardId);
		LoyaltyCardEntity updatedCard = cardRepository.saveAndFlush(cardEntity);
		LoyaltyCardDto returnValue = tempConverter.loyaltyCardEntityToDto(updatedCard);
		return returnValue;
	}

	@Override
	public void deleteLoyaltyCard(Integer loyaltyCardId) {
		// TODO Auto-generated method stub
		cardRepository.deleteById(loyaltyCardId);
		cardRepository.flush();
	}

}
