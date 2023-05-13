window.onload = redirectHome;

function redirectLogin() {
	$("#ajaxLoadedContent").load("/login");
}

function redirectRegister() {
	$("#ajaxLoadedContent").load("/userRegistration");
}

function redirectAbout() {
	$("#ajaxLoadedContent").load("/aboutUs");
}

function redirectHome() {
	$("#ajaxLoadedContent").load("/home");
}

function redirectAllBooks() {
	$("#ajaxLoadedContent").load("/books/allBooks");
}

function redirectAllBooksById() {
	$("#ajaxLoadedContent").load("/books/allBooksById");
}

function redirectAllBooksByRating() {
	$("#ajaxLoadedContent").load("/books/allBooksByRating");
}

function redirectAllBooksByPrice() {
	$("#ajaxLoadedContent").load("/books/allBooksByPrice");
}

function redirectBookForm() {
	$("#ajaxLoadedContent").load("/admin/createBook");
}

function redirectAllGenres() {
	$("#ajaxLoadedContent").load("/admin/allGenres");
}

function redirectGenreForm() {
	$("#ajaxLoadedContent").load("/admin/createGenre");
}

function redirectUpdateGenre(genreId) {
	$("#ajaxLoadedContent").load("/admin/updateGenre/" + genreId);
}

function redirectUpdateBook(bookId) {
	$("#ajaxLoadedContent").load("/admin/updateBook/" + bookId);
}

function viewBookDetails(bookId) {
	$("#ajaxLoadedContent").load("/books/bookDetails/" + bookId);
}

function addReview(bookId) {
	$("#ajaxLoadedContent").load("/reviews/createReview/" + bookId);
}

function redirectAllReviews() {
	$("#ajaxLoadedContent").load("/admin/allReviews");
}

function redirectAllRequestedReviews() {
	$("#ajaxLoadedContent").load("/admin/allRequestedReviews");
}

function redirectPendingReview(reviewId) {
	$("#ajaxLoadedContent").load("/admin/pendingReviewDetails/" + reviewId);
}

function redirectReviewDetails(reviewId) {
	$("#ajaxLoadedContent").load("/admin/reviewDetails/" + reviewId);
}

function redirectWishlist() {
	$("#ajaxLoadedContent").load("/books/getWishList");
}

function redirectLoyaltyCardInfo() {
	$("#ajaxLoadedContent").load("/loyaltyCards/cardInfo");
}

function redirectCardRequestSent() {
	$("#ajaxLoadedContent").load("/loyaltyCards/cardRequestSent");
}

function redirectAllCards() {
	$("#ajaxLoadedContent").load("/admin/getAllCards");
}

function redirectAllCardRequests() {
	$("#ajaxLoadedContent").load("/admin/checkCardRequests");
}

function addToCart(bookId) {
	$("#ajaxLoadedContent").load("/books/addToCart/" + bookId);
}

function redirectItemAdded() {
	$("#ajaxLoadedContent").load("/cart/addItemCompleted");
}

function redirectCart() {
	$("#ajaxLoadedContent").load("/cart/getCart");
}

function redirectProcessOrder() {
	$("#ajaxLoadedContent").load("/orders/processOrder");
}

function redirectAllOrders() {
	$("#ajaxLoadedContent").load("/admin/allOrders");
}

function redirectAllCustomerOrders(customerId) {
	$("#ajaxLoadedContent").load("/admin/allOrders/" + customerId);
}

function redirectOrderDetails(orderId) {
	$("#ajaxLoadedContent").load("/admin/getOrder/" + orderId);
}

function redirectAllCustomers() {
	$("#ajaxLoadedContent").load("/admin/allCustomers");
}

function redirectCustomerDetails(customerId) {
	$("#ajaxLoadedContent").load("/admin/getCustomer/" + customerId);
}

function redirectAccountDetails() {
	$("#ajaxLoadedContent").load("/accountInfo");
}

function redirectUpdateAddress(addressId) {
	$("#ajaxLoadedContent").load("/addresses/updateAddress/" + addressId);
}

