package com.radovan.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.radovan.spring.dto.BookDto;
import com.radovan.spring.dto.BookGenreDto;
import com.radovan.spring.dto.CartItemDto;
import com.radovan.spring.dto.CustomerDto;
import com.radovan.spring.dto.ReviewDto;
import com.radovan.spring.dto.UserDto;
import com.radovan.spring.service.BookGenreService;
import com.radovan.spring.service.BookService;
import com.radovan.spring.service.CustomerService;
import com.radovan.spring.service.ReviewService;
import com.radovan.spring.service.UserService;

@Controller
@RequestMapping(value = "/books")
public class BookController {

	@Autowired
	private BookService bookService;

	@Autowired
	private BookGenreService genreService;

	@Autowired
	private ReviewService reviewService;

	@Autowired
	private UserService userService;

	@Autowired
	private CustomerService customerService;

	@GetMapping(value = "/allBooks")
	public String allBooksList(ModelMap map) {
		List<BookDto> allBooks = bookService.listAll();
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/bookList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allBooksById")
	public String allBooksListSortedById(ModelMap map) {
		List<BookDto> allBooks = bookService.listAllByBookId();
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/bookList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allBooksByRating")
	public String allBooksListSortedByRating(ModelMap map) {
		List<BookDto> allBooks = bookService.listAllByRating();
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/bookList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/allBooksByPrice")
	public String allBooksListSortedByPrice(ModelMap map) {
		List<BookDto> allBooks = bookService.listAllByPrice();
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/bookList :: ajaxLoadedContent";
	}

	@GetMapping(value = "/bookDetails/{bookId}")
	public String getBookDetails(@PathVariable("bookId") Integer bookId, ModelMap map) {
		BookDto currentBook = bookService.getBookById(bookId);
		List<BookGenreDto> allGenres = genreService.listAll();
		List<ReviewDto> allReviews = reviewService.listAllByBookId(bookId);
		List<UserDto> allUsers = userService.listAllUsers();
		List<CustomerDto> allCustomers = customerService.getAllCustomers();
		map.put("currentBook", currentBook);
		map.put("allGenres", allGenres);
		map.put("allReviews", allReviews);
		map.put("allUsers", allUsers);
		map.put("allCustomers", allCustomers);
		map.put("recordsPerPage", 5);
		return "fragments/bookDetails :: ajaxLoadedContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@PostMapping(value = "/addToWishList/{bookId}")
	public String toWishlist(@PathVariable("bookId") Integer bookId) {
		bookService.addToWishList(bookId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/getWishList")
	public String getWishListBooks(ModelMap map) {
		List<BookDto> allBooks = bookService.listAllFromWishList();
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/wishlist :: ajaxLoadedContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/deleteFromWishList/{bookId}")
	public String deleteFromWishList(@PathVariable("bookId") Integer bookId) {
		bookService.removeFromWishList(bookId);
		return "fragments/homePage :: ajaxLoadedContent";
	}

	@PreAuthorize(value = "hasAuthority('ROLE_USER')")
	@GetMapping(value = "/addToCart/{bookId}")
	public String addBookToCart(@PathVariable("bookId") Integer bookId, ModelMap map) {
		CartItemDto cartItem = new CartItemDto();
		BookDto selectedBook = bookService.getBookById(bookId);
		map.put("selectedBook", selectedBook);
		map.put("cartItem", cartItem);
		return "fragments/cartItemForm :: ajaxLoadedContent";
	}

	@GetMapping(value = "/searchBooks")
	public String searchBooks(@RequestParam("keyword") String keyword, ModelMap map) {
		List<BookDto> allBooks = bookService.search(keyword);
		map.put("allBooks", allBooks);
		map.put("recordsPerPage", 5);
		return "fragments/searchList :: ajaxLoadedContent";
	}
}
