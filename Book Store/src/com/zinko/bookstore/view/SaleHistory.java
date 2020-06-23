package com.zinko.bookstore.view;

import java.time.LocalDate;
import java.util.List;

import com.zinko.bookstore.entity.Category;
import com.zinko.bookstore.entity.SaleDetail;
import com.zinko.bookstore.services.CategoryService;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class SaleHistory {
	@FXML
	private ComboBox<Category> category;
	@FXML
	private TextField bookName;
	@FXML
	private DatePicker datefrom;
	@FXML
	private DatePicker dateto;
	@FXML
	private TableView<SaleDetail> saleHistoryTableView;
	private CategoryService categoryService;
	
	public void search() {
		
		
	}
	
	public void clear() {
		category.setValue(null);
		bookName.setText("");
		datefrom.setValue(LocalDate.now());
		dateto.setValue(LocalDate.now());
		
	}
	
	@FXML
	public void initialize() {
		categoryService = CategoryService.getInstance();
		
		List<Category> categoryList = categoryService.findAll();
		category.getItems().addAll(categoryList);
		
		category.valueProperty().addListener((a,b,c)-> search());
		bookName.textProperty().addListener((a,b,c)->search());
		
	}

}
