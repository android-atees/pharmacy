package in.ateesinfomedia.relief.models.shipping;

/*
 *Created by Adithya T Raj on 10-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShippingTotal implements Serializable {

    @SerializedName("total_segments")
    private ArrayList<TotalSegments> total_segments;

    public ArrayList<TotalSegments> getTotal_segments() {
        return total_segments;
    }

    public void setTotal_segments(ArrayList<TotalSegments> total_segments) {
        this.total_segments = total_segments;
    }
}
