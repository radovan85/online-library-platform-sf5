package com.radovan.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.service.DeliveryAddressService;

@Controller
@RequestMapping(value = "/addresses")
public class DeliveryAddressController {

	@Autowired
	private DeliveryAddressService addressService;

	@GetMapping(value = "/updateAddress/{addressId}")
	public String renderAddressForm(@PathVariable("addressId") Integer addressId, ModelMap map) {
		DeliveryAddressDto address = new DeliveryAddressDto();
		DeliveryAddressDto currentAddress = addressService.getAddressById(addressId);
		map.put("address", address);
		map.put("currentAddress", currentAddress);
		return "fragments/updateAddressForm :: ajaxLoadedContent";
	}

	@PostMapping(value = "/createAddress")
	public String createAddress(@ModelAttribute("address") DeliveryAddressDto address) {
		addressService.createAddress(address);
		return "fragments/homePage :: ajaxLoadedContent";
	}
}
