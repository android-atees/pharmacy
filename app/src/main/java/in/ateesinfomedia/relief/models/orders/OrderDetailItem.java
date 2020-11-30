package in.ateesinfomedia.relief.models.orders;

/*
 *Created by Adithya T Raj on 12-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderDetailItem implements Serializable {

    @SerializedName("item_id")
    private String item_id;

    @SerializedName("sku")
    private String sku;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("product_type")
    private String product_type;

    @SerializedName("qty_ordered")
    private String qty_ordered;

    @SerializedName("image")
    private String image;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getQty_ordered() {
        return qty_ordered;
    }

    public void setQty_ordered(String qty_ordered) {
        this.qty_ordered = qty_ordered;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
