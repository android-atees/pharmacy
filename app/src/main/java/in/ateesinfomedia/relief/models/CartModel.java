package in.ateesinfomedia.relief.models;

public class CartModel {

    String id;
    String quantity;
    String product_name;
    String product_total;
    String product_total_offer;
    String product_image1;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_total() {
        return product_total;
    }

    public void setProduct_total(String product_total) {
        this.product_total = product_total;
    }

    public String getProduct_total_offer() {
        return product_total_offer;
    }

    public void setProduct_total_offer(String product_total_offer) {
        this.product_total_offer = product_total_offer;
    }

    public String getProduct_image1() {
        return product_image1;
    }

    public void setProduct_image1(String product_image1) {
        this.product_image1 = product_image1;
    }
}
