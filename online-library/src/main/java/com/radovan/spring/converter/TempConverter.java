package com.radovan.spring.converter;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.CartDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.DeliveryAddressDto;
import com.radovan.spring.dto.LoyaltyCardDto;
import com.radovan.spring.dto.LoyaltyCardRequestDto;
import com.radovan.spring.dto.OrderAddressDto;
import com.radovan.spring.dto.OrderDto;
import com.radovan.spring.dto.OrderItemDto;
import com.radovan.spring.dto.PersistenceLoginDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.RoleDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.BookGenreEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.DeliveryAddressEntity;
import com.radovan.spring.entity.LoyaltyCardEntity;
import com.radovan.spring.entity.LoyaltyCardRequestEntity;
import com.radovan.spring.entity.OrderAddressEntity;
import com.radovan.spring.entity.OrderEntity;
import com.radovan.spring.entity.OrderItemEntity;
import com.radovan.spring.entity.PersistenceLoginEntity;
import com.radovan.spring.entity.ReviewEntity;
import com.radovan.spring.entity.RoleEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.repository.BookGenreRepository;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.DeliveryAddressRepository;
import com.radovan.spring.repository.LoyaltyCardRepository;
import com.radovan.spring.repository.OrderAddressRepository;
import com.radovan.spring.repository.OrderItemRepository;
import com.radovan.spring.repository.OrderRepository;
import com.radovan.spring.repository.PersistenceLoginRepository;
import com.radovan.spring.repository.ReviewRepository;
import com.radovan.spring.repository.RoleRepository;
import com.radovan.spring.repository.UserRepository;
import com.radovan.spring.repository.WishListRepository;

@Component
public class TempConverter {

	@Autowired
	private BookGenreRepository genreRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private LoyaltyCardRepository loyaltyCardRepository;

	@Autowired
	private DeliveryAddressRepository addressRepository;

	@Autowired
	private PersistenceLoginRepository persistenceRepository;

	@Autowired
	private OrderAddressRepository orderAddressRepository;

