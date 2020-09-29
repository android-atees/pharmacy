package in.ateesinfomedia.relief.models;

import java.io.Serializable;

public class DoctorsDetailModel implements Serializable {

    String id;
    String hospital_name;
    String hospital_address;
    String consulting_days;
    String consulting_time;
    String hospital_phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHospital_address() {
        return hospital_address;
    }

    public void setHospital_address(String hospital_address) {
        this.hospital_address = hospital_address;
    }

    public String getConsulting_days() {
        return consulting_days;
    }

    public void setConsulting_days(String consulting_days) {
        this.consulting_days = consulting_days;
    }

    public String getConsulting_time() {
        return consulting_time;
    }

    public void setConsulting_time(String consulting_time) {
        this.consulting_time = consulting_time;
    }

    public String getHospital_phone() {
        return hospital_phone;
    }

    public void setHospital_phone(String hospital_phone) {
        this.hospital_phone = hospital_phone;
    }
}
