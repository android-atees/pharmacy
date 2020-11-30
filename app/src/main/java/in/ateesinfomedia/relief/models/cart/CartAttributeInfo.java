package in.ateesinfomedia.relief.models.cart;

/*
 *Created by Adithya T Raj on 08-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartAttributeInfo implements Serializable {

    /*@SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;*/

    @SerializedName("option_id")
    private String option_id;

    @SerializedName("option_value")
    private String option_value;

    public String getOption_id() {
        return option_id;
    }

    public void setOption_id(String option_id) {
        this.option_id = option_id;
    }

    public String getOption_value() {
        return option_value;
    }

    public void setOption_value(String option_value) {
        this.option_value = option_value;
    }
}
