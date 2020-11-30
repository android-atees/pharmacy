package in.ateesinfomedia.relief.models.cart;

/*
 *Created by Adithya T Raj on 08-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CartOptions implements Serializable {

    @SerializedName("attributes_info")
    private ArrayList<CartAttributeInfo> attributes_info;

    public ArrayList<CartAttributeInfo> getAttributes_info() {
        return attributes_info;
    }

    public void setAttributes_info(ArrayList<CartAttributeInfo> attributes_info) {
        this.attributes_info = attributes_info;
    }
}
