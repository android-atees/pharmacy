package in.ateesinfomedia.relief.models.cart;

/*
 *Created by Adithya T Raj on 26-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CartModel implements Serializable {

    @SerializedName("item_id")
    private String item_id;

    @SerializedName("sku")
    private String sku;

    @SerializedName("qty")
    private String qty;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("product_type")
    private String product_type;

    @SerializedName("quote_id")
    private String quote_id;

    @SerializedName("image")
    private String image;

    @SerializedName("options")
    private CartOptions options;

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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
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

    public String getQuote_id() {
        return quote_id;
    }

    public void setQuote_id(String quote_id) {
        this.quote_id = quote_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CartOptions getOptions() {
        return options;
    }

    public void setOptions(CartOptions options) {
        this.options = options;
    }
}
