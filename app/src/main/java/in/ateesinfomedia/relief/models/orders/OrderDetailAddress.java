package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 12-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetailAddress implements Serializable {

    @SerializedName("city")
    private String city;

    @SerializedName("street")
    private ArrayList<String> street;

    @SerializedName("postcode")
    private String postcode;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("state")
    private String state;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getStreet() {
        return street;
    }

    public void setStreet(ArrayList<String> street) {
        this.street = street;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
