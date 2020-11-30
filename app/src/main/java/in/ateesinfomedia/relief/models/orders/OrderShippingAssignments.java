package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderShippingAssignments implements Serializable {

    @SerializedName("shipping")
    private OrderShipping shipping;

    @SerializedName("items")
    private ArrayList<OrderItem> items;

    public OrderShipping getShipping() {
        return shipping;
    }

    public void setShipping(OrderShipping shipping) {
        this.shipping = shipping;
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderItem> items) {
        this.items = items;
    }
}
