package in.ateesinfomedia.relief.models.cart;

/*
 *Created by Adithya T Raj on 21-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartCountResponse implements Serializable {

    @SerializedName("count")
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
