package in.ateesinfomedia.relief.models.detailpro;

/*
 *Created by Adithya T Raj on 25-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExtensionAttributes implements Serializable {

    @SerializedName("stock_item")
    private StockItem stock_item;

    public StockItem getStock_item() {
        return stock_item;
    }

    public void setStock_item(StockItem stock_item) {
        this.stock_item = stock_item;
    }
}
