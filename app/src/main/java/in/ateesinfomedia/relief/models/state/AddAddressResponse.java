package in.ateesinfomedia.relief.models.state;

/*
 *Created by Adithya T Raj on 08-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddAddressResponse implements Serializable {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
