package com.zinko.bookstore.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainFrame implements Initializable {
	@FXML
	private Label title;
	@FXML
	private Label footer;
	@FXML
	private StackPane viewHolder;
	@FXML
	private VBox sideBar;

	//To show Home View
	public void showHome(MouseEvent event) {
		changeOnActive(event);
		loadView("Sale Book", "Home");

	}

	//To show Category View
	public void showCategory(MouseEvent event) {
		changeOnActive(event);
		loadView("Book Category", "BookCategory");

	}

	//To show BooList View
	public void showBook(MouseEvent event) {
		changeOnActive(event);
		loadView("Book List", "BookList");

	}

	//To show AuthorList View
	public void showAuthor(MouseEvent event) {
		changeOnActive(event);
		loadView("Author List", "AuthorList");

	}
 
	//To Show Sale History View
	public void showSaleHistory(MouseEvent event) {
		changeOnActive(event);
		loadView("Sale History", "SaleHistory");
	}
	
	private void changeOnActive(MouseEvent event) {
		Node node = (Node)event.getSource();
		sideBar.getChildren().stream()		                    
							 .filter(n -> n.getStyleClass().contains("active"))
							 .findAny()
							 .ifPresent(n -> n.getStyleClass().remove("active"));
		node.getStyleClass().add("active");
	}

	public void loadView(String viewName, String fileName) {
		title.setText(viewName);
		try {

			Parent view = FXMLLoader.load(getClass().getResource(fileName.concat(".fxml")));
			viewHolder.getChildren().clear();
			viewHolder.getChildren().add(view);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resource) {
		footer.setText("\u00A9 By JDC");
		loadView("Sale Book", "Home");

	}

}
