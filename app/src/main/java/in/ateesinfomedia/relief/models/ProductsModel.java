package in.ateesinfomedia.relief.models;

import java.io.Serializable;

public class ProductsModel implements Serializable{

    String id;
    String brand_id;
    String product_name;
    String sub_category_name;
    String product_description;
    String product_highlights;
    String product_quantity;
    String product_price;
    String product_offer_price;
    String product_shipping_price;
    String product_sku;
    String product_gst;
    String product_hsn;
    String product_priority_points;
    String product_meta_tags;
    String product_meta_description;
    String product_meta_keywords;
    String product_image1;
    String product_image2;
    String product_image3;
    String product_image4;
    String create_date;
    String recentlyadded;
    String topsellted;
    String toprated;
    String popular;
    String product_total;
    String product_total_offer;
    String gst_amount;
    String sale_count;
    String view_count;
    String offer;
    String isCart;

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getIsCart() {
        return isCart;
    }

    public void setIsCart(String isCart) {
        this.isCart = isCart;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public String getProduct_highlights() {
        return product_highlights;
    }

    public void setProduct_highlights(String product_highlights) {
        this.product_highlights = product_highlights;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setProduct_quantity(String product_quantity) {
        this.product_quantity = product_quantity;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_offer_price() {
        return product_offer_price;
    }

    public void setProduct_offer_price(String product_offer_price) {
        this.product_offer_price = product_offer_price;
    }

    public String getProduct_shipping_price() {
        return product_shipping_price;
    }

    public void setProduct_shipping_price(String product_shipping_price) {
        this.product_shipping_price = product_shipping_price;
    }

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public String getProduct_gst() {
        return product_gst;
    }

    public void setProduct_gst(String product_gst) {
        this.product_gst = product_gst;
    }

    public String getProduct_hsn() {
        return product_hsn;
    }

    public void setProduct_hsn(String product_hsn) {
        this.product_hsn = product_hsn;
    }

    public String getProduct_priority_points() {
        return product_priority_points;
    }

    public void setProduct_priority_points(String product_priority_points) {
        this.product_priority_points = product_priority_points;
    }

    public String getProduct_meta_tags() {
        return product_meta_tags;
    }

    public void setProduct_meta_tags(String product_meta_tags) {
        this.product_meta_tags = product_meta_tags;
    }

    public String getProduct_meta_description() {
        return product_meta_description;
    }

    public void setProduct_meta_description(String product_meta_description) {
        this.product_meta_description = product_meta_description;
    }

    public String getProduct_meta_keywords() {
        return product_meta_keywords;
    }

    public void setProduct_meta_keywords(String product_meta_keywords) {
        this.product_meta_keywords = product_meta_keywords;
    }

    public String getProduct_image1() {
        return product_image1;
    }

    public void setProduct_image1(String product_image1) {
        this.product_image1 = product_image1;
    }

    public String getProduct_image2() {
        return product_image2;
    }

    public void setProduct_image2(String product_image2) {
        this.product_image2 = product_image2;
    }

    public String getProduct_image3() {
        return product_image3;
    }

    public void setProduct_image3(String product_image3) {
        this.product_image3 = product_image3;
    }

    public String getProduct_image4() {
        return product_image4;
    }

    public void setProduct_image4(String product_image4) {
        this.product_image4 = product_image4;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getRecentlyadded() {
        return recentlyadded;
    }

    public void setRecentlyadded(String recentlyadded) {
        this.recentlyadded = recentlyadded;
    }

    public String getTopsellted() {
        return topsellted;
    }

    public void setTopsellted(String topsellted) {
        this.topsellted = topsellted;
    }

    public String getToprated() {
        return toprated;
    }

    public void setToprated(String toprated) {
        this.toprated = toprated;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
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

    public String getGst_amount() {
        return gst_amount;
    }

    public void setGst_amount(String gst_amount) {
        this.gst_amount = gst_amount;
    }

    public String getSale_count() {
        return sale_count;
    }

    public void setSale_count(String sale_count) {
        this.sale_count = sale_count;
    }

    public String getView_count() {
        return view_count;
    }

    public void setView_count(String view_count) {
        this.view_count = view_count;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
