package com.radovan.spring.dto;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class BookDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer bookId;

	private String ISBN;
	private String name;
	private String publisher;
	private String author;
	private String description;
	private String language;
	private Integer publishedYear;
	private Integer pageNumber;
	private Float price;
	private Float averageRating;
	private String imageUrl;
	private String cover;
	private String letter;
	private Integer genreId;
	private List<Integer> reviewsIds;
	private LinkedHashMap<Integer, String> letters;
	private LinkedHashMap<Integer, String> covers;

	public BookDto() {
		letters = new LinkedHashMap<Integer, String>();
		letters.put(1, "Han");
		letters.put(2, "Latin");
		letters.put(3, "Cyrillic");

		covers = new LinkedHashMap<Integer, String>();
		covers.put(1, "Paperback");
		covers.put(2, "Hardcover");
	}

	public Integer getBookId() {
		return bookId;
	}

	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Integer getPublishedYear() {
		return publishedYear;
	}

	public void setPublishedYear(Integer publishedYear) {
		this.publishedYear = publishedYear;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(Float averageRating) {
		this.averageRating = averageRating;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getLetter() {
		return letter;
	}

	public void setLetter(String letter) {
		this.letter = letter;
	}

	public Integer getGenreId() {
		return genreId;
	}

	public void setGenreId(Integer genreId) {
		this.genreId = genreId;
	}

	public List<Integer> getReviewsIds() {
		return reviewsIds;
	}

	public void setReviewsIds(List<Integer> reviewsIds) {
		this.reviewsIds = reviewsIds;
	}

	public LinkedHashMap<Integer, String> getLetters() {
		return letters;
	}

	public void setLetters(LinkedHashMap<Integer, String> letters) {
		this.letters = letters;
	}

	public LinkedHashMap<Integer, String> getCovers() {
		return covers;
	}

	public void setCovers(LinkedHashMap<Integer, String> covers) {
		this.covers = covers;
	}

}
