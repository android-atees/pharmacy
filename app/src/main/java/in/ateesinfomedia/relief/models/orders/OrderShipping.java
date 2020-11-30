package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import in.ateesinfomedia.relief.models.login.CustomerAddress;

public class OrderShipping implements Serializable {

    @SerializedName("address")
    private OrderAddress address;

    @SerializedName("method")
    private String method;

    public OrderAddress getAddress() {
        return address;
    }

    public void setAddress(OrderAddress address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
