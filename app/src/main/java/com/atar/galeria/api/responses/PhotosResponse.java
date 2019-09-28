package com.atar.galeria.api.responses;

import com.atar.galeria.db.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotosResponse {

    @SerializedName("items")
    private List<Photo> photos;
    public List<Photo> getPhotos() {
        return photos;
    }
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
     * "success" is true = "There was a successful fetch, HOORAH!"
     * "success" is false = "There was a fetch and it failed..."
     * "success" is null = "There was no fetch yet ¯\_(ツ)_/¯"
     */
    @SerializedName("success")
    private boolean success;
    public boolean isSuccess() {
        return success;
    }
    public void setSuccess(boolean success) {
        this.success = success;
    }
}
