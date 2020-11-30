package in.ateesinfomedia.relief.models.shipping;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShippingInfo implements Serializable {

    @SerializedName("payment_methods")
    private ArrayList<PaymentMethod> payment_methods;

    @SerializedName("totals")
    private ShippingTotal totals;

    public ArrayList<PaymentMethod> getPayment_methods() {
        return payment_methods;
    }

    public void setPayment_methods(ArrayList<PaymentMethod> payment_methods) {
        this.payment_methods = payment_methods;
    }

    public ShippingTotal getTotals() {
        return totals;
    }

    public void setTotals(ShippingTotal totals) {
        this.totals = totals;
    }
}
