package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 12-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailPaymentInfo implements Serializable {

    @SerializedName("method")
    private String method;

    @SerializedName("info")
    private String info;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
