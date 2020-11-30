package in.ateesinfomedia.relief.models.shipping;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ShippingCost implements Serializable {

    @SerializedName("carrier_code")
    private String carrier_code;

    @SerializedName("method_code")
    private String method_code;

    @SerializedName("carrier_title")
    private String carrier_title;

    @SerializedName("method_title")
    private String method_title;

    @SerializedName("amount")
    private String amount;

    @SerializedName("base_amount")
    private String base_amount;

    @SerializedName("available")
    private Boolean available;

    @SerializedName("error_message")
    private String error_message;

    @SerializedName("price_excl_tax")
    private String price_excl_tax;

    @SerializedName("price_incl_tax")
    private String price_incl_tax;

    public String getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(String carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getMethod_code() {
        return method_code;
    }

    public void setMethod_code(String method_code) {
        this.method_code = method_code;
    }

    public String getCarrier_title() {
        return carrier_title;
    }

    public void setCarrier_title(String carrier_title) {
        this.carrier_title = carrier_title;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getBase_amount() {
        return base_amount;
    }

    public void setBase_amount(String base_amount) {
        this.base_amount = base_amount;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }

    public String getPrice_excl_tax() {
        return price_excl_tax;
    }

    public void setPrice_excl_tax(String price_excl_tax) {
        this.price_excl_tax = price_excl_tax;
    }

    public String getPrice_incl_tax() {
        return price_incl_tax;
    }

    public void setPrice_incl_tax(String price_incl_tax) {
        this.price_incl_tax = price_incl_tax;
    }
}
