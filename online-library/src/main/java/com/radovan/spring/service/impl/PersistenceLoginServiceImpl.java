package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.PersistenceLoginEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.PersistenceLoginRepository;
import com.radovan.spring.service.PersistenceLoginService;

@Service
@Transactional
public class PersistenceLoginServiceImpl implements PersistenceLoginService {

	@Autowired
	private PersistenceLoginRepository persistenceRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public PersistenceLoginDto addPersistenceLogin() {
		// TODO Auto-generated method stub

		PersistenceLoginDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerOpt.isPresent()) {
			PersistenceLoginEntity persistence = new PersistenceLoginEntity();
			persistence.setCustomer(customerOpt.get());
			ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
			Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
			persistence.setCreatedAt(createdAt);
			PersistenceLoginEntity storedPersistence = persistenceRepository.save(persistence);
			returnValue = tempConverter.persistenceEntityToDto(storedPersistence);
		}

		return returnValue;
	}

	@Override
	public PersistenceLoginDto getLastLogin(Integer customerId) {
		// TODO Auto-generated method stub
		PersistenceLoginDto returnValue = null;
		Optional<CustomerEntity> customerOpt = customerRepository.findById(customerId);
		if (customerOpt.isPresent()) {
			Optional<PersistenceLoginEntity> persistenceOpt = Optional
					.ofNullable(persistenceRepository.findLastLogin(customerOpt.get().getCustomerId()));
			if (persistenceOpt.isPresent()) {
				returnValue = tempConverter.persistenceEntityToDto(persistenceOpt.get());
			}
		}
		return returnValue;
	}

	@Override
	public void clearCustomerLogins(Integer customerId) {
		// TODO Auto-generated method stub
		persistenceRepository.clearCustomerLogins(customerId);
		persistenceRepository.flush();
	}

}
