package in.ateesinfomedia.relief.models.login;

/*
 *Created by Adithya T Raj on 20-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomerData implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("group_id")
    private String group_id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("updated_at")
    private String updated_at;

    @SerializedName("created_in")
    private String created_in;

    @SerializedName("email")
    private String email;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("store_id")
    private String store_id;

    @SerializedName("website_id")
    private String website_id;

    @SerializedName("disable_auto_group_change")
    private String disable_auto_group_change;

    @SerializedName("addresses")
    private ArrayList<CustomerAddress> addresses;

    @SerializedName("custom_attributes")
    private ArrayList<CustomAttributes> custom_attributes;

    public ArrayList<CustomerAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<CustomerAddress> addresses) {
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_in() {
        return created_in;
    }

    public void setCreated_in(String created_in) {
        this.created_in = created_in;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getWebsite_id() {
        return website_id;
    }

    public void setWebsite_id(String website_id) {
        this.website_id = website_id;
    }

    public String getDisable_auto_group_change() {
        return disable_auto_group_change;
    }

    public void setDisable_auto_group_change(String disable_auto_group_change) {
        this.disable_auto_group_change = disable_auto_group_change;
    }

    public ArrayList<CustomAttributes> getCustom_attributes() {
        return custom_attributes;
    }

    public void setCustom_attributes(ArrayList<CustomAttributes> custom_attributes) {
        this.custom_attributes = custom_attributes;
    }
}
