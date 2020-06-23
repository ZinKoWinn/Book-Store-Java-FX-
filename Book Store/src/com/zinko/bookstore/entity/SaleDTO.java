package com.zinko.bookstore.entity;

import java.util.List;

public class SaleDTO {
	private Sale sale;
	private List<SaleDetail> saleDetail;

	public Sale getSale() {
		return sale;
	}

	public List<SaleDetail> getSaleDetail() {
		return saleDetail;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}

	public void setSaleDetail(List<SaleDetail> saleDetail) {
		this.saleDetail = saleDetail;
	}

}
