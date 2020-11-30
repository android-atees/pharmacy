package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.ateesinfomedia.relief.models.login.AddressRegion;

public class OrderAddress implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("customer_id")
    private String customer_id;

    @SerializedName("region")
    private String region;

    @SerializedName("region_id")
    private String region_id;

    @SerializedName("country_id")
    private String country_id;

    @SerializedName("street")
    private ArrayList<String> street;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("postcode")
    private String postcode;

    @SerializedName("city")
    private String city;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("default_shipping")
    private Boolean default_shipping;

    @SerializedName("default_billing")
    private Boolean default_billing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
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

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }

    public ArrayList<String> getStreet() {
        return street;
    }

    public void setStreet(ArrayList<String> street) {
        this.street = street;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Boolean getDefault_shipping() {
        return default_shipping;
    }

    public void setDefault_shipping(Boolean default_shipping) {
        this.default_shipping = default_shipping;
    }

    public Boolean getDefault_billing() {
        return default_billing;
    }

    public void setDefault_billing(Boolean default_billing) {
        this.default_billing = default_billing;
    }
}
