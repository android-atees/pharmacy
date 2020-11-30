package in.ateesinfomedia.relief.models.login;

/*
 *Created by Adithya T Raj on 20-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import in.ateesinfomedia.relief.models.products.MediaGalleryEntries;

public class LoginResponse implements Serializable {

    @SerializedName("media_gallery_entries")
    private ArrayList<MediaGalleryEntries> media_gallery_entries;

}
