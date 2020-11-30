package in.ateesinfomedia.relief.models.cart;

/*
 *Created by Adithya T Raj on 26-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.ateesinfomedia.relief.models.login.CustomerData;

public class CartResponse implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("items")
    private ArrayList<CartModel> items;

    @SerializedName("items_count")
    private String items_count;

    @SerializedName("items_qty")
    private String items_qty;

    @SerializedName("customer")
    private CustomerData customer;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<CartModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<CartModel> items) {
        this.items = items;
    }

    public String getItems_count() {
        return items_count;
    }

    public void setItems_count(String items_count) {
        this.items_count = items_count;
    }

    public String getItems_qty() {
        return items_qty;
    }

    public void setItems_qty(String items_qty) {
        this.items_qty = items_qty;
    }

    public CustomerData getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerData customer) {
        this.customer = customer;
    }
}
