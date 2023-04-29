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

import javafx.scene.control.CheckBox;

public class BookItem {
    private String bookTitle;
    private double bookPrice;
    
    public CheckBox select; //checkbox for selecting books as customer

    public BookItem(String bookTitle, double bookPrice) {
        select = new CheckBox();
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
    }

    public String getTitle() {
        return this.bookTitle;
    }

    public double getPrice(){
        return this.bookPrice;
    }
 
    public void setTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }
 
    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public CheckBox getSelect(){
        return select;
    }

    //unselects checkbox after leaving customer home screen
    public void setSelect(CheckBox select){
        this.select = select;
    }
}
