package in.ateesinfomedia.relief.models.state;

/*
 *Created by Adithya T Raj on 3-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StateModel implements Serializable {

    @SerializedName("value")
    private String value;

    @SerializedName("label")
    private String label;

    @SerializedName("code")
    private String code;

    @SerializedName("country_id")
    private String country_id;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
