package com.radovan.spring.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radovan.spring.converter.TempConverter;
import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.WishListDto;
import com.radovan.spring.entity.BookEntity;
import com.radovan.spring.entity.CartEntity;
import com.radovan.spring.entity.CartItemEntity;
import com.radovan.spring.entity.CustomerEntity;
import com.radovan.spring.entity.UserEntity;
import com.radovan.spring.entity.WishListEntity;
import com.radovan.spring.repository.BookRepository;
import com.radovan.spring.repository.CartItemRepository;
import com.radovan.spring.repository.CartRepository;
import com.radovan.spring.repository.CustomerRepository;
import com.radovan.spring.repository.WishListRepository;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CartItemService;
import com.radovan.spring.service.CartService;
import com.radovan.spring.utils.RandomStringUtil;

@Service
@Transactional
public class BookServiceImpl implements BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private TempConverter tempConverter;

	@Autowired
	private RandomStringUtil randomStringUtil;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private WishListRepository wishListRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Autowired
	private CartService cartService;

	@Autowired
	private CartItemService cartItemService;

	private DecimalFormat decfor = new DecimalFormat("0.00");

	@Override
	public BookDto addBook(BookDto book) {
		// TODO Auto-generated method stub
		Optional<Integer> bookIdOpt = Optional.ofNullable(book.getBookId());
		if (bookIdOpt.isPresent()) {
			Integer bookId = bookIdOpt.get();
			Optional<Float> avgRating = Optional.ofNullable(bookRepository.calculateAverageRating(bookId));
			if (avgRating.isPresent()) {
				book.setAverageRating(avgRating.get());
			}
		}
		Optional<String> isbn = Optional.ofNullable(book.getISBN());
		if (!isbn.isPresent()) {
			book.setISBN(randomStringUtil.getAlphaNumericString(13).toUpperCase());
		}

		BookEntity bookEntity = tempConverter.bookDtoToEntity(book);
		BookEntity storedBook = bookRepository.save(bookEntity);
		BookDto returnValue = tempConverter.bookEntityToDto(storedBook);

		if (bookIdOpt.isPresent()) {
			Optional<List<CartItemEntity>> allCartItemsOpt = Optional
					.ofNullable(cartItemRepository.findAllByBookId(returnValue.getBookId()));
			if (!allCartItemsOpt.isEmpty()) {
				allCartItemsOpt.get().forEach((itemEntity) -> {
					Float itemPrice = returnValue.getPrice() * itemEntity.getQuantity();
					if (cartItemService.hasDiscount(itemEntity.getCartItemId())) {
						itemPrice = itemPrice - ((itemPrice / 100) * 35);
					}

					itemPrice = Float.valueOf(decfor.format(itemPrice));
					itemEntity.setPrice(itemPrice);
					cartItemRepository.saveAndFlush(itemEntity);
				});

				Optional<List<CartEntity>> allCartsOpt = Optional.ofNullable(cartRepository.findAll());
				if (!allCartsOpt.isEmpty()) {
					allCartsOpt.get().forEach((cartEntity) -> {
						cartService.refreshCartState(cartEntity.getCartId());
					});
				}
			}
		}
		return returnValue;
	}

	@Override
	public BookDto getBookById(Integer bookId) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
		if (bookOpt.isPresent()) {
			returnValue = tempConverter.bookEntityToDto(bookOpt.get());
		}
		return returnValue;
	}

	@Override
	public BookDto getBookByISBN(String isbn) {
		// TODO Auto-generated method stub
		BookDto returnValue = null;
		Optional<BookEntity> bookOpt = Optional.ofNullable(bookRepository.findByISBN(isbn));
		if (bookOpt.isPresent()) {
			returnValue = tempConverter.bookEntityToDto(bookOpt.get());
		}
		return returnValue;
	}

	@Override
	public void deleteBook(Integer bookId) {
		// TODO Auto-generated method stub
		bookRepository.deleteById(bookId);
		bookRepository.flush();
	}

	@Override
	public List<BookDto> listAll() {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAll());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByGenreId(Integer genreId) {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAllByGenreId(genreId));
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public List<BookDto> search(String keyword) {
		// TODO Auto-generated method stub
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAllByKeyword(keyword));
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public void addToWishList(Integer bookId) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerOpt.isPresent()) {
			Integer customerId = customerOpt.get().getCustomerId();
			Optional<WishListEntity> wishListOpt = Optional.ofNullable(wishListRepository.findByCustomerId(customerId));
			if (wishListOpt.isPresent()) {
				Optional<BookEntity> bookOpt = bookRepository.findById(bookId);
				if (bookOpt.isPresent()) {
					WishListEntity wishListEntity = wishListOpt.get();
					BookEntity bookEntity = bookOpt.get();
					List<BookEntity> booksList = wishListEntity.getBooks();
					Optional<List<Integer>> booksIdsOpt = Optional
							.ofNullable(wishListRepository.findBookIds(wishListEntity.getWishListId()));
					if (!booksIdsOpt.isEmpty()) {
						if (!(booksIdsOpt.get().contains(bookEntity.getBookId()))) {
							booksList.add(bookEntity);
							wishListEntity.setBooks(booksList);
							wishListRepository.saveAndFlush(wishListEntity);
						}

					}

				}
			}
		}
	}

	@Override
	public List<BookDto> listAllFromWishList() {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerOpt.isPresent()) {
			Optional<WishListEntity> wishListOpt = Optional
					.ofNullable(wishListRepository.findByCustomerId(customerOpt.get().getCustomerId()));
			if (wishListOpt.isPresent()) {
				Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(wishListOpt.get().getBooks());
				if (!allBooksOpt.isEmpty()) {
					allBooksOpt.get().forEach((book) -> {
						BookDto bookDto = tempConverter.bookEntityToDto(book);
						returnValue.add(bookDto);
					});
				}
			}
		}
		return returnValue;
	}

	@Override
	public void removeFromWishList(Integer bookId) {
		// TODO Auto-generated method stub
		UserEntity authUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<CustomerEntity> customerOpt = Optional.ofNullable(customerRepository.findByUserId(authUser.getId()));
		if (customerOpt.isPresent()) {
			Optional<WishListEntity> wishListOpt = Optional
					.ofNullable(wishListRepository.findByCustomerId(customerOpt.get().getCustomerId()));
			if (wishListOpt.isPresent()) {
				WishListDto wishList = tempConverter.wishListEntityToDto(wishListOpt.get());
				List<Integer> booksIds = wishList.getBooksIds();
				booksIds.remove(Integer.valueOf(bookId));
				wishList.setBooksIds(booksIds);
				WishListEntity wishListEntity = tempConverter.wishListDtoToEntity(wishList);
				wishListRepository.saveAndFlush(wishListEntity);
			}
		}
	}

	@Override
	public List<BookDto> listAllByBookId() {
		// TODO Auto-generated method stub

		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAllSortedById());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByRating() {
		// TODO Auto-generated method stub

		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAllSortedByRating());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public List<BookDto> listAllByPrice() {
		// TODO Auto-generated method stub

		List<BookDto> returnValue = new ArrayList<BookDto>();
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAllSortedByPrice());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				BookDto bookDto = tempConverter.bookEntityToDto(book);
				returnValue.add(bookDto);
			});
		}
		return returnValue;
	}

	@Override
	public void removeBookFromAllWishlist(Integer bookId) {
		// TODO Auto-generated method stub
		bookRepository.eraseBookFromAllWishlists(bookId);
		bookRepository.flush();
	}

	@Override
	public void refreshAvgRating() {
		// TODO Auto-generated method stub
		Optional<List<BookEntity>> allBooksOpt = Optional.ofNullable(bookRepository.findAll());
		if (!allBooksOpt.isEmpty()) {
			allBooksOpt.get().forEach((book) -> {
				Optional<Float> avgRatingOpt = Optional
						.ofNullable(bookRepository.calculateAverageRating(book.getBookId()));
				if (avgRatingOpt.isPresent()) {
					book.setAverageRating(avgRatingOpt.get());
				} else {
					book.setAverageRating(null);
				}

				bookRepository.saveAndFlush(book);
			});
		}
	}

}
