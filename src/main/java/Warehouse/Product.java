package Warehouse;

public class Product {

    public String name;//name of product
    public String description;//description of product
    public int price;//price of the product
    public int amount;//amount of the product stored

    /** constructor with name only*/
    public Product(String name){
        this.name = name;
    }

    /** constructor with values*/
    public Product(String name, String description, int price, int amount){
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getDescription(){
        return description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public int getPrice() {
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
    public int getAmount(){
        return amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void removeAmount(int amount) {this.amount -= amount;}
}