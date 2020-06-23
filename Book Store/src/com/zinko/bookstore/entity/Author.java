package com.zinko.bookstore.entity;

import java.io.Serializable;

public class Author implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private int age;
	private String country;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public String getCountry() {
		return country;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public String toString() {
		return this.getName();
	}
}