package com.zinko.bookstore.services;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.util.ConnectionManager;

public class CategoryService {
	private static CategoryService INSTANCE;
	private static final String insert = "insert into category (name) values (?)";
	private static final String update = "update category  name = ? where id = ?";
	private static final String delete = "delete from category where id = ?";

	public CategoryService() {
	}

	public static CategoryService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CategoryService();

		}
		return INSTANCE;
	}

	public void add(Category c) {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, c.getName());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				c.setId(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void update(Category c) {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(update)) {
			statement.setString(1, c.getName());
			statement.setInt(2, c.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Category c) {
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(delete)) {
			statement.setInt(1, c.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Category> findAll() {
		String find = "select * from category where 1 = 1";
		List<Category> list = new ArrayList<>();

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement statement = conn.prepareStatement(find)) {

			ResultSet rs = statement.executeQuery();

			while (rs.next())
				list.add(getObject(rs));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Category> findByName(String name) {
		String find = "select * from category where 1 = 1";
		List<Category> list = new ArrayList<>();
		boolean isConcat = null != name && !name.isEmpty();

		try (Connection conn = ConnectionManager.getConnection();
				PreparedStatement statement = conn
						.prepareStatement(isConcat ? find.concat(" and name like ?") : find)) {

			if (isConcat) {
				statement.setString(1, "%".concat(name).concat("%"));
			}
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				list.add(getObject(rs));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	public Category getObject(ResultSet rs) throws SQLException {
		Category c = new Category();
		c.setId(rs.getInt("id"));
		c.setName(rs.getString("name"));
		return c;
	}

	public void upload(File file) {
		try {
			Files.readAllLines(file.toPath()).stream().map(Category::new).forEach(this::add);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
