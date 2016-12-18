package com.collage.model;

import android.graphics.drawable.Drawable;

public class Photo {

    private Drawable samplePhoto;
    private int widthPx;
    private int heightPx;

    public Photo(Drawable samplePhoto, int width, int height) {
        this.samplePhoto = samplePhoto;
        this.widthPx = width;
        this.heightPx = height;
    }

    public Drawable getSamplePhoto() {
        return samplePhoto;
    }

    public int getHeightPx() {
        return heightPx;
    }

    public int getWidthPx() {
        return widthPx;
    }
}
