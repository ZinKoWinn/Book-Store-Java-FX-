package com.zinko.bookstore.view;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;
import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.services.AuthorService;
import com.zinko.bookstore.services.BookService;
import com.zinko.bookstore.services.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class BookList {

	@FXML
	private TextField category;
	@FXML
	private TextField authorName;
	@FXML
	private TextField bookName;
	@FXML
	private DatePicker releaseDate;
	@FXML
	private TableView<Book> bookTableView;

	private CategoryService categoryService;
	private AuthorService authorService;
	private BookService bookService;
	private Consumer<Book> listener;

	public void add() {
		BookEdit.show(null, this::listener);
	}

	public void search() {
		bookTableView.getItems().clear();
		List<Book> bookList = bookService.findByParams(category.getText(), authorName.getText(), bookName.getText(),
				releaseDate.getValue());
		bookTableView.getItems().addAll(bookList);
	}

	public void clear() {
		category.clear();
		authorName.clear();
		bookName.clear();
		releaseDate.setValue(null);
	}

	private void listener(Book book) {
		listener.accept(book);
	}

	@FXML
	public void initialize() {
		categoryService = CategoryService.getInstance();
		authorService = AuthorService.getInstance();
		bookService = BookService.getInstance();
		// loadCategory();
		// loadAuthor();

		MenuItem edit = new MenuItem("Edit");
		MenuItem delete = new MenuItem("Delete");
		MenuItem imageUpload = new MenuItem("Image Upload");
		MenuItem showDetail = new MenuItem("Show Detail");

		bookTableView.setContextMenu(new ContextMenu(edit, delete, imageUpload, showDetail));

		edit.setOnAction(e -> {
			Book book = bookTableView.getSelectionModel().getSelectedItem();
			BookEdit.show(book, this::listener);
			bookService.update(book);
			search();
		});

		delete.setOnAction(d -> {
			Book book = bookTableView.getSelectionModel().getSelectedItem();
			bookService.delete(book);
			search();
		});

		imageUpload.setOnAction(i -> {
			FileChooser chooser = new FileChooser();
			chooser.setTitle("Choose Book Cover Photo");
			chooser.setSelectedExtensionFilter(new ExtensionFilter("Book Cover Photo", "*.png", "*.jpg", "*.jpeg"));
			File file = chooser.showSaveDialog(authorName.getScene().getWindow());
			Book book = bookTableView.getSelectionModel().getSelectedItem();
			book.setImage(file.getAbsolutePath());
			bookService.imageUpload(book);
		});

		showDetail.setOnAction(s -> {
			Book book = bookTableView.getSelectionModel().getSelectedItem();
			BookDetail.show(book);
		});

		search();

		category.textProperty().addListener((a, b, c) -> search());
		authorName.textProperty().addListener((a, b, c) -> search());
		bookName.textProperty().addListener((x, y, z) -> search());
		releaseDate.valueProperty().addListener((a, b, c) -> search());

	}

}
