package com.radovan.spring.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "books")
public class BookEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "book_id")
	private Integer bookId;

	@Column(nullable = false, length = 13)
	private String ISBN;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false, length = 40)
	private String publisher;

	@Column(nullable = false, length = 40)
	private String author;

	@Column(nullable = false, length = 90)
	private String description;

	@Column(nullable = false, length = 30)
	private String language;

	@Column(name = "published_year", nullable = false)
	private Integer publishedYear;

	@Column(name = "page_number", nullable = false)
	private Integer pageNumber;

	@Column(nullable = false)
	private Float price;

	@Column(name = "average_rating")
	private Float averageRating;

	@Column(name = "image_url", nullable = false, length = 255)
	private String imageUrl;

	@Column(nullable = false, length = 30)
	private String cover;

	@Column(nullable = false, length = 30)
	private String letter;

	@ManyToOne
	@JoinColumn(name = "genre_id")
	private BookGenreEntity genre;

	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "book")
	private List<ReviewEntity> reviews;

	@Transient
	private LinkedHashMap<Integer, String> letters;

	@Transient
	private LinkedHashMap<Integer, String> covers;

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

	public BookGenreEntity getGenre() {
		return genre;
	}

	public void setGenre(BookGenreEntity genre) {
		this.genre = genre;
	}

	public List<ReviewEntity> getReviews() {
		return reviews;
	}

	public void setReviews(List<ReviewEntity> reviews) {
		this.reviews = reviews;
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
