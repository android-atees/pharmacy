package in.ateesinfomedia.remedio.models;

import java.io.Serializable;

public class OrderDetails implements Serializable{

    String id;
    String prescription_id;
    String medicine_id;
    String medicine_name;
    String medicine_brand;
    String medicine_price;
    String quantity;
    String gst;
    String total_amount;
    String gst_percentage;

    String sub_total;
    String cgst_amount;
    String sgst_amount;
    String confirm_date;
    String medicine_mrp;
    String batch_id;
    String expairy_date;
    String manufacture_id;
    String cgst_percentage;
    String sgst_percentage;
    String manufacture_name;
    String batch;
    String hsn_code;

    String user_status;

    String name;
    String address;
    String number;
    String alternativeNumber;
    String cityOrDistrict;
    String state;
    String country;
    String pinCode;
    String landmark;
    String mLat;
    String mlong;

    String tracking;
    String pres_image;

    public String getPres_image() {
        return pres_image;
    }

    public void setPres_image(String pres_image) {
        this.pres_image = pres_image;
    }

    public String getmLat() {
        return mLat;
    }

    public void setmLat(String mLat) {
        this.mLat = mLat;
    }

    public String getMlong() {
        return mlong;
    }

    public void setMlong(String mlong) {
        this.mlong = mlong;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getCgst_amount() {
        return cgst_amount;
    }

    public void setCgst_amount(String cgst_amount) {
        this.cgst_amount = cgst_amount;
    }

    public String getSgst_amount() {
        return sgst_amount;
    }

    public void setSgst_amount(String sgst_amount) {
        this.sgst_amount = sgst_amount;
    }

    public String getConfirm_date() {
        return confirm_date;
    }

    public void setConfirm_date(String confirm_date) {
        this.confirm_date = confirm_date;
    }

    public String getMedicine_mrp() {
        return medicine_mrp;
    }

    public void setMedicine_mrp(String medicine_mrp) {
        this.medicine_mrp = medicine_mrp;
    }

    public String getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(String batch_id) {
        this.batch_id = batch_id;
    }

    public String getExpairy_date() {
        return expairy_date;
    }

    public void setExpairy_date(String expairy_date) {
        this.expairy_date = expairy_date;
    }

    public String getManufacture_id() {
        return manufacture_id;
    }

    public void setManufacture_id(String manufacture_id) {
        this.manufacture_id = manufacture_id;
    }

    public String getCgst_percentage() {
        return cgst_percentage;
    }

    public void setCgst_percentage(String cgst_percentage) {
        this.cgst_percentage = cgst_percentage;
    }

    public String getSgst_percentage() {
        return sgst_percentage;
    }

    public void setSgst_percentage(String sgst_percentage) {
        this.sgst_percentage = sgst_percentage;
    }

    public String getManufacture_name() {
        return manufacture_name;
    }

    public void setManufacture_name(String manufacture_name) {
        this.manufacture_name = manufacture_name;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getHsn_code() {
        return hsn_code;
    }

    public void setHsn_code(String hsn_code) {
        this.hsn_code = hsn_code;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAlternativeNumber() {
        return alternativeNumber;
    }

    public void setAlternativeNumber(String alternativeNumber) {
        this.alternativeNumber = alternativeNumber;
    }

    public String getCityOrDistrict() {
        return cityOrDistrict;
    }

    public void setCityOrDistrict(String cityOrDistrict) {
        this.cityOrDistrict = cityOrDistrict;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(String prescription_id) {
        this.prescription_id = prescription_id;
    }

    public String getMedicine_id() {
        return medicine_id;
    }

    public void setMedicine_id(String medicine_id) {
        this.medicine_id = medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public void setMedicine_name(String medicine_name) {
        this.medicine_name = medicine_name;
    }

    public String getMedicine_brand() {
        return medicine_brand;
    }

    public void setMedicine_brand(String medicine_brand) {
        this.medicine_brand = medicine_brand;
    }

    public String getMedicine_price() {
        return medicine_price;
    }

    public void setMedicine_price(String medicine_price) {
        this.medicine_price = medicine_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getGst_percentage() {
        return gst_percentage;
    }

    public void setGst_percentage(String gst_percentage) {
        this.gst_percentage = gst_percentage;
    }
}
