package in.ateesinfomedia.relief.models;

import java.io.Serializable;

public class LaboratoryModel implements Serializable{

    String id;
    String name;
    String address;
    String place;
    String pincode;
    String phone;
    String alter_phone;
    String email;
    String image;
    String description;
    String lat;
    String longi;
    String constituency_id;
    String constituency_name;
    String opening_days;
    String opening_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAlter_phone() {
        return alter_phone;
    }

    public void setAlter_phone(String alter_phone) {
        this.alter_phone = alter_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getConstituency_id() {
        return constituency_id;
    }

    public void setConstituency_id(String constituency_id) {
        this.constituency_id = constituency_id;
    }

    public String getConstituency_name() {
        return constituency_name;
    }

    public void setConstituency_name(String constituency_name) {
        this.constituency_name = constituency_name;
    }

    public String getOpening_days() {
        return opening_days;
    }

    public void setOpening_days(String opening_days) {
        this.opening_days = opening_days;
    }

    public String getOpening_time() {
        return opening_time;
    }

    public void setOpening_time(String opening_time) {
        this.opening_time = opening_time;
    }
}
