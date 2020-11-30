package in.ateesinfomedia.relief.models.detailpro;

/*
 *Created by Adithya T Raj on 25-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class OptionsPrice implements Serializable {

    @SerializedName("options")
    private ArrayList<String> options;

    @SerializedName("price")
    private String price;

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
