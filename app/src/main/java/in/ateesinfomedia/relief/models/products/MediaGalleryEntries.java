package in.ateesinfomedia.relief.models.products;

/*
 *Created by Adithya T Raj on 18-10-2020
 */

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaGalleryEntries implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("media_type")
    private String media_type;

    @SerializedName("label")
    private String label;

    @SerializedName("position")
    private String position;

    @SerializedName("disabled")
    private Boolean disabled;

    @SerializedName("types")
    private ArrayList<String> types;

    @SerializedName("file")
    private String file;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