function redirectLogout() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loggedout"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Logout error!");
	})
}

function confirmLoginPass() {
	$.ajax({
		url : "http://localhost:8080/loginPassConfirm",
		type : "POST"
	})
	.done(function(){
		checkForSuspension();
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/loginErrorPage");
	})
}

function checkForSuspension() {
	$.ajax({
		url : "http://localhost:8080/suspensionChecker",
		type : "POST"
	})
	.done(function(){
		window.location.href = "/";
	})
	.fail(function(){
		alert("Account suspended!");
		redirectLogout();
	})
}

function deleteGenre(genreId) {
	if (confirm("Are you sure you want to delete this genre?\nIt will affect all related books!")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteGenre/" + genreId
		})
		.done(function(){
			redirectAllGenres();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteReview(reviewId) {
	if (confirm("Are you sure you want to reject this review?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/rejectReview/" + reviewId
		})
		.done(function(){
			redirectAllRequestedReviews();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function deleteBook(bookId) {
	if (confirm("Are you sure you want to delete this book?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteBook/" + bookId
		})
		.done(function(){
			redirectAllBooks();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function authorizeReview(reviewId) {
	if (confirm("Are you sure you want to authorize this review?")) {
		$.ajax({
			type : "POST",
			url : "http://localhost:8080/admin/authorizeReview/" + reviewId
		})
		.done(function(){
			redirectAllReviews();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};

function addToWishlist(bookId) {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/books/addToWishList/" + bookId
	})
	.done(function(){
		alert("Book added to wish list!");
	})
	.fail(function(){
		alert("Failed!");
	})
}

function removeFromWishlist(bookId) {
	if (confirm("Remove this book from wish list?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/books/deleteFromWishList/" + bookId
		})
		.done(function(){
			redirectWishlist();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function getLoyaltyCard() {
	$.ajax({
		type : "POST",
		url : "http://localhost:8080/loyaltyCards/createCardRequest"
	})
	.done(function(){
		redirectCardRequestSent();
	})
	.fail(function(){
		alert("Failed!");
	})
}

function authorizeCard(requestId) {
	if (confirm("Authorize loyalty card for this customer?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/authorizeCard/" + requestId
		})
		.done(function(){
			redirectAllCardRequests();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function denyCard(requestId) {
	if (confirm("Deny loyalty card for this customer?")) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/rejectCard/" + requestId
		})
		.done(function(){
			redirectAllCardRequests();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function eraseItem(cartId, itemId) {
	if (confirm('Remove this item from cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteItem/" + cartId + "/" + itemId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function eraseAllItems(cartId) {
	if (confirm('Are you sure you want to clear your cart?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/cart/deleteAllItems/" + cartId
		})
		.done(function(){
			redirectCart();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function deleteOrder(orderId) {
	if (confirm('Are you sure you want to clear this order?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteOrder/" + orderId
		})
		.done(function(){
			redirectAllOrders();
		})
		.fail(function(){
			lert("Failed!");
		})
	}
}

function suspendUser(userId) {
	if (confirm('Are you sure you want to suspend this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/suspendUser/" + userId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function reactivateUser(userId) {
	if (confirm('Are you sure you want to reactivate this user?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/reactivateUser/" + userId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
}

function redirectOrderConfirmation(cartId) {
	$.ajax({
		url : "http://localhost:8080/orders/confirmOrder/" + cartId,
		type: "GET"
	})
	.done(function(){
		$("#ajaxLoadedContent").load("orders/confirmOrder/" + cartId);
	})
	.fail(function(){
		$("#ajaxLoadedContent").load("/cart/invalidCart");
	})
};


function deleteCustomer(customerId) {
	if (confirm('Are you sure you want to remove this customer?')) {
		$.ajax({
			type : "GET",
			url : "http://localhost:8080/admin/deleteCustomer/" + customerId
		})
		.done(function(){
			redirectAllCustomers();
		})
		.fail(function(){
			alert("Failed!");
		})
	}
};





