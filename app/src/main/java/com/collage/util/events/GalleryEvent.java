package com.collage.util.events;

public class GalleryEvent {

    private String albumStorageId;

    public GalleryEvent(String albumStorageId) {
        this.albumStorageId = albumStorageId;
    }

    public String getAlbumStorageId() {
        return albumStorageId;
    }
}
