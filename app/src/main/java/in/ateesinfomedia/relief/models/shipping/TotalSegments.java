package in.ateesinfomedia.relief.models.shipping;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TotalSegments implements Serializable {

    @SerializedName("code")
    private String code;

    @SerializedName("title")
    private String title;

    @SerializedName("value")
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
