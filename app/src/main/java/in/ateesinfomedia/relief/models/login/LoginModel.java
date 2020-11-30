package in.ateesinfomedia.relief.models.login;

/*
 *Created by Adithya T Raj on 20-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginModel implements Serializable {

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("customer_data")
    private CustomerData customer_data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CustomerData getCustomer_data() {
        return customer_data;
    }

    public void setCustomer_data(CustomerData customer_data) {
        this.customer_data = customer_data;
    }
}
