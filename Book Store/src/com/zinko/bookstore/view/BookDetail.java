package com.zinko.bookstore.view;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.services.BookService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BookDetail {
	
	@FXML
	private Label category;
	@FXML
	private Label authorName;
	@FXML
	private Label bookName;
	@FXML
	private Label price;
	@FXML
	private Label releasedDate;
   @FXML
   private ImageView image;
	@FXML
	private Label remark;
	private static final DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	
	public static void show(Book book) {
		try {
			FXMLLoader loader = new FXMLLoader(BookDetail.class.getResource("BookDetail.fxml"));
			Parent view = loader.load();
			BookDetail controller = loader.getController();
			Stage stage = new Stage();
			controller.setData(book);
			stage.setTitle("Book Detail");
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(view));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		  
	}
	
	public void setData(Book book) throws FileNotFoundException {
		
		if(null != book) {
			category.setText(book.getCategory().getName());
			authorName.setText(book.getAuthor().getName());
			bookName.setText(book.getName());
			price.setText(String.valueOf(book.getPrice()));
			releasedDate.setText(String.valueOf(formater.format(book.getReleaseDate())));
			remark.setText(book.getRemark().isEmpty() ? "UNKNOWN" : book.getRemark());	
			
			if(null != BookService.getInstance().getImage(book)) {
				image.setImage(new Image(new FileInputStream(BookService.getInstance().getImage(book))));
			}
			
		}
	}
	
	public void buy() {
		
	}
	
	public void close() {
		price.getScene().getWindow().hide();
		
	}

}
