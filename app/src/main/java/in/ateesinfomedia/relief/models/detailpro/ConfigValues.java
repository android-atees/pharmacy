package in.ateesinfomedia.relief.models.detailpro;

/*
 *Created by Adithya T Raj on 25-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConfigValues implements Serializable {

    @SerializedName("label")
    private String label;

    @SerializedName("value")
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
