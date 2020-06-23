package com.zinko.bookstore.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zinko.bookstore.entity.Author;
import com.zinko.bookstore.util.ConnectionManager;

public class AuthorService {
	private static AuthorService INSTANCE;

	public AuthorService() {
	}

	public static AuthorService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new AuthorService();
		}
		return INSTANCE;
	}

	public void add(Author a) {
		String insert = "insert into author (name,age,country) values (?,?,?)";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, a.getName());
			statement.setInt(2, a.getAge());
			statement.setString(3, a.getCountry());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				a.setId(rs.getInt(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Author a) {
		String update = "update author set name = ?, age = ?, country = ? where id = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, a.getName());
			statement.setInt(2, a.getAge());
			statement.setString(3, a.getCountry());
			statement.setInt(4, a.getId());
			statement.executeUpdate();

			ResultSet rs = statement.getGeneratedKeys();
			while (rs.next()) {
				a.setId(rs.getInt(1));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Author a) {
		String delete = "delete from author  where id = ?";

		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(delete)) {
			statement.setInt(1, a.getId());
			statement.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Author> findAll() {
		return findByParams(null, 0, null);
	}

	public List<Author> findByParams(String name, int age, String country) {
		String find = "select * from author where 1 = 1";
		List<Author> authorList = new ArrayList<>();
		StringBuilder sb = new StringBuilder(find);
		List<Object> params = new LinkedList<>();

		if (null != name && !name.isEmpty()) {
			sb.append(" and name like ?");
			params.add("%".concat(name).concat("%"));
		}

		if (age < 0) {
			sb.append("and age like ?");
			params.add(age);

		}

		if (null != country && !country.isEmpty()) {
			sb.append(" and country like ?");
			params.add("%".concat(country).concat("%"));
		}
		try (Connection connection = ConnectionManager.getConnection();
				PreparedStatement statement = connection.prepareStatement(sb.toString())) {
			for (int i = 0; i < params.size(); i++) {
				statement.setObject(i + 1, params.get(i));
			}

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				Author author = new Author();
				author.setId(rs.getInt("id"));
				author.setName(rs.getString("name"));
				author.setAge(rs.getInt("age"));
				author.setCountry(rs.getString("country"));
				authorList.add(author);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return authorList;

	}
}
