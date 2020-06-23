package com.zinko.bookstore.view;


import java.io.File;
import java.util.List;
import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.services.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class BookCategory {
	@FXML
	private TextField name;
	@FXML
	private FlowPane category_box;
	
	private CategoryService categoryService;

	//To add the Category name into the database(Category Table)
	public void add() {
		Category c = new Category();
		c.setName(name.getText());
		categoryService.add(c);
		search();
		clear();

	}

	//File uploading into the database (Category Table)
	public void upload() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Choose Catgory");
		chooser.setSelectedExtensionFilter(new ExtensionFilter("Category", "*.txt","*.csv","*.tsv"));
		File file = chooser.showSaveDialog(name.getScene().getWindow());
		categoryService.upload(file);

	}

	//To search the Category Name from the Category Table
	public void search() {
		category_box.getChildren().clear();
		List<Category> categoryList = categoryService.findByName(name.getText());
		categoryList .stream().map(c->new CategoryBox(c))
		                      .forEach(c->category_box.getChildren().addAll(c));
	//With method reference
	//	categoryList.stream().map(CategoryBox::new).forEach(category_box.getChildren()::addAll);
		                                                
	}

	//To Clear the TextField
	public void clear() {
		name.clear();
		
	}

	@FXML
	public void initialize() {
		categoryService = CategoryService.getInstance();
		search();
		name.textProperty().addListener((a,b,c)->search());
		name.setOnKeyPressed(p->{
			if(p.getCode().equals(KeyCode.ENTER)) {
				add();
			}
		});
		
	}
	
	private class CategoryBox extends HBox {
		CategoryBox(Category c) {
			getStyleClass().add("flowPane");
			Label catName = new Label(c.getName());
			getChildren().add(catName);
		}
	}

}
