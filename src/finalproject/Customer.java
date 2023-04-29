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
public class Customer extends User {
    protected String username;
    protected String password;
    private String status;
    private int points;
  
    //constructor
    public Customer(String username, String password){
        this.username = username;
        this.password = password;
        points = 0;
    }
  
    //inherit methods from User
    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }
    
    public int getPoints(){
        return points;
    }
    
    //checks customers status based on points
    public String getStatus(){
        return status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPoints(int points) {
        this.points += points;
        setStatus(this.points);
    }

    public void setStatus(int points) {
        if (points>1000) {
            status = "GOLD";
        } else {
            status = "SILVER";
        }
    }
}
