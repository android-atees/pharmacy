package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderExtensionAttributes implements Serializable {

    @SerializedName("shipping_assignments")
    private ArrayList<OrderShippingAssignments> shipping_assignments;

    @SerializedName("payment_additional_info")
    private ArrayList<OrderPaymentInfo> payment_additional_info;

    public ArrayList<OrderShippingAssignments> getShipping_assignments() {
        return shipping_assignments;
    }

    public void setShipping_assignments(ArrayList<OrderShippingAssignments> shipping_assignments) {
        this.shipping_assignments = shipping_assignments;
    }

    public ArrayList<OrderPaymentInfo> getPayment_additional_info() {
        return payment_additional_info;
    }

    public void setPayment_additional_info(ArrayList<OrderPaymentInfo> payment_additional_info) {
        this.payment_additional_info = payment_additional_info;
    }
}
