package com.zinko.bookstore.view;

import java.util.List;

import com.zinko.bookstore.entity.Book;
import com.zinko.bookstore.entity.SaleDetail;
import com.zinko.bookstore.services.AuthorService;
import com.zinko.bookstore.services.BookService;
import com.zinko.bookstore.services.CategoryService;
import com.zinko.bookstore.services.SaleService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Home {
	@FXML
	private TextField category;
	@FXML
	private TextField bookName;
	@FXML
	private TableView<Book> homeTableView;
	@FXML
	private TableView<SaleDetail> cartTableView;
	@FXML
	private TableColumn<SaleDetail, String> quantityCol;
	@FXML
	private Label subTotal;
	@FXML
	private Label tax;
	@FXML
	private Label total;
	@FXML
	private Label topTotal;

	private SaleService saleService;
	private AuthorService authorService;
	private CategoryService categoryService;
	private BookService bookService;

	//To search the bookList that have for the sale
	public void search() {
		homeTableView.getItems().clear();
		List<Book> bookList = bookService.findByParams(category.getText(), null, bookName.getText(), null);
		homeTableView.getItems().addAll(bookList);

	}

	public void addToCart(MouseEvent event) {
		if (event.getClickCount() == 2) {
			Book bookList = homeTableView.getSelectionModel().getSelectedItem();

			if (bookList != null) {
				SaleDetail soldList = cartTableView.getItems().stream().filter(s -> s.getBookId() == bookList.getId())
						.findAny().orElse(null);

//If there is no item in the cartTableView,add the data
				if (null == soldList) {
					soldList = new SaleDetail();
					soldList.setBookId(bookList.getId());
					soldList.setBookName(bookList.getName());
					soldList.setQuantity(1);
					soldList.setCategory(bookList.getCategory());
					soldList.setAuthor(bookList.getAuthor());
					soldList.setUnitprice(bookList.getPrice());
					// Tax Rate 5%
					soldList.setTax(soldList.getSubTotal() * 5 / 100);
					cartTableView.getItems().add(soldList);
				} else {
					soldList.setQuantity(soldList.getQuantity() + 1);
					cartTableView.refresh();
				}

				findTotal();

			}

		}

	}

	//To find the the total cost of the books
	public void findTotal() {
		subTotal.setText(String
				.valueOf(cartTableView.getItems().stream().mapToInt(st -> st.getUnitprice() * st.getQuantity()).sum()));
		tax.setText(String.valueOf(cartTableView.getItems().stream().mapToInt(t -> t.getTax()).sum()));
		total.setText(String.valueOf(cartTableView.getItems().stream().mapToInt(tt -> tt.getTotal()).sum()));
		topTotal.setText(String.valueOf(cartTableView.getItems().stream().mapToInt(tt -> tt.getTotal()).sum()));

	}

	//To Clear the all Label and cartTableview Items
	public void clear(MouseEvent e) {
		subTotal.setText("0");
		tax.setText("0");
		total.setText("0");
		topTotal.setText("0");
		cartTableView.getItems().clear();

	}

	@FXML
	public void initialize() {
		categoryService = CategoryService.getInstance();
		authorService = AuthorService.getInstance();
		bookService = BookService.getInstance();

		category.textProperty().addListener((x, y, z) -> search());
		bookName.textProperty().addListener((x, y, z) -> search());

		search();

	}

}
