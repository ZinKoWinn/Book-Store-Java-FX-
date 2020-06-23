package com.zinko.bookstore.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.function.Consumer;

import com.zinko.bookstore.entity.Author;
import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.services.AuthorService;
import com.zinko.bookstore.services.BookService;
import com.zinko.bookstore.services.CategoryService;
import com.zinko.bookstore.util.ApplicationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookEdit {

	@FXML
	private Label info;
	@FXML
	private Label title;
	@FXML
	private ComboBox<Category> category;
	@FXML
	private ComboBox<Author> authorName;
	@FXML
	private TextField bookName;
	@FXML
	private TextField price;
	@FXML
	private DatePicker releasedDate;
	@FXML
	private TextArea remark;
	private static Stage stage;
	private Book book;
	private Consumer<Book> handler;
	private BookService bookservice;
	
	public static void show(Book book,Consumer<Book> handler)  {
		try {
			FXMLLoader loader = new FXMLLoader(BookEdit.class.getResource("BookEdit.fxml"));
			Parent view = loader.load();
			BookEdit control = loader.getController();
			stage = new Stage();
			control.setData(book);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void save() {
		try {
			boolean bookisNull= book == null;
			if(bookisNull) {
				book = new Book();
			}
				book.setCategory(category.getValue());
				if(null == category.getValue()) {
					throw new ApplicationException("Please select category!");
				}
				
				book.setAuthor(authorName.getValue());
				if(null == authorName.getValue()) {
					throw new ApplicationException("Please select author name!");
				}
				
				book.setName(bookName.getText());
				if(null == bookName.getText() && bookName.getText().isEmpty()) {
					throw new ApplicationException("Please enter book name!");
				}
				
				book.setPrice(Integer.parseInt(price.getText()));
				if(null == price.getText() && price.getText().isEmpty()) {
					throw new ApplicationException("Please enter price!");
				}
				
				book.setReleaseDate(releasedDate.getValue());
				if(null == releasedDate.getValue()) {
					throw new ApplicationException("Please select releaseDate!");
				}
				
				book.setRemark(remark.getText());
				
				if(bookisNull) {
					bookservice.add(book);
				}else {
					bookservice.update(book);
					
				}
				handler.accept(book);
				close();
				
			
		} catch (Exception e) {
			info.setText(e.getMessage());
		}
		close();
		
	}
	
	public void setData(Book book) throws FileNotFoundException {
		this.book = book;
		
		if(null != book) {
			category.setValue(book.getCategory());
			authorName.setValue(book.getAuthor());
			bookName.setText(book.getName());
			price.setText(String.valueOf((book.getPrice())));
			releasedDate.setValue(book.getReleaseDate());
			remark.setText(book.getRemark());
			title.setText("EDIT BOOK");
			stage.setTitle("EDIT BOOK");
			stage.getIcons().add(new Image(new FileInputStream("edit.png")));
			
			
		}else {
			title.setText("ADD BOOK");
			stage.setTitle("ADD BOOK");
			stage.getIcons().add(new Image(new FileInputStream("add.png")));
			
		}	
	}
	
	public void close() {
		info.getScene().getWindow().hide();
		
	}
	
	@FXML
	public void initialize() {
		category.getItems().addAll(CategoryService.getInstance().findAll());
		authorName.getItems().addAll(AuthorService.getInstance().findAll());
		bookservice = BookService.getInstance();
		
	}
}
