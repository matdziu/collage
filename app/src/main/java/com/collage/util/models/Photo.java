package com.collage.util.models;

import org.parceler.Parcel;

@Parcel
public class Photo {

    public String imageUrl;

    @SuppressWarnings("unused")
    public Photo() {
        // default constructor for Parceler
    }

    public Photo(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
