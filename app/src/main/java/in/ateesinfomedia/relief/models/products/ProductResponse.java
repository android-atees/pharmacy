package in.ateesinfomedia.relief.models.products;

/*
 *Created by Adithya T Raj on 18-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductResponse implements Serializable {

    @SerializedName("items")
    private ArrayList<ProductModel> items;

    @SerializedName("total_count")
    private String total_count;

    public ArrayList<ProductModel> getItems() {
        return items;
    }

    public void setItems(ArrayList<ProductModel> items) {
        this.items = items;
    }

    public String getTotal_count() {
        return total_count;
    }

    public void setTotal_count(String total_count) {
        this.total_count = total_count;
    }
}