	@Autowired
	private ModelMapper mapper;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	public BookDto bookEntityToDto(BookEntity bookEntity) {
		BookDto returnValue = mapper.map(bookEntity, BookDto.class);
		Optional<BookGenreEntity> genreOpt = Optional.ofNullable(bookEntity.getGenre());
		if (genreOpt.isPresent()) {
			returnValue.setGenreId(genreOpt.get().getGenreId());
		}

		Optional<List<ReviewEntity>> reviewsOpt = Optional.ofNullable(bookEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (!reviewsOpt.isEmpty()) {
			reviewsOpt.get().forEach((review) -> {
				reviewsIds.add(review.getReviewId());
			});
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		returnValue.setReviewsIds(reviewsIds);
		return returnValue;
	}

	public BookEntity bookDtoToEntity(BookDto book) {
		BookEntity returnValue = mapper.map(book, BookEntity.class);
		Optional<Integer> genreIdOpt = Optional.ofNullable(book.getGenreId());
		if (genreIdOpt.isPresent()) {
			Integer genreId = genreIdOpt.get();
			BookGenreEntity genre = genreRepository.findById(genreId).get();
			returnValue.setGenre(genre);
		}

		Optional<List<Integer>> reviewsIdsOpt = Optional.ofNullable(book.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIdsOpt.isEmpty()) {
			reviewsIdsOpt.get().forEach((reviewId) -> {
				ReviewEntity review = reviewRepository.findById(reviewId).get();
				reviews.add(review);
			});
		}

		returnValue.setReviews(reviews);
		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public BookGenreDto bookGenreEntityToDto(BookGenreEntity genreEntity) {
		BookGenreDto returnValue = mapper.map(genreEntity, BookGenreDto.class);
		Optional<List<BookEntity>> booksOpt = Optional.ofNullable(genreEntity.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!booksOpt.isEmpty()) {
			booksOpt.get().forEach((book) -> {
				booksIds.add(book.getBookId());
			});
		}

		returnValue.setBooksIds(booksIds);
		return returnValue;

	}

	public BookGenreEntity bookGenreDtoToEntity(BookGenreDto genre) {
		BookGenreEntity returnValue = mapper.map(genre, BookGenreEntity.class);
		Optional<List<Integer>> booksIdsOpt = Optional.ofNullable(genre.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIdsOpt.isEmpty()) {
			booksIdsOpt.get().forEach((bookId) -> {
				BookEntity bookEntity = bookRepository.findById(bookId).get();
				books.add(bookEntity);
			});
		}

		returnValue.setBooks(books);
		return returnValue;
	}

	public CartDto cartEntityToDto(CartEntity cartEntity) {
		CartDto returnValue = mapper.map(cartEntity, CartDto.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartEntity.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(cartEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		List<Integer> itemsIds = new ArrayList<>();
		Optional<List<CartItemEntity>> cartItemsOpt = Optional.ofNullable(cartEntity.getCartItems());
		if (!cartItemsOpt.isEmpty()) {
			cartItemsOpt.get().forEach((itemEntity) -> {
				Integer itemId = itemEntity.getCartItemId();
				itemsIds.add(itemId);
			});
		}
		returnValue.setCartItemsIds(itemsIds);
		Float price = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(price);
		return returnValue;

	}

	public CartEntity cartDtoToEntity(CartDto cartDto) {
		CartEntity returnValue = mapper.map(cartDto, CartEntity.class);
		Optional<Float> cartPriceOpt = Optional.ofNullable(cartDto.getCartPrice());
		if (!cartPriceOpt.isPresent()) {
			returnValue.setCartPrice(0f);
		}
		Optional<Integer> customerIdOpt = Optional.ofNullable(cartDto.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		List<CartItemEntity> cartItems = new ArrayList<>();
		Optional<List<Integer>> itemsIdsOpt = Optional.ofNullable(cartDto.getCartItemsIds());

		if (!itemsIdsOpt.isEmpty()) {
			itemsIdsOpt.get().forEach((itemId) -> {
				CartItemEntity itemEntity = cartItemRepository.findById(itemId).get();
				cartItems.add(itemEntity);
			});
		}

		returnValue.setCartItems(cartItems);
		Float price = Float.valueOf(decfor.format(returnValue.getCartPrice()));
		returnValue.setCartPrice(price);
		return returnValue;
	}

	public CartItemDto cartItemEntityToDto(CartItemEntity cartItemEntity) {
		CartItemDto returnValue = mapper.map(cartItemEntity, CartItemDto.class);
		Optional<BookEntity> bookOpt = Optional.ofNullable(cartItemEntity.getBook());
		if (bookOpt.isPresent()) {
			returnValue.setBookId(bookOpt.get().getBookId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(cartItemEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);

		return returnValue;
	}

	public CartItemEntity cartItemDtoToEntity(CartItemDto cartItemDto) {
		CartItemEntity returnValue = mapper.map(cartItemDto, CartItemEntity.class);
		Optional<Integer> cartIdOpt = Optional.ofNullable(cartItemDto.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> bookIdOpt = Optional.ofNullable(cartItemDto.getBookId());
		if (bookIdOpt.isPresent()) {
			Integer bookId = bookIdOpt.get();
			BookEntity bookEntity = bookRepository.findById(bookId).get();
			returnValue.setBook(bookEntity);
		}

		Float price = Float.valueOf(decfor.format(returnValue.getPrice()));
		returnValue.setPrice(price);
		return returnValue;
	}

	public CustomerDto customerEntityToDto(CustomerEntity customerEntity) {
		CustomerDto returnValue = mapper.map(customerEntity, CustomerDto.class);
		Optional<UserEntity> userOpt = Optional.ofNullable(customerEntity.getUser());
		if (userOpt.isPresent()) {
			returnValue.setUserId(userOpt.get().getId());
		}

		Optional<CartEntity> cartOpt = Optional.ofNullable(customerEntity.getCart());
		if (cartOpt.isPresent()) {
			returnValue.setCartId(cartOpt.get().getCartId());
		}

		Optional<WishListEntity> wishListOpt = Optional.ofNullable(customerEntity.getWishList());
		if (wishListOpt.isPresent()) {
			returnValue.setWishListId(wishListOpt.get().getWishListId());
		}

		Optional<LoyaltyCardEntity> loyaltyCardOpt = Optional.ofNullable(customerEntity.getLoyaltyCard());
		if (loyaltyCardOpt.isPresent()) {
			returnValue.setLoyaltyCardId(loyaltyCardOpt.get().getLoyaltyCardId());
		}

		Optional<DeliveryAddressEntity> addressOpt = Optional.ofNullable(customerEntity.getDeliveryAddress());
		if (addressOpt.isPresent()) {
			returnValue.setDeliveryAddressId(addressOpt.get().getDeliveryAddressId());
		}

		Optional<List<ReviewEntity>> reviewsOpt = Optional.ofNullable(customerEntity.getReviews());
		List<Integer> reviewsIds = new ArrayList<Integer>();
		if (reviewsOpt.isPresent()) {
			reviewsOpt.get().forEach((review) -> {
				reviewsIds.add(review.getReviewId());
			});
		}

		returnValue.setReviewsIds(reviewsIds);

		List<Integer> persistenceLoginsIds = new ArrayList<Integer>();
		Optional<List<PersistenceLoginEntity>> persistenceLoginsOpt = Optional
				.ofNullable(customerEntity.getPersistenceLogins());
		if (!persistenceLoginsOpt.isEmpty()) {
			persistenceLoginsOpt.get().forEach((persistence) -> {
				persistenceLoginsIds.add(persistence.getId());
			});
		}

		returnValue.setPersistenceLoginsIds(persistenceLoginsIds);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> registrationDateOpt = Optional.ofNullable(customerEntity.getRegistrationDate());
		if (registrationDateOpt.isPresent()) {
			String registrationDateStr = registrationDateOpt.get().toLocalDateTime()
					.atZone(ZoneId.of("Europe/Belgrade")).format(formatter);
			returnValue.setRegistrationDateStr(registrationDateStr);
		}
		return returnValue;
	}

	public CustomerEntity customerDtoToEntity(CustomerDto customer) {
		CustomerEntity returnValue = mapper.map(customer, CustomerEntity.class);
		Optional<Integer> userIdOpt = Optional.ofNullable(customer.getUserId());
		if (userIdOpt.isPresent()) {
			Integer userId = userIdOpt.get();
			UserEntity userEntity = userRepository.findById(userId).get();
			returnValue.setUser(userEntity);
		}

		Optional<Integer> cartIdOpt = Optional.ofNullable(customer.getCartId());
		if (cartIdOpt.isPresent()) {
			Integer cartId = cartIdOpt.get();
			CartEntity cartEntity = cartRepository.findById(cartId).get();
			returnValue.setCart(cartEntity);
		}

		Optional<Integer> wishListIdOpt = Optional.ofNullable(customer.getWishListId());
		if (wishListIdOpt.isPresent()) {
			Integer wishListId = wishListIdOpt.get();
			WishListEntity wishListEntity = wishListRepository.findById(wishListId).get();
			returnValue.setWishList(wishListEntity);
		}

		Optional<Integer> loyaltyCardIdOpt = Optional.ofNullable(customer.getLoyaltyCardId());
		if (loyaltyCardIdOpt.isPresent()) {
			Integer loyaltyCardId = loyaltyCardIdOpt.get();
			LoyaltyCardEntity cardEntity = loyaltyCardRepository.findById(loyaltyCardId).get();
			returnValue.setLoyaltyCard(cardEntity);
		}

		Optional<Integer> delieryAddressIdOpt = Optional.ofNullable(customer.getDeliveryAddressId());
		if (delieryAddressIdOpt.isPresent()) {
			Integer deliveryAddressId = delieryAddressIdOpt.get();
			DeliveryAddressEntity addressEntity = addressRepository.findById(deliveryAddressId).get();
			returnValue.setDeliveryAddress(addressEntity);
		}

		Optional<List<Integer>> reviewsIdsOpt = Optional.ofNullable(customer.getReviewsIds());
		List<ReviewEntity> reviews = new ArrayList<ReviewEntity>();
		if (!reviewsIdsOpt.isEmpty()) {
			reviewsIdsOpt.get().forEach((reviewId) -> {
				ReviewEntity reviewEntity = reviewRepository.findById(reviewId).get();
				reviews.add(reviewEntity);
			});
		}

		returnValue.setReviews(reviews);

		Optional<List<Integer>> persistenceLoginsIdsOpt = Optional.ofNullable(customer.getPersistenceLoginsIds());
		List<PersistenceLoginEntity> persistenceLogins = new ArrayList<PersistenceLoginEntity>();
		if (!persistenceLoginsIdsOpt.isEmpty()) {
			persistenceLoginsIdsOpt.get().forEach((persistenceId) -> {
				PersistenceLoginEntity persistence = persistenceRepository.findById(persistenceId).get();
				persistenceLogins.add(persistence);
			});
		}

		returnValue.setPersistenceLogins(persistenceLogins);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		Optional<String> dateOfBirthStrOpt = Optional.ofNullable(customer.getDateOfBirthStr());
		if (dateOfBirthStrOpt.isPresent()) {
			LocalDate dateOfBirthLocal = LocalDate.parse(dateOfBirthStrOpt.get(), formatter);
			Timestamp dateOfBirth = Timestamp.valueOf(dateOfBirthLocal.atStartOfDay());
			returnValue.setDateOfBirth(dateOfBirth);
		}

		return returnValue;
	}

	public LoyaltyCardDto loyaltyCardEntityToDto(LoyaltyCardEntity card) {
		LoyaltyCardDto returnValue = mapper.map(card, LoyaltyCardDto.class);
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(card.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		return returnValue;
	}

	public LoyaltyCardEntity loyaltyCardDtoToEntity(LoyaltyCardDto card) {
		LoyaltyCardEntity returnValue = mapper.map(card, LoyaltyCardEntity.class);
		Optional<Integer> customerIdOpt = Optional.ofNullable(card.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderDto orderEntityToDto(OrderEntity orderEntity) {
		OrderDto returnValue = mapper.map(orderEntity, OrderDto.class);
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(orderEntity.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		Optional<OrderAddressEntity> addressOpt = Optional.ofNullable(orderEntity.getAddress());
		if (addressOpt.isPresent()) {
			returnValue.setAddressId(addressOpt.get().getAddressId());
		}

		Optional<List<OrderItemEntity>> orderedItemsOpt = Optional.ofNullable(orderEntity.getOrderedItems());
		List<Integer> orderedItemsIds = new ArrayList<Integer>();
		if (!orderedItemsOpt.isEmpty()) {
			orderedItemsOpt.get().forEach((item) -> {
				orderedItemsIds.add(item.getOrderItemId());
			});
		}

		returnValue.setOrderedItemsIds(orderedItemsIds);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime time = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = time.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public OrderEntity orderDtoToEntity(OrderDto order) {
		OrderEntity returnValue = mapper.map(order, OrderEntity.class);
		Optional<Integer> customerIdOpt = Optional.ofNullable(order.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		Optional<Integer> addressIdOpt = Optional.ofNullable(order.getAddressId());
		if (addressIdOpt.isPresent()) {
			Integer addressId = addressIdOpt.get();
			OrderAddressEntity addressEntity = orderAddressRepository.findById(addressId).get();
			returnValue.setAddress(addressEntity);
		}

		Optional<List<Integer>> orderedItemsIdsOpt = Optional.ofNullable(order.getOrderedItemsIds());
		List<OrderItemEntity> orderedItems = new ArrayList<OrderItemEntity>();
		if (!orderedItemsIdsOpt.isEmpty()) {
			orderedItemsIdsOpt.get().forEach((itemId) -> {
				OrderItemEntity item = orderItemRepository.findById(itemId).get();
				orderedItems.add(item);
			});
		}

		returnValue.setOrderedItems(orderedItems);
		return returnValue;
	}

	public OrderItemDto orderItemEntityToDto(OrderItemEntity itemEntity) {
		OrderItemDto returnValue = mapper.map(itemEntity, OrderItemDto.class);

		Optional<OrderEntity> orderOpt = Optional.ofNullable(itemEntity.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderItemEntity orderItemDtoToEntity(OrderItemDto itemDto) {
		OrderItemEntity returnValue = mapper.map(itemDto, OrderItemEntity.class);

		Optional<Integer> orderIdOpt = Optional.ofNullable(itemDto.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public ReviewDto reviewEntityToDto(ReviewEntity reviewEntity) {
		ReviewDto returnValue = mapper.map(reviewEntity, ReviewDto.class);
		Optional<CustomerEntity> authorOpt = Optional.ofNullable(reviewEntity.getAuthor());
		if (authorOpt.isPresent()) {
			returnValue.setAuthorId(authorOpt.get().getCustomerId());
		}

		Optional<BookEntity> bookOpt = Optional.ofNullable(reviewEntity.getBook());
		if (bookOpt.isPresent()) {
			returnValue.setBookId(bookOpt.get().getBookId());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(reviewEntity.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public ReviewEntity reviewDtoToEntity(ReviewDto review) {
		ReviewEntity returnValue = mapper.map(review, ReviewEntity.class);
		Optional<Integer> authorIdOpt = Optional.ofNullable(review.getAuthorId());
		if (authorIdOpt.isPresent()) {
			Integer authorId = authorIdOpt.get();
			CustomerEntity author = customerRepository.findById(authorId).get();
			returnValue.setAuthor(author);
		}

		Optional<Integer> bookIdOpt = Optional.ofNullable(review.getBookId());
		if (bookIdOpt.isPresent()) {
			Integer bookId = bookIdOpt.get();
			BookEntity bookEntity = bookRepository.findById(bookId).get();
			returnValue.setBook(bookEntity);
		}

		return returnValue;
	}

	public WishListDto wishListEntityToDto(WishListEntity wishList) {
		WishListDto returnValue = mapper.map(wishList, WishListDto.class);
		Optional<List<BookEntity>> booksOpt = Optional.ofNullable(wishList.getBooks());
		List<Integer> booksIds = new ArrayList<Integer>();
		if (!booksOpt.isEmpty()) {
			booksOpt.get().forEach((book) -> {
				booksIds.add(book.getBookId());
			});
		}

		returnValue.setBooksIds(booksIds);

		Optional<CustomerEntity> customerOpt = Optional.ofNullable(wishList.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		return returnValue;
	}

	public WishListEntity wishListDtoToEntity(WishListDto wishList) {
		WishListEntity returnValue = mapper.map(wishList, WishListEntity.class);
		Optional<List<Integer>> booksIdsOpt = Optional.ofNullable(wishList.getBooksIds());
		List<BookEntity> books = new ArrayList<BookEntity>();
		if (!booksIdsOpt.isEmpty()) {
			booksIdsOpt.get().forEach((bookId) -> {
				BookEntity bookEntity = bookRepository.findById(bookId).get();
				books.add(bookEntity);
			});
		}

		returnValue.setBooks(books);

		Optional<Integer> customerIdOpt = Optional.ofNullable(wishList.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public LoyaltyCardRequestDto cardRequestEntityToDto(LoyaltyCardRequestEntity request) {
		LoyaltyCardRequestDto returnValue = mapper.map(request, LoyaltyCardRequestDto.class);
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(request.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}
		return returnValue;
	}

	public LoyaltyCardRequestEntity cardRequestDtoToEntity(LoyaltyCardRequestDto request) {
		LoyaltyCardRequestEntity returnValue = mapper.map(request, LoyaltyCardRequestEntity.class);
		Optional<Integer> customerIdOpt = Optional.ofNullable(request.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}
		return returnValue;
	}

	public DeliveryAddressDto deliveryAddressEntityToDto(DeliveryAddressEntity address) {
		DeliveryAddressDto returnValue = mapper.map(address, DeliveryAddressDto.class);
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(address.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}
		return returnValue;
	}

	public DeliveryAddressEntity deliveryAddressDtoToEntity(DeliveryAddressDto address) {
		DeliveryAddressEntity returnValue = mapper.map(address, DeliveryAddressEntity.class);
		Optional<Integer> customerIdOpt = Optional.ofNullable(address.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}
		return returnValue;
	}

	public OrderItemEntity cartItemToOrderItemEntity(CartItemEntity cartItemEntity) {
		OrderItemEntity returnValue = mapper.map(cartItemEntity, OrderItemEntity.class);

		Optional<BookEntity> bookOpt = Optional.ofNullable(cartItemEntity.getBook());
		if (bookOpt.isPresent()) {
			returnValue.setBookName(bookOpt.get().getName());
			returnValue.setBookPrice(bookOpt.get().getPrice());
		}

		return returnValue;
	}

	public PersistenceLoginDto persistenceEntityToDto(PersistenceLoginEntity persistence) {
		PersistenceLoginDto returnValue = mapper.map(persistence, PersistenceLoginDto.class);
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(persistence.getCustomer());
		if (customerOpt.isPresent()) {
			returnValue.setCustomerId(customerOpt.get().getCustomerId());
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

		Optional<Timestamp> createdAtOpt = Optional.ofNullable(persistence.getCreatedAt());
		if (createdAtOpt.isPresent()) {
			ZonedDateTime createdAtZoned = createdAtOpt.get().toLocalDateTime().atZone(ZoneId.of("Europe/Belgrade"));
			String createdAtStr = createdAtZoned.format(formatter);
			returnValue.setCreatedAtStr(createdAtStr);
		}

		return returnValue;
	}

	public PersistenceLoginEntity persistenceDtoToEntity(PersistenceLoginDto persistence) {
		PersistenceLoginEntity returnValue = mapper.map(persistence, PersistenceLoginEntity.class);
		Optional<Integer> customerIdOpt = Optional.ofNullable(persistence.getCustomerId());
		if (customerIdOpt.isPresent()) {
			Integer customerId = customerIdOpt.get();
			CustomerEntity customerEntity = customerRepository.findById(customerId).get();
			returnValue.setCustomer(customerEntity);
		}

		return returnValue;
	}

	public OrderAddressDto orderAddressEntityToDto(OrderAddressEntity address) {
		OrderAddressDto returnValue = mapper.map(address, OrderAddressDto.class);
		Optional<OrderEntity> orderOpt = Optional.ofNullable(address.getOrder());
		if (orderOpt.isPresent()) {
			returnValue.setOrderId(orderOpt.get().getOrderId());
		}

		return returnValue;
	}

	public OrderAddressEntity orderAddressDtoToEntity(OrderAddressDto address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		Optional<Integer> orderIdOpt = Optional.ofNullable(address.getOrderId());
		if (orderIdOpt.isPresent()) {
			Integer orderId = orderIdOpt.get();
			OrderEntity orderEntity = orderRepository.findById(orderId).get();
			returnValue.setOrder(orderEntity);
		}

		return returnValue;
	}

	public OrderAddressEntity addressToOrderAddress(DeliveryAddressEntity address) {
		OrderAddressEntity returnValue = mapper.map(address, OrderAddressEntity.class);
		return returnValue;
	}

	public UserDto userEntityToDto(UserEntity userEntity) {
		UserDto returnValue = mapper.map(userEntity, UserDto.class);
		returnValue.setEnabled(userEntity.getEnabled());
		Optional<List<RoleEntity>> rolesOpt = Optional.ofNullable(userEntity.getRoles());
		List<Integer> rolesIds = new ArrayList<Integer>();

		if (!rolesOpt.isEmpty()) {
			rolesOpt.get().forEach((roleEntity) -> {
				rolesIds.add(roleEntity.getId());
			});
		}

		returnValue.setRolesIds(rolesIds);

		return returnValue;
	}

	public UserEntity userDtoToEntity(UserDto userDto) {
		UserEntity returnValue = mapper.map(userDto, UserEntity.class);
		List<RoleEntity> roles = new ArrayList<>();
		Optional<List<Integer>> rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds());

		if (!rolesIdsOpt.isEmpty()) {
			rolesIdsOpt.get().forEach((roleId) -> {
				RoleEntity role = roleRepository.findById(roleId).get();
				roles.add(role);
			});
		}

		returnValue.setRoles(roles);

		return returnValue;
	}

	public RoleDto roleEntityToDto(RoleEntity roleEntity) {
		RoleDto returnValue = mapper.map(roleEntity, RoleDto.class);
		List<UserEntity> users = roleEntity.getUsers();
		List<Integer> userIds = new ArrayList<>();

		users.forEach((user) -> {
			userIds.add(user.getId());
		});

		returnValue.setUsersIds(userIds);
		return returnValue;
	}

	public RoleEntity roleDtoToEntity(RoleDto roleDto) {
		RoleEntity returnValue = mapper.map(roleDto, RoleEntity.class);
		List<Integer> usersIds = roleDto.getUsersIds();
		List<UserEntity> users = new ArrayList<>();

		usersIds.forEach((userId) -> {
			UserEntity userEntity = userRepository.findById(userId).get();
			users.add(userEntity);
		});

		returnValue.setUsers(users);
		return returnValue;
	}
}
