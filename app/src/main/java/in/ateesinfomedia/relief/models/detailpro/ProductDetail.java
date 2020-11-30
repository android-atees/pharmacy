package in.ateesinfomedia.relief.models.detailpro;

/*
 *Created by Adithya T Raj on 25-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;

public class ProductDetail implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("sku")
    private String sku;

    @SerializedName("name")
    private String name;

    @SerializedName("attribute_set_id")
    private String attribute_set_id;

    @SerializedName("price")
    private String price;

    @SerializedName("status")
    private String status;

    @SerializedName("visibility")
    private String visibility;

    @SerializedName("type_id")
    private String type_id;

    @SerializedName("is_in_stock")
    private Boolean is_in_stock;

    @SerializedName("extension_attributes")
    private ExtensionAttributes extension_attributes;

    @SerializedName("media_gallery_entries")
    private ArrayList<MediaGalleryEntries> media_gallery_entries;

    @SerializedName("custom_attributes")
    private ArrayList<CustomAttributes> custom_attributes;

    @SerializedName("configurable_product_options")
    private ArrayList<ConfigProductOptions> configurable_product_options;

    @SerializedName("options_price")
    private ArrayList<OptionsPrice> options_price;

    @SerializedName("message")
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAttribute_set_id() {
        return attribute_set_id;
    }

    public void setAttribute_set_id(String attribute_set_id) {
        this.attribute_set_id = attribute_set_id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public Boolean getIs_in_stock() {
        return is_in_stock;
    }

    public void setIs_in_stock(Boolean is_in_stock) {
        this.is_in_stock = is_in_stock;
    }

    public ExtensionAttributes getExtension_attributes() {
        return extension_attributes;
    }

    public void setExtension_attributes(ExtensionAttributes extension_attributes) {
        this.extension_attributes = extension_attributes;
    }

    public ArrayList<MediaGalleryEntries> getMedia_gallery_entries() {
        return media_gallery_entries;
    }

    public void setMedia_gallery_entries(ArrayList<MediaGalleryEntries> media_gallery_entries) {
        this.media_gallery_entries = media_gallery_entries;
    }

    public ArrayList<CustomAttributes> getCustom_attributes() {
        return custom_attributes;
    }

    public void setCustom_attributes(ArrayList<CustomAttributes> custom_attributes) {
        this.custom_attributes = custom_attributes;
    }

    public ArrayList<ConfigProductOptions> getConfigurable_product_options() {
        return configurable_product_options;
    }

    public void setConfigurable_product_options(ArrayList<ConfigProductOptions> configurable_product_options) {
        this.configurable_product_options = configurable_product_options;
    }

    public ArrayList<OptionsPrice> getOptions_price() {
        return options_price;
    }

    public void setOptions_price(ArrayList<OptionsPrice> options_price) {
        this.options_price = options_price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
