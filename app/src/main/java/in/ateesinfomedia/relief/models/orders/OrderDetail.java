package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 12-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderDetail implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("increment_id")
    private String increment_id;

    @SerializedName("status")
    private String status;

    @SerializedName("grand_total")
    private String grand_total;

    @SerializedName("sub_total")
    private String sub_total;

    @SerializedName("discount_amount")
    private String discount;

    @SerializedName("shipping_amount")
    private String shipping_amount;

    @SerializedName("tax")
    private String tax;

    @SerializedName("total_qty_ordered")
    private String total_qty_ordered;

    @SerializedName("order_currency")
    private String order_currency;

    @SerializedName("customer_firstname")
    private String customer_firstname;

    @SerializedName("customer_lastname")
    private String customer_lastname;

    @SerializedName("billing_address")
    private OrderDetailAddress billing_address;

    @SerializedName("shipping_address")
    private OrderDetailAddress shipping_address;

    @SerializedName("payment_info")
    private OrderDetailPaymentInfo payment_info;

    @SerializedName("items")
    private ArrayList<OrderDetailItem> items;

    @SerializedName("order_created_at")
    private String order_created_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIncrement_id() {
        return increment_id;
    }

    public void setIncrement_id(String increment_id) {
        this.increment_id = increment_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(String shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal_qty_ordered() {
        return total_qty_ordered;
    }

    public void setTotal_qty_ordered(String total_qty_ordered) {
        this.total_qty_ordered = total_qty_ordered;
    }

    public String getOrder_currency() {
        return order_currency;
    }

    public void setOrder_currency(String order_currency) {
        this.order_currency = order_currency;
    }

    public String getCustomer_firstname() {
        return customer_firstname;
    }

    public void setCustomer_firstname(String customer_firstname) {
        this.customer_firstname = customer_firstname;
    }

    public String getCustomer_lastname() {
        return customer_lastname;
    }

    public void setCustomer_lastname(String customer_lastname) {
        this.customer_lastname = customer_lastname;
    }

    public OrderDetailAddress getBilling_address() {
        return billing_address;
    }

    public void setBilling_address(OrderDetailAddress billing_address) {
        this.billing_address = billing_address;
    }

    public OrderDetailAddress getShipping_address() {
        return shipping_address;
    }

    public void setShipping_address(OrderDetailAddress shipping_address) {
        this.shipping_address = shipping_address;
    }

    public OrderDetailPaymentInfo getPayment_info() {
        return payment_info;
    }

    public void setPayment_info(OrderDetailPaymentInfo payment_info) {
        this.payment_info = payment_info;
    }

    public ArrayList<OrderDetailItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<OrderDetailItem> items) {
        this.items = items;
    }

    public String getOrder_created_at() {
        return order_created_at;
    }

    public void setOrder_created_at(String order_created_at) {
        this.order_created_at = order_created_at;
    }
}
