package in.ateesinfomedia.relief.models;

import java.io.Serializable;

public class ConstituencyModel implements Serializable {

    String id;
    String district_id;
    String constituency_name;
    String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public String getConstituency_name() {
        return constituency_name;
    }

    public void setConstituency_name(String constituency_name) {
        this.constituency_name = constituency_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}