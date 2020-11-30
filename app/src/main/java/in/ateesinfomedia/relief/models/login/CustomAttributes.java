package in.ateesinfomedia.relief.models.login;

/*
 *Created by Adithya T Raj on 13-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomAttributes implements Serializable {

    @SerializedName("attribute_code")
    private String attribute_code;

    @SerializedName("value")
    private String value;

    public String getAttribute_code() {
        return attribute_code;
    }

    public void setAttribute_code(String attribute_code) {
        this.attribute_code = attribute_code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
