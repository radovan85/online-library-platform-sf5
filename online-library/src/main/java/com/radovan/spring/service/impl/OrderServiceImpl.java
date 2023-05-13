package com.radovan.spring.service.impl;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.service.CartService;
import com.radovan.spring.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private CartService cartService;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	public OrderDto addOrder() {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOptional = Optional
				.ofNullable(customerRepository.findByUserId(authUser.getId()));
		CustomerEntity customerEntity = null;
		OrderEntity orderEntity = new OrderEntity();
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (customerOptional.isPresent()) {
			customerEntity = customerOptional.get();
			Optional<CartEntity> cartOptional = Optional.ofNullable(customerEntity.getCart());
			if (cartOptional.isPresent()) {
				CartEntity cartEntity = cartOptional.get();
				Optional<List<CartItemEntity>> allCartItemsOpt = Optional.ofNullable(cartEntity.getCartItems());
				if (!allCartItemsOpt.isEmpty()) {
					for (CartItemEntity cartItem : allCartItemsOpt.get()) {
						OrderItemEntity orderItem = tempConverter.cartItemToOrderItemEntity(cartItem);
						orderedItems.add(orderItem);
					}

					cartItemRepository.removeAllByCartId(cartEntity.getCartId());
					cartService.refreshCartState(cartEntity.getCartId());

					DeliveryAddressEntity deliveryAddress = customerEntity.getDeliveryAddress();
					OrderAddressEntity orderAddress = tempConverter.addressToOrderAddress(deliveryAddress);
					OrderAddressEntity storedOrderAddress = orderAddressRepository.save(orderAddress);

					orderEntity.setCustomer(customerEntity);
					orderEntity.setAddress(storedOrderAddress);
					Optional<Integer> discountOpt = Optional.ofNullable(orderEntity.getDiscount());
					if (!discountOpt.isPresent()) {
						orderEntity.setDiscount(0);
					}

					ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneId.of("Europe/Belgrade"));
					Timestamp createdAt = new Timestamp(currentTime.toInstant().getEpochSecond() * 1000L);
					orderEntity.setCreatedAt(createdAt);
					orderEntity.setBookQuantity(0);
					orderEntity.setOrderPrice(0f);
					OrderEntity storedOrder = orderRepository.save(orderEntity);

					for (OrderItemEntity orderItem : orderedItems) {
						orderItem.setOrder(storedOrder);
						orderItemRepository.save(orderItem);
					}

					orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId());
					Optional<Float> orderPriceOpt = Optional
							.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId()));
					if (orderPriceOpt.isPresent()) {
						Float orderPrice = Float.valueOf(decfor.format(orderPriceOpt.get()));
						storedOrder.setOrderPrice(orderPrice);
					}

					storedOrderAddress.setOrder(storedOrder);
					orderAddressRepository.saveAndFlush(storedOrderAddress);

					Integer bookQuantity = getBookQuantity(storedOrder.getOrderId());
					storedOrder.setBookQuantity(bookQuantity);
					storedOrder.setOrderedItems(orderedItems);
					storedOrder = orderRepository.saveAndFlush(storedOrder);
					returnValue = tempConverter.orderEntityToDto(storedOrder);

				}
			}
		}

		return returnValue;
	}

	@Override
	public List<OrderDto> listAll() {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrdersOpt = Optional.ofNullable(orderRepository.findAll());
		if (!allOrdersOpt.isEmpty()) {
			allOrdersOpt.get().forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}
		return returnValue;
	}

	@Override
	public Float calculateOrderTotal(Integer orderId) {
		// TODO Auto-generated method stub
		Float returnValue = null;
		Optional<Float> orderTotalOpt = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId));
		if (orderTotalOpt.isPresent()) {
			returnValue = Float.valueOf(decfor.format(orderTotalOpt.get()));
		}
		return returnValue;
	}

	@Override
	public OrderDto getOrder(Integer orderId) {
		// TODO Auto-generated method stub
		OrderDto returnValue = null;
		Optional<OrderEntity> orderOpt = orderRepository.findById(orderId);
		if (orderOpt.isPresent()) {
			returnValue = tempConverter.orderEntityToDto(orderOpt.get());
		}
		return returnValue;
	}

	@Override
	public void deleteOrder(Integer orderId) {
		// TODO Auto-generated method stub
		orderRepository.deleteById(orderId);
		orderRepository.flush();
	}

	@Override
	public Integer getBookQuantity(Integer orderId) {
		// TODO Auto-generated method stub
		Integer returnValue = null;
		Optional<Integer> bookQuantityOpt = Optional.ofNullable(orderItemRepository.findBookQuantity(orderId));
		if (bookQuantityOpt.isPresent()) {
			returnValue = bookQuantityOpt.get();
		}
		return returnValue;
	}

	@Override
	public OrderDto refreshOrder(Integer orderId, OrderDto order) {
		// TODO Auto-generated method stub

		OrderEntity orderEntity = tempConverter.orderDtoToEntity(order);
		orderEntity.setOrderId(orderId);
		OrderEntity storedOrder = orderRepository.saveAndFlush(orderEntity);
		OrderDto returnValue = tempConverter.orderEntityToDto(storedOrder);
		return returnValue;
	}

	@Override
	public List<OrderDto> listAllByCustomerId(Integer customerId) {
		// TODO Auto-generated method stub
		List<OrderDto> returnValue = new ArrayList<OrderDto>();
		Optional<List<OrderEntity>> allOrdersOpt = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId));
		if (!allOrdersOpt.isEmpty()) {
			allOrdersOpt.get().forEach((order) -> {
				OrderDto orderDto = tempConverter.orderEntityToDto(order);
				returnValue.add(orderDto);
			});
		}
		return returnValue;
	}

	@Override
	public Float calculateOrdersValue(Integer customerId) {
		// TODO Auto-generated method stub
		Float returnValue = 0f;
		Optional<Float> ordersAmountOpt = Optional.ofNullable(orderRepository.getOrdersValue(customerId));
		if (ordersAmountOpt.isPresent()) {
			returnValue = ordersAmountOpt.get();
		}
		return returnValue;
	}

}
