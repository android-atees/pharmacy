package in.ateesinfomedia.relief.models.detailpro;

/*
 *Created by Adithya T Raj on 25-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfigProductDetailResponse implements Serializable {

    @SerializedName("data")
    private ProductDetail data;

    @SerializedName("message")
    private String message;

    public ProductDetail getData() {
        return data;
    }

    public void setData(ProductDetail data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
