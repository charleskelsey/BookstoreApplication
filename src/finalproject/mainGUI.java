/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

/**
 *
 * @author charles kelsey
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;

public class mainGUI extends Application {
    //objects
    private final Manager manager = new Manager();
    private Customer currentCustomer;
    private static final Files fileOb = new Files();

    //buttons
    Button btn_login = new Button("Login");
    Button btn_logout = new Button("Logout");
 
    Button btn_goBack = new Button("Back");
 
    Button btn_books = new Button("Books");
    Button btn_customer = new Button("Customers");
 

    Button btn_buy = new Button("Buy");
    Button btn_redeemBuy = new Button("Redeem points & Buy");
 
    TextField usernameTF = new TextField();
    PasswordField passwordTF = new PasswordField();
 
    //HBox lays out its children in a single horizontal row
    //If the hbox has a border and/or padding set, then the contents will be layed out within those insets
    HBox hBox = new HBox(); 

    //TableView allows displaying table views in application
    TableView<BookItem> booksTable = new TableView<>(); //create table for books
    ObservableList<BookItem> books = FXCollections.observableArrayList();

    TableView<Customer> customersTable = new TableView<>(); //create table for customers list
    ObservableList<Customer> customers = FXCollections.observableArrayList();
 
    public ObservableList<BookItem> addBooks() { //function for appending all books from arraylist in manager class to 'books' arraylist
        books.addAll(manager.booksList); ////
        return books;
    }
 
    public ObservableList<Customer> addCustomers() { //function for appending all customers to 'customers' arraylist
        customers.addAll(manager.getCustomers());
        return customers;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bookstore Application");
        primaryStage.setResizable(false); //does not allowe resizing window
        primaryStage.setScene(new Scene(loginScreen(false), 605, 550)); //login screen initially set as false --> get there through primary stage
  
        primaryStage.show();
  
        System.out.println("Opened bookstore application");

        //this saves information in file after closing application
        try {
            manager.array_file_refresh();
            System.out.println("Information saved to files.");
        } catch (IOException e){
            System.out.println("Error occured while saving files.");
        }
  
        //login btn function
        btn_login.setOnAction(e -> {
            boolean flag = false;
   
            if(usernameTF.getText().equals(manager.getUsername()) && passwordTF.getText().equals(manager.getUsername())) {
                primaryStage.setScene(new Scene(manager_StartScreen(), 605, 550)); //if admin credentials correct, go to manager start screen
                flag = true;
            }
   
            for(Customer cust: manager.getCustomers()){ //search for if a customer in list matches credentials
                if (usernameTF.getText().equals(cust.getUsername()) && passwordTF.getText().equals(cust.getPassword())) {
                    currentCustomer = cust;
                    primaryStage.setScene(new Scene(customer_HomeScreen(0), 605, 550)); //if customer exists, go to customer home screen
                    flag = true;
                }
            }
   
            if(flag == false) { //keep user in the login screen if flag is false (ie. no account was verified as admin OR customer by system)
                primaryStage.setScene(new Scene(loginScreen(true), 605, 550));
            }
        }); //end login button function
  
        //logout btn function
        btn_logout.setOnAction(e -> {
            primaryStage.setScene(new Scene(loginScreen(false), 605, 550)); //logging out takes you to login screen by default
   
            for(BookItem book: Manager.booksList){ //apply a checkbox for each book in book list
                book.setSelect(new CheckBox());
            }
   
            //clear username and password text fields
            usernameTF.clear();
            passwordTF.clear();
        }); //end logout button function
   
        //back button function -> go to new scene: manager start screen
        btn_goBack.setOnAction(e -> primaryStage.setScene(new Scene(manager_StartScreen(), 605, 550)));
  
        //books button function -> go to new scene: book table screen
        btn_books.setOnAction(e -> primaryStage.setScene(new Scene(manager_booksTableScreen(), 605, 550)));
  
        //customer button function (for manager) -> go to new scene: customer table screen (view all customers in system)
        btn_customer.setOnAction(e -> primaryStage.setScene(new Scene(manager_CustomerTableScreen(), 605, 550)));

        //buy button function (will not work unless customer has chosen a book)
        btn_buy.setOnAction(e -> {
            boolean book_chosen = false;
   
            for(BookItem book: Manager.booksList) { //iterate through book list
                if (book.getSelect().isSelected()) { //if a book from the list is CHECKED, switch book_chosen flag to TRUE
                    book_chosen = true;
                }
            }
   
            if(book_chosen == false) { //if no book chosen, stay in default customer home screen
                primaryStage.setScene(new Scene(customer_HomeScreen(1), 605, 550));
            } else if(book_chosen == true) { //if at least 1 book is chosen, proceed to checkout screen
                primaryStage.setScene(new Scene(customer_CheckoutScreen(false), 605, 550));
            }
        }); //end buy button function
  
        //redeem-points-and-buy function (will not work unless customer has points)
        btn_redeemBuy.setOnAction(e -> {
            boolean book_chosen = false;
   
            for(BookItem b: Manager.booksList){
                if (b.getSelect().isSelected()) {
                    book_chosen = true;
                }
            }
   
            if(book_chosen == false){ //if no book chosen, stay in default customer home screen
                primaryStage.setScene(new Scene(customer_HomeScreen(1), 605, 550));
            } else if(currentCustomer.getPoints() == 0) { //if no points in account, stay in default customer home screen
                primaryStage.setScene(new Scene(customer_HomeScreen(2), 605, 550));
            } else if(currentCustomer.getPoints() > 0) { //if points > 0, proceed to checkout screen
                primaryStage.setScene(new Scene(customer_CheckoutScreen(true), 605, 550));
            }
        }); //end redeem-points-and-buy function
  
        //exiting application (pressing [x])
        //when application closed, reset file and add all the current infomation
        //when application reopened, program appends arrays with the current information
        primaryStage.setOnCloseRequest(e -> {
            System.out.println("Book Application closed.");
  
            try{ //proceeds to refresh and update all files using Files method
                fileOb.resetBookFile();
                fileOb.resetCustFile();
                System.out.println("Files have been reset.");
   
                fileOb.bookWrite(Manager.booksList);
                fileOb.customerWrite(manager.getCustomers());
                System.out.println("Files have been updated with current array data");
            } catch(IOException exception) { //output the exception if something goes wrong
                exception.printStackTrace();
                System.out.println("Exception thrown" + exception);
            }
        }); //end set-on-close request
    } //END primaryStage
 
    //SUPPLEMENTARY FUNCTIONS
 
    //LOGIN SCREEN
    public Group loginScreen(boolean loginError) {
        /* GROUP COMPONENT: a container component which applies no special layout to its children.
        Acts as a container to put other things in.
        All child components (nodes) are positioned at 0,0.
        Typically used to apply some effect or transformation to a set of controls as a whole - as a group*/
  
        Group loginScreen = new Group(); //to be the return variable
  
        //parent header
        HBox header = new HBox();
  
        //text container for title: "Bookstore"
        Text title_bookstore = new Text();
        title_bookstore.setText("BOOKSTORE");
        title_bookstore.setFont(Font.font("verdana", FontWeight.BOLD, 35));
  
        header.getChildren().addAll(title_bookstore); //add title to header component

        header.setAlignment(Pos.CENTER); //center header component

        //VBOX: lays out its children in a single VERTICAL COLUMN 
        //If the vbox has a border and/or padding set, then the contents will be layed out within those insets
        //INSETS: A set of INSIDE OFFSETS for the 4 sides of a rectangular area
        VBox loginBox = new VBox();
  
        loginBox.setPadding(new Insets(30,65,45,65));
        loginBox.setSpacing(15);
  
        Text username = new Text("USERNAME");
        Text password = new Text("PASSWORD");
  
        btn_login.setMinWidth(200); //setMinWidth: Property for overriding the region's computed minimum width
  
        //add username text and its text field, and password text and textfield to vbox 
        loginBox.getChildren().addAll(username, usernameTF, password, passwordTF, btn_login); //displayed in order it is added 

        if(loginError){ //if loginError parameter passed is TRUE
            Text errorMsg = new Text("Incorrect username or password.");
            loginBox.getChildren().add(errorMsg); //append text: error message to vbox
        }

        VBox contain = new VBox();
  
        //add header(hBOX) and login box(vBOX) into one container, then append to overall base -> Group: loginScreen
        contain.getChildren().addAll(header, loginBox);
        contain.setPadding(new Insets(80, 280, 200, 150));
        contain.setSpacing(80);

        loginScreen.getChildren().addAll(contain);
        return loginScreen;
    } //end loginScreen
 
    //MANAGER START SCREEN
    public VBox manager_StartScreen() {
        VBox manager_startscreen = new VBox(); //to return
  
        manager_startscreen.setAlignment(Pos.CENTER);
        manager_startscreen.setPadding(new Insets(200,200,200,200));
        manager_startscreen.setSpacing(100);

        HBox buttons = new HBox(); //horizontal box
  
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
  
        //add books btn and customer btn
        buttons.getChildren().addAll(btn_books, btn_customer);
        btn_books.setPrefSize(100,100);
        btn_customer.setPrefSize(100,100);
  
        //add buttons to main start screen
        manager_startscreen.getChildren().addAll(buttons, btn_logout);
  
        return manager_startscreen;
    }//end managerStartScreen
 
    //MANAGER BOOKS TABLE SCREEN
    public Group manager_booksTableScreen() {
        Group manager_booktable = new Group(); //to return

        //hBox.getChildren().clear(); 
        booksTable.getItems().clear();
        booksTable.getColumns().clear();

        Label label = new Label("BOOKS");
        label.setFont(new Font("verdana", 30));

        //book title col.
        TableColumn<BookItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMaxWidth(500);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //book price col.
        TableColumn<BookItem, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(150);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        booksTable.setItems(addBooks()); //set items into book table
        booksTable.getColumns().addAll(titleColumn, priceColumn); //put title and price category subheaders into table as well

        //book title text field
        TextField addBookTitle = new TextField();
        addBookTitle.setPromptText("Title");
        addBookTitle.setMaxWidth(titleColumn.getPrefWidth());
  
        //book price text field
        TextField addBookPrice = new TextField();
        addBookPrice.setMaxWidth(priceColumn.getPrefWidth());
        addBookPrice.setPromptText("Price");

        VBox combine = new VBox();

        //add button
        Button addButton = new Button("Add");

        Label bookAddErr = new Label("Invalid Input");

        addButton.setOnAction(e -> {
            try {
                double price = Math.round((Double.parseDouble(addBookPrice.getText()))*100);
                Manager.booksList.add(new BookItem(addBookTitle.getText(), price/100));
                //makes new book and adds it to arraylist
                booksTable.getItems().clear(); //refresh page so new books can be accessed
                booksTable.setItems(addBooks());
                addBookTitle.clear(); //clears text fields
                addBookPrice.clear();
                combine.getChildren().remove(bookAddErr); //removes a previous Invalid Input error if there was one
            } catch (Exception exception) {
                if(!combine.getChildren().contains(bookAddErr)){
                    combine.getChildren().add(bookAddErr);
                }
            }
        }); //end add button function
  
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            BookItem selectedItem = booksTable.getSelectionModel().getSelectedItem(); //selects row highlighted
            booksTable.getItems().remove(selectedItem); //removes from table view as soon as delete pressed
            Manager.booksList.remove(selectedItem); //actually removes from the arraylist
        }); //end delete button

        hBox.getChildren().addAll(addBookTitle, addBookPrice, addButton, deleteButton);
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER);

        HBox back = new HBox();

        back.setPadding(new Insets(5));
        back.getChildren().addAll(btn_goBack);

        combine.setAlignment(Pos.CENTER);
        combine.setSpacing(5);
        combine.setPadding(new Insets(0, 0, 0, 150));
        combine.getChildren().addAll(label, booksTable, hBox);

        VBox vbox = new VBox();

        vbox.setPadding(new Insets(0, 200, 60, 0));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(back, combine);

        manager_booktable.getChildren().addAll(vbox);

        return manager_booktable;
    } //end manager_booksTableScreen
 
    //CUSTOMER TABLE SCREEN (for MANAGER)
    public Group manager_CustomerTableScreen() {
        Group manager_customertable = new Group();

        hBox.getChildren().clear(); //clears hBox for appending username and password text fields, and add/delete button
        customersTable.getItems().clear();
        customersTable.getColumns().clear();

        Label label = new Label("CUSTOMER");
        label.setFont(new Font("verdana", 30));

        //Customer username column
        TableColumn<Customer, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setMinWidth(140);
        usernameCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        //Customer password column
        TableColumn<Customer, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setMinWidth(140);
        passwordCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        //Customer points column
        TableColumn<Customer, Integer> pointsCol = new TableColumn<>("Points");
        pointsCol.setMinWidth(100);
        pointsCol.setStyle("-fx-alignment: CENTER;"); //align text in textfield to center
        pointsCol.setCellValueFactory(new PropertyValueFactory<>("points"));

        customersTable.setItems(addCustomers()); //set items (customers) into customers table
        customersTable.getColumns().addAll(usernameCol, passwordCol, pointsCol); //put username, password, and points subheaders into table as well

        TextField username_add = new TextField();
        username_add.setPromptText("USERNAME");
        username_add.setMaxWidth(usernameCol.getPrefWidth());

        TextField password_add = new TextField();
        password_add.setMaxWidth(passwordCol.getPrefWidth());
        password_add.setPromptText("PASSWORD");

        Button btn_add = new Button("Add");
        btn_add.setOnAction(e -> { //add customer (for manager)
            if(!(username_add.getText().equals("") || password_add.getText().equals(""))) {
                manager.addCustomer(new Customer(username_add.getText(), password_add.getText())); //create Customer object w/ username and password, append to customerList
                customersTable.getItems().clear(); //this refreshes the table
                customersTable.setItems(addCustomers()); //add customer to table
    
                //clear text fields
                password_add.clear(); 
                username_add.clear();
            }
        }); //end add button function

        Button btn_delete = new Button("Delete");
        btn_delete.setOnAction(e -> { //delete customer (for manager)
            Customer item_selected = customersTable.getSelectionModel().getSelectedItem(); //selects item in table
            customersTable.getItems().remove(item_selected); //remove from table view

            manager.deleteCustomer(item_selected); //removes from the arraylist
        }); //end deleteButton (for manager)

        //add username textfield, password textfield, add btn, delete btn to horizontal box container
        hBox.getChildren().addAll(username_add, password_add, btn_add, btn_delete);
  
        hBox.setSpacing(35); //spacing of each child element in horizontal box (hbox)
        hBox.setAlignment(Pos.CENTER);
  
        HBox back_area = new HBox();
  
        back_area.setPadding(new Insets(5)); 
        back_area.getChildren().addAll(btn_goBack);

        VBox vertical_container = new VBox();
  
        vertical_container.setAlignment(Pos.CENTER);
        vertical_container.setSpacing(5);
        vertical_container.setPadding(new Insets(0,0,0,110));
        vertical_container.getChildren().addAll(label, customersTable, hBox);

        VBox complete_container = new VBox();

        complete_container.setPadding(new Insets(0, 150, 60, 0));
        complete_container.getChildren().addAll(back_area, vertical_container);
        complete_container.setAlignment(Pos.CENTER);

        manager_customertable.getChildren().addAll(complete_container);
        return manager_customertable;
    }//end manager_CustomerTableScreen
 
    //CUSTOMER HOME SCREEN
    public Group customer_HomeScreen(int flag) {
        Group cust_homescreen = new Group();
  
        //clear columns
        booksTable.getItems().clear();
        booksTable.getColumns().clear();
        //booksTable.setFocusModel(null);

        Text welcome_message = new Text("Hello, " + currentCustomer.getUsername() + "!");
        Text status_text = new Text(" Status: ");
        Text status_text_rank = new Text(currentCustomer.getStatus() + ", ");

        Text text_points = new Text(" Points: " + currentCustomer.getPoints());

        //booktitle col
        TableColumn<BookItem, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setMinWidth(200);
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        //bookprice col
        TableColumn<BookItem, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        //checkbox col
        TableColumn<BookItem, String> selectColumn = new TableColumn<>("Select");
        selectColumn.setMinWidth(100);
        selectColumn.setCellValueFactory(new PropertyValueFactory<>("select"));

        booksTable.setItems(addBooks()); //add books to booktable (it was initially cleared)
        booksTable.getColumns().addAll(titleColumn, priceColumn, selectColumn); //place tile, price, and select columns into table (becomes visible)

        HBox cust_infobox = new HBox();
  
        cust_infobox.getChildren().addAll(status_text, status_text_rank, text_points);
  
        BorderPane header_top = new BorderPane();
  
        header_top.setLeft(welcome_message);
        header_top.setRight(cust_infobox);

        HBox bottom_hArea = new HBox();
  
        bottom_hArea.setAlignment(Pos.BOTTOM_CENTER);
        bottom_hArea.setSpacing(30);
  
        bottom_hArea.getChildren().addAll(btn_buy, btn_redeemBuy, btn_logout);

        String errorMsg = "";

        if(flag == 1){ errorMsg = "You must choose a book first!"; }
        else if(flag == 2){ errorMsg =  "You have no points to redeem!"; }

        Text display_errorMsg = new Text(errorMsg);
  
        VBox contain_all = new VBox();
  
        contain_all.setSpacing(10);
        contain_all.setAlignment(Pos.CENTER);
        contain_all.setPadding(new Insets(40, 200, 30, 100));
        contain_all.getChildren().addAll(header_top, booksTable, bottom_hArea, display_errorMsg);

        cust_homescreen.getChildren().addAll(contain_all);

        return cust_homescreen;
    }//end customerHomeScreen
 
    //CUSTOMER CHECKOUT SCREEN
    public Group customer_CheckoutScreen(boolean usedPoints) {
        Group customer_checkout = new Group();
  
        double subtotal = 0;
        double total;
        double discount;

        int pointsEarned;
        int first = 0;
        int num_of_books = 0;
  
        String[][] boughtBooks = new String[25][2];

        for(BookItem book: Manager.booksList){
            if(book.getSelect().isSelected()){
                subtotal = subtotal + book.getPrice();
                boughtBooks[first][0] = book.getTitle();
                boughtBooks[first][1] = String.valueOf(book.getPrice());
       
                first++;
            }
        }

        if(usedPoints == true) { //if points were used
            //if points money value is greater than or equal to subtotal, just use the points
            if((double)currentCustomer.getPoints()/100 >= subtotal) {
                discount = subtotal;
                //add the negative amount of points from converting from subtotal
                currentCustomer.setPoints(-(int)subtotal*100);
            } else { //else discount is number of points/100 -> $
                discount = ((double)currentCustomer.getPoints()/100);
                //add the negative amount of points to customer's currnet points
                currentCustomer.setPoints(-currentCustomer.getPoints());
            }
        } else { discount = 0; }

        total = subtotal - discount; //subtract discount from subtotal to obtain total
        pointsEarned = (int)total * 10; //gain back points based on total paid (x10)
        currentCustomer.setPoints(pointsEarned); //set those points gained to customer's total points

        HBox header = new HBox();
  
        header.setAlignment(Pos.CENTER);
        header.setSpacing(15);
        header.setPadding(new Insets(0,0,25,0));

        VBox vbox_receipt = new VBox();

        Text title_receipt = new Text();
        title_receipt.setText("RECEIPT");
        title_receipt.setFont(Font.font("verdana", FontWeight.BOLD, 35));
  
        header.getChildren().addAll(title_receipt);
  
        for (int i = 0; i < 25; i++) {
            if(boughtBooks[i][0] != null) { //if bought at least 1 book
                Text bookTitle = new Text(boughtBooks[i][0]);
                Text bookPrice = new Text(boughtBooks[i][1]);
    
                BorderPane item = new BorderPane();
    
                item.setLeft(bookTitle);
                item.setRight(bookPrice);

                num_of_books++;
            }
        }

        Text subtotalTxt = new Text("Price before points discount: $" + (Math.round(subtotal*100.0))/100.0);
        Text pointsDisc = new Text("Points Discount: $" + (Math.round(discount*100.0))/100.0);
        Text totalTxt = new Text("Total Price: $" + (Math.round(total*100.0))/100.0);

        vbox_receipt.getChildren().addAll(subtotalTxt, pointsDisc, totalTxt);

        vbox_receipt.setAlignment(Pos.CENTER);

        VBox bottom_area = new VBox();
  
        bottom_area.setSpacing(60);
        bottom_area.setAlignment(Pos.CENTER);
        Text info = new Text("You earned " + pointsEarned + " points " + ". Your status is now: " + currentCustomer.getStatus() + "." + "\nThank you, and we hope to see you again!");
        bottom_area.getChildren().addAll(info, btn_logout);

        VBox container = new VBox();

        container.setPadding(new Insets(60,105,500,100));
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.getChildren().addAll(header, vbox_receipt, bottom_area);

        customer_checkout.getChildren().addAll(container);
  
        Manager.booksList.removeIf(book -> book.getSelect().isSelected()); //remove book if book is chosen
  
        return customer_checkout;
    }//end customer_CheckoutScreen

    public static void main(String[] args) {
        launch(args);
    }
}
