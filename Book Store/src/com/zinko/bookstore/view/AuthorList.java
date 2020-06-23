package com.zinko.bookstore.view;

import java.util.List;

import com.zinko.bookstore.entity.Author;
import com.zinko.bookstore.services.AuthorService;
import com.zinko.bookstore.util.TabelFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;

public class AuthorList {
	@FXML
	private TextField authorName;
	@FXML
	private TextField authorAge;
	@FXML
	private TextField authorCountry;
	@FXML
	private TableView<Author> authorTableView;
	@FXML
	private TableColumn<Author, String> nameCol;
	@FXML
	private TableColumn<Author, Integer> ageCol;
	@FXML
	private TableColumn<Author, String> countryCol;

	private AuthorService authorService;

//To add the data into the database(Author Table)
	public void add() {
		Author a = new Author();
		if (!authorName.getText().isEmpty())
			a.setName(authorName.getText());

		if (!authorAge.getText().isEmpty())
			a.setAge(Integer.parseInt(authorAge.getText()));

		if (!authorCountry.getText().isEmpty())
			a.setCountry(authorCountry.getText());
		authorService.add(a);
		search();
		clear();

	}

//To Search the data of the authors from author table
	public void search() {
		authorTableView.getItems().clear();
		int age = this.authorAge.getText().isEmpty() ? 0 : Integer.parseInt(this.authorAge.getText());
		List<Author> authorList = authorService.findByParams(authorName.getText(), age, authorCountry.getText());
		authorTableView.getItems().addAll(authorList);

	}

//To Clear the TextField
	public void clear() {
		authorName.clear();
		authorAge.clear();
		authorCountry.clear();

	}

	@FXML
	public void initialize() {
		authorService = AuthorService.getInstance();

		search();

		authorName.textProperty().addListener((a, b, c) -> search());
		authorAge.textProperty().addListener((a, b, c) -> search());
		authorCountry.textProperty().addListener((a, b, c) -> {
			search();
		});
		
//To Edit TableCell Data
		nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		nameCol.setOnEditCommit(n -> {
			Author a = n.getRowValue();
			a.setName(n.getNewValue());
			authorService.update(a);
			search();
		});

		ageCol.setCellFactory(TextFieldTableCell.forTableColumn(new TabelFormatter()));
		ageCol.setOnEditCommit(a -> {
			Author auth = a.getRowValue();
			auth.setAge(a.getNewValue());
			authorService.update(auth);
			search();
		});

		countryCol.setCellFactory(TextFieldTableCell.forTableColumn());
		countryCol.setOnEditCommit(c -> {
			Author a = c.getRowValue();
			a.setCountry(c.getNewValue());
			authorService.update(a);
			search();
		});

// To Delete  Table Item
		MenuItem delete = new MenuItem("Delete");
		authorTableView.setContextMenu(new ContextMenu(delete));
		delete.setOnAction(d -> {
			Author a = authorTableView.getSelectionModel().getSelectedItem();
			authorService.delete(a);
			search();
		});

	}

}
