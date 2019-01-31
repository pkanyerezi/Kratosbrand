package com.brand.Kratos.model.PostTags;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagsArray {

    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;
    @SerializedName("platform")
    @Expose
    private String platform;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

}