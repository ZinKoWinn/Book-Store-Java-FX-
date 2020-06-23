package com.zinko.bookstore.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Sale {

	private int id;
	private LocalDate saleDate;
	private LocalTime saleTime;
	private int tax;

	public int getId() {
		return id;
	}

	public LocalDate getSaleDate() {
		return saleDate;
	}

	public LocalTime getSaleTime() {
		return saleTime;
	}

	public int getTax() {
		return tax;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setSaleDate(LocalDate saleDate) {
		this.saleDate = saleDate;
	}

	public void setSaleTime(LocalTime saleTime) {
		this.saleTime = saleTime;
	}

	public void setTax(int tax) {
		this.tax = tax;
	}

}
