package com.collage.util.model;

import android.graphics.drawable.Drawable;

public class Photo {

    private Drawable samplePhoto;
    private int widthPx;

    public Photo(Drawable samplePhoto, int width) {
        this.samplePhoto = samplePhoto;
        this.widthPx = width;
    }

    public Drawable getSamplePhoto() {
        return samplePhoto;
    }

    public int getWidthPx() {
        return widthPx;
    }
}
