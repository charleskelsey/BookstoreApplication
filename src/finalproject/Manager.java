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

import java.io.IOException;
import java.util.ArrayList;

public class Manager {
 
    protected String username = "admin";
    protected String password = "admin";
 
    private static final Files files = new Files(); //create a Files Object
    
    protected static ArrayList<BookItem> booksList = new ArrayList<>(); //create BookItem specific arraylist
    private static ArrayList<Customer> customersList = new ArrayList<>(); //create Customer specific arraylist

    //import values from arraylist obtained from files in Files class, and store those values into temporary arrays, which further are appended to main arraylist --> booksList and customersList
    public void array_file_refresh() throws IOException {
     
        ArrayList<BookItem> tempBooks = files.bookReadf(); //obtain the book's information from the arraylist in Files class
        ArrayList<Customer> tempCustomers = files.readCustomerFile(); //obtain the customer's information from the arraylist in Files class
        
        booksList.addAll(tempBooks); //append the book info to BookItem object specific arraylist
        customersList.addAll(tempCustomers); //append the customer info to Customer object specific arraylist
        
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }

    public void addCustomer(Customer created){
        customersList.add(created);
    }

    public void deleteCustomer(Customer selected){
        customersList.remove(selected);
    }

    public ArrayList<Customer> getCustomers(){
        return (ArrayList<Customer>) customersList.clone();
    }

}
