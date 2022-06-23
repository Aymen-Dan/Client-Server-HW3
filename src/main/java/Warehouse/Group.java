package Warehouse;

import java.util.ArrayList;

public class Group {
    public String name;//name of product group
    public String description;//description of product group
    public ArrayList<Product> products;//list of products

    /** constructor with name only*/
    public Group (String name)  {
        this.name = name;

    }

    /** constructor with values, base size 7*/
    public Group (String name, String description)  {
        this.name = name;
        this.description = description;
        products = new ArrayList<>(7);
    }

    /**add product to product list */
    boolean addProduct(Product product) {
        return products.add(product);
    }



}

