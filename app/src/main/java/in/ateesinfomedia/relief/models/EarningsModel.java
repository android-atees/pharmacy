package in.ateesinfomedia.relief.models;

import java.io.Serializable;

public class EarningsModel implements Serializable{

    String name;
    String phone;
    String create_date;
    String ref_amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getRef_amount() {
        return ref_amount;
    }

    public void setRef_amount(String ref_amount) {
        this.ref_amount = ref_amount;
    }
}
