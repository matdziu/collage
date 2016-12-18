package com.collage.model;

import android.graphics.drawable.Drawable;

public class Photo {

    private Drawable samplePhoto;

    public Photo(Drawable samplePhoto) {
        this.samplePhoto = samplePhoto;
    }

    public Drawable getSamplePhoto() {
        return samplePhoto;
    }
}
