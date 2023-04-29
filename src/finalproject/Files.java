/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalproject;

/**
 *
 * @author charl
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Files {
    //copy all exisiting books in book ArrayList to file "book.txt"
    public void bookWrite(ArrayList<BookItem> books) throws IOException {
        String book_info;
        FileWriter bookFile = new FileWriter("book.txt", true);
        for(BookItem book : books) { //iterate through passed arraylist: books
            book_info = book.getTitle() + ", " + book.getPrice() + "\n"; //format -> title, price
            bookFile.write(book_info); //write the book_info into bookFile in the format: title, price
        }
        bookFile.close(); //close bookFile
    }
 
    //copy all existing customers in customer ArrayList to file "customer.txt"
    public void customerWrite(ArrayList<Customer> customers) throws IOException{
        String cust_info;
        FileWriter custFile = new FileWriter("customer.txt", true);
        for(Customer c: customers) { //interate through passed arraylist: customers
            cust_info = c.getUsername() + ", " + c.getPassword() + ", " + c.getPoints() + "\n"; //format -> username, password, points
            custFile.write(cust_info); //write the cust_info into custFile in the format: username, password, points
        }
        custFile.close(); //close custFile
    }
 
    //reset entire book file so that new book can be copied into book list
    public void resetBookFile() throws IOException {
        FileWriter bookFile = new FileWriter("book.txt", false);
        bookFile.close();
    }
 
    //reset entire cust file so that new customer can be copied into customer list
    public void resetCustFile() throws IOException {
        FileWriter customerFile = new FileWriter("customer.txt", false);
        customerFile.close();
    }

    //read values from book file -> store info into temporary array -> send back values in temp array to store in the actual array
    public ArrayList<BookItem> bookReadf() throws IOException {
        Scanner scan = new Scanner(new FileReader("book.txt"));
  
        ArrayList<BookItem> storeTheBook = new ArrayList<>(); //temporary arraylist that stores name and price of book
  
        String bookTitle; //holds book title
        double bookPrice; //holds book price
  
        while(scan.hasNext()){ //scan through file
            String[] bookInfo = scan.nextLine().split(","); //splits the String at comma separator
            bookTitle = bookInfo[0]; //book title stored in: bookTitle
            bookPrice = Double.parseDouble(bookInfo[1]); //book price stored in: bookPrice (convert String into double)
            storeTheBook.add(new BookItem(bookTitle, bookPrice)); //create new BookItem object that holds the file's current book title and price, and append that to the local arraylist that stores the book's information
        }
        return storeTheBook; //return the arraylist containing book info values
    }

    public ArrayList<Customer> readCustomerFile() throws IOException {
        Scanner scan = new Scanner(new FileReader("customer.txt"));
  
        ArrayList<Customer> storeCustInfo = new ArrayList<>(); //temporary arraylist that stores customer's username, password, and points

        while(scan.hasNext()){ //scan through file
            String[] customerInfo = scan.nextLine().split(", "); //splits the String at comma separators
            String username = customerInfo[0]; //customer username stored in: username
            String password = customerInfo[1]; //customer password stored in: password
            int points = Integer.parseInt(customerInfo[2]); //customer's points stored in points (convert String to integer)
   
            storeCustInfo.add(new Customer(username, password)); //create new Customer object that holds the file's customer username, password, and points, and append that to the local arraylist that stores the customer's information
   
            //take the 2nd index value from local arraylist that stores customer's info: the points value -> take that value and set it as the number of points that specific customer has
            storeCustInfo.get(storeCustInfo.size()-1).setPoints(points);
        }
        return storeCustInfo; //return the arraylist containing customer's info values
    }
}
