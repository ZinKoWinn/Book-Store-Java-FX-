package com.zinko.bookstore.entity;

public class SaleDetail {

	private int id;
	private Category category;
	private Book book;
	private Author author;
	private Sale sale;
	private int unitprice;
	private int quantity;
	private int tax;
	private String bookName;
	private int bookId;
	private boolean delete;

	public int getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public Book getBook() {
		return book;
	}

	public Author getAuthor() {
		return author;
	}

	public Sale getSale() {
		return sale;
	}

	public int getUnitprice() {
		return unitprice;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getTax() {
		return tax;
	}

	public String getBookName() {
		return bookName;
	}

	public int getBookId() {
		return bookId;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public void setUnitprice(int unitprice) {
		this.unitprice = unitprice;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

	public void setBookName(String bookName) {
		this.bookName = bookName;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public String getCategoryName() {
		return category.getName();
	}

	public String getAuthorName() {
		return author.getName();
	}

	public int getSaleTax() {
		return sale.getTax();
	}

	public int getSubTotal() {
		return getUnitprice() * getQuantity();
	}

	public int getTotal() {
		return getSubTotal() + getTax();
	}
}
