package com.zinko.bookstore.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zinko.bookstore.entity.Author;
import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.entity.Sale;
import com.zinko.bookstore.entity.SaleDTO;
import com.zinko.bookstore.entity.SaleDetail;
import com.zinko.bookstore.util.ConnectionManager;

public class SaleService {

	private static SaleService INSTANCE;

	public SaleService() {
	}

	public static SaleService getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new SaleService();
		}
		return INSTANCE;
	}

	public void add(SaleDTO saleDTO) {

		String saleInsert = "insert into sale (sale_date,sale_time,tax) values (?,?,?)";
		String saleDetailInsert = "insert into sale_detail (quantity,unit_price,book_id,sale_id,book_category_id,book_author_id) values (?,?,?,?,?,?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement saleStatement = connection.prepareStatement(saleInsert,
						Statement.RETURN_GENERATED_KEYS);
				PreparedStatement saleDetailStatement = connection.prepareStatement(saleDetailInsert)) {
			Sale sale = saleDTO.getSale();
			saleStatement.setDate(1, Date.valueOf(sale.getSaleDate()));
			saleStatement.setTime(2, Time.valueOf(sale.getSaleTime()));
			saleStatement.setInt(3, sale.getTax());

			ResultSet rs = saleStatement.getGeneratedKeys();
			while (rs.next()) {
				sale.setId(rs.getInt(1));
				List<SaleDetail> saleDetail = saleDTO.getSaleDetail();
				for (SaleDetail sd : saleDetail) {
					saleDetailStatement.setInt(1, sd.getQuantity());
					saleDetailStatement.setInt(2, sd.getUnitprice());
					saleDetailStatement.setInt(3, sd.getBook().getId());
					saleDetailStatement.setInt(4, sd.getSale().getId());
					saleDetailStatement.setInt(5, sd.getCategory().getId());
					saleDetailStatement.setInt(6, sd.getAuthor().getId());
					saleDetailStatement.addBatch();

				}
				saleDetailStatement.executeBatch();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(SaleDTO saleDTO) {

	}

	public void delete(SaleDTO saleDTO) {

	}

	public List<Sale> findSale(LocalDate datefrom, LocalDate dateto) {
		String salefind = "select * from sale where 1 = 1";
		List<Sale> saleList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(salefind);
		List<Object> params = new LinkedList<>();

		if (datefrom != null && datefrom.isBefore(dateto)) {
			sb.append("and sale_date >= ?");
			params.add(Date.valueOf(datefrom));
		}

		if (dateto != null && dateto.isAfter(datefrom)) {
			sb.append("and sale_date <= ?");
			params.add(Date.valueOf(dateto));

		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Sale sale = new Sale();
				sale.setId(rs.getInt("id"));
				sale.setSaleDate(rs.getDate("sale_date").toLocalDate());
				sale.setSaleTime(rs.getTime("sale_time").toLocalTime());
				sale.setTax(rs.getInt("tax"));

				saleList.add(sale);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleList;

	}

	public List<SaleDetail> findSaleDetail(String category, String bookName, LocalDate datefrom, LocalDate dateto) {
		String saleDetailFind = "select a.name author_name, b.name book_name, c.name category_name,\r\n"
				+ " s.tax tax, sd.id saleDetail_id, sd.book_author_id book_author_id,\r\n"
				+ " sd.book_category_id book_category_id, sd.unit_price unit_price, \r\n"
				+ " sd.quantity quantity, sd.book_id book_id, sd.sale_id sale_id,\r\n"
				+ " sum(sd.unit_price * sd.quantity + s.tax) total from \r\n"
				+ " sale_detail sd join book b on sd.book_id = b.id join sale s \r\n"
				+ " on sd.sale_id = s.id join category c on sd.book_category_id = c.id \r\n"
				+ " join author a on sd.book_author_id = a.id where 1 = 1";

		List<SaleDetail> saleDetailList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(saleDetailFind);
		List<Object> params = new LinkedList<>();

		if (null != category && !category.isEmpty()) {
			sb.append("and c.name like ?");
			params.add("%".concat(category).concat("%"));
		}

		if (bookName != null && bookName.isEmpty()) {
			sb.append("and b.name like ?");
			params.add("%".concat(bookName).concat("%"));
		}

		if (datefrom != null && datefrom.isBefore(dateto)) {
			sb.append("and s.sale_date >=?");
			params.add(Date.valueOf(datefrom));
		}

		if (null != dateto && dateto.isAfter(datefrom)) {
			sb.append("and s.sale_date <=?");
			params.add(Date.valueOf(dateto));
		}

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				SaleDetail saleDetail = new SaleDetail();
				saleDetail.setId(rs.getInt("saleDetail_id"));
				saleDetail.setQuantity(rs.getInt("quantity"));
				saleDetail.setUnitprice(rs.getInt("unit_price"));

				Sale sale = new Sale();
				sale.setId(rs.getInt("sale_id"));
				sale.setTax(rs.getInt("tax"));

				Book book = new Book();
				book.setId(rs.getInt("book_id"));
				book.setName(rs.getString("book_name"));

				Category c = new Category();
				c.setId(rs.getInt("book_category_id"));
				c.setName(rs.getString("category_name"));

				Author author = new Author();
				author.setId(rs.getInt("book_author_id"));
				author.setName(rs.getString("author_name"));

				saleDetail.setSale(sale);
				saleDetail.setBook(book);
				saleDetail.setCategory(c);
				saleDetail.setAuthor(author);
				saleDetailList.add(saleDetail);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return saleDetailList;
	}

}
