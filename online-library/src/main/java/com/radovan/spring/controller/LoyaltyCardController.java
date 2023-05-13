package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.LoyaltyCardService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping("/loyaltyCards")
public class LoyaltyCardController {

	@Autowired
	private LoyaltyCardService loyaltyCardService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@PostMapping(value = "/createCardRequest")
	public String createCardRequest() {
		loyaltyCardService.addCardRequest();
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@GetMapping(value = "/cardRequestSent")
	public String cardRequestSent() {
		return "fragments/loyaltyCardRequestSent :: ajaxLoadedContent";
	}

	@GetMapping(value = "/cardInfo")
	public String loyaltyCardInfo(ModelMap map) {
		UserDto authUser = userService.getCurrentUser();
		CustomerDto customer = customerService.getCustomerByUserId(authUser.getId());
		LoyaltyCardRequestDto cardRequest = loyaltyCardService.getRequestByCustomerId(customer.getCustomerId());
		List<LoyaltyCardDto> allCards = loyaltyCardService.listAllLoyaltyCards();
		map.put("customer", customer);
		map.put("cardRequest", cardRequest);
		map.put("allCards", allCards);
		return "fragments/loyaltyCardDetails :: ajaxLoadedContent";
	}
}
