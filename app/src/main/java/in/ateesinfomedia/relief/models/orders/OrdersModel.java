package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 11-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrdersModel implements Serializable {

    @SerializedName("entity_id")
    private String entity_id;

    @SerializedName("grand_total")
    private String grand_total;

    @SerializedName("increment_id")
    private String increment_id;

    @SerializedName("status")
    private String status;

    @SerializedName("extension_attributes")
    private OrderExtensionAttributes extension_attributes;

    public String getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public String getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(String grand_total) {
        this.grand_total = grand_total;
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

    public OrderExtensionAttributes getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(OrderExtensionAttributes extension_attributes) {
        this.extension_attributes = extension_attributes;
    }
}
