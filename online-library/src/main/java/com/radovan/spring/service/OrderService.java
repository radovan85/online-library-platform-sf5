package com.radovan.spring.service;

import java.util.List;

import com.radovan.spring.dto.OrderDto;

public interface OrderService {

	OrderDto addOrder();

	List<OrderDto> listAll();

	Float calculateOrderTotal(Integer orderId);

	OrderDto getOrder(Integer orderId);

	void deleteOrder(Integer orderId);
	
	Integer getBookQuantity(Integer orderId);
	
	OrderDto refreshOrder(Integer orderId,OrderDto order);
	
	List<OrderDto> listAllByCustomerId(Integer customerId);
	
	Float calculateOrdersValue(Integer customerId);
}
