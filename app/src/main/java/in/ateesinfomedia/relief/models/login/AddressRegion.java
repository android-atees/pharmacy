package in.ateesinfomedia.relief.models.login;

/*
 *Created by Adithya T Raj on 26-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AddressRegion implements Serializable {

    @SerializedName("region_code")
    private String region_code;

    @SerializedName("region")
    private String region;

    @SerializedName("region_id")
    private String region_id;

    public String getRegion_code() {
        return region_code;
    }

    public void setRegion_code(String region_code) {
        this.region_code = region_code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegion_id() {
        return region_id;
    }

    public void setRegion_id(String region_id) {
        this.region_id = region_id;
    }
}
