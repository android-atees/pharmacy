package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderResponse implements Serializable {

    @SerializedName("items")
    private ArrayList<OrdersModel> items;

    @SerializedName("message")
    private String message;

    public ArrayList<OrdersModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrdersModel> items) {
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
