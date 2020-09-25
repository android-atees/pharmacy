package in.ateesinfomedia.remedio.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class TrackingModel implements Serializable {

    double lati;
    double longi;
    LatLng latLong;

    public LatLng getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLng latLong) {
        this.latLong = latLong;
    }

    public double getLati() {
        return lati;
    }

    public void setLati(double lati) {
        this.lati = lati;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}
