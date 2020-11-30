package in.ateesinfomedia.relief.models.slider;

/*
 *Created by Adithya T Raj on 24-11-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeBanner implements Serializable {

    @SerializedName("banners_id")
    private String banners_id;

    @SerializedName("banner_image_mobile")
    private String banner_image_mobile;

    public String getBanners_id() {
        return banners_id;
    }

    public void setBanners_id(String banners_id) {
        this.banners_id = banners_id;
    }

    public String getBanner_image_mobile() {
        return banner_image_mobile;
    }

    public void setBanner_image_mobile(String banner_image_mobile) {
        this.banner_image_mobile = banner_image_mobile;
    }
}
