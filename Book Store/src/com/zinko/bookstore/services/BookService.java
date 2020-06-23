package com.zinko.bookstore.services;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zinko.bookstore.entity.Author;
import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.util.ConnectionManager;

public class BookService {

	private static BookService INSTANCE;

	private static final String find = "select book.id, book.name book_name, book.price price, "
			+ "book.release_date release_date, book.remark remark, category.id category_id,"
			+ " category.name category_name, author.id author_id, author.name author_name "
			+ "from book book join category category on book.category_id = category.id "
			+ "join author author on book.author_id = author.id where 1 = 1\r\n" + "";

	public BookService() {
	}

	public static BookService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BookService();
		}
		return INSTANCE;
	}

	public void add(Book b) {
		String insert = "insert into book (name,price,release_date,remark,author_id,category_id) values (?,?,?,?,?,?)";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(insert)) {
			statement.setString(1, b.getName());
			statement.setInt(2, (b.getPrice()));
			statement.setDate(3, Date.valueOf(b.getReleaseDate()));
			statement.setString(4, b.getRemark());
			statement.setInt(5, b.getAuthor().getId());
			statement.setInt(6, b.getCategory().getId());

			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(Book b) {
		String update = "update book set name = ?,price = ?,release_date = ?,remark = ?,category_id = ?,author_id = ? where id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(update)) {
			statement.setString(1, b.getName());
			statement.setInt(2, (b.getPrice()));
			statement.setDate(3, Date.valueOf(b.getReleaseDate()));
			statement.setString(4, b.getRemark());
			statement.setInt(5, b.getCategory().getId());
			statement.setInt(6, b.getAuthor().getId());
			statement.setInt(7, b.getId());

			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void imageUpload(Book b) {
		String image_url = "update book set image = ? where id= ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(image_url)) {
			statement.setString(1, b.getImage());
			statement.setInt(2, b.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getImage(Book b) {
		String image_url = "select image from book where id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(image_url)) {
			statement.setInt(1, b.getId());
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				return rs.getString("image");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(Book b) {
		String delete = "delete from book where id = ?";
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(delete)) {
			statement.setInt(1, b.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<Book> findAll() {
		return findByParams(null, null, null, null);
	}

	public List<Book> findByParams(String category, String authorName, String bookName, LocalDate releaseDate) {
		List<Book> list = new ArrayList<>();
		StringBuilder sb = new StringBuilder(find);
		List<Object> params = new LinkedList<>();

		if (null != category && !category.isEmpty()) {
			sb.append(" and category.name like ?");
			params.add("%".concat(category).concat("%"));
		}

		if (null != authorName && !authorName.isEmpty()) {
			sb.append(" and author.name like ?");
			params.add("%".concat(authorName).concat("%"));
		}

		if (null != bookName && !bookName.isEmpty()) {
			sb.append("and  book.name like ?");
			params.add("%".concat(bookName).concat("%"));
		}

		if (null != releaseDate) {
			sb.append(" and book.release_date >= ?");
			params.add(Date.valueOf(releaseDate));
		}

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sb.toString())) {

			for (int i = 0; i < params.size(); i++) {
				stmt.setObject(i + 1, params.get(i));
			}

			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				list.add(getObject(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}

	public Book getObject(ResultSet rs) throws SQLException {
		Book b = new Book();
		b.setId(rs.getInt("id"));
		b.setName(rs.getString("book_name"));
		b.setPrice(rs.getInt("price"));
		b.setReleaseDate(rs.getDate("release_date").toLocalDate());
		b.setRemark(rs.getString("remark"));

		Category c = new Category();
		c.setId(rs.getInt("category_id"));
		c.setName(rs.getString("category_name"));

		Author a = new Author();
		a.setId(rs.getInt("author_id"));
		a.setName(rs.getString("author_name"));

		b.setCategory(c);
		b.setAuthor(a);

		return b;
	}

}
