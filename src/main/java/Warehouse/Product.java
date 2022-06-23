package Warehouse;

import java.util.ArrayList;

public class Product {

    public String name;//name of product
    public String description;//description of product
    public int price;//price of the product
    public int amount;//amount of the product stored


    /** constructor */
    public Product(String name, String description, int price, int amount){
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;

    }
}