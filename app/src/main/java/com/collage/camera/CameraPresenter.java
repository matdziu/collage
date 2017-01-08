package com.collage.camera;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class CameraPresenter {

    private File imageFile;

    void createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        imageFile = File.createTempFile(imageFileName, ".jpg", galleryFolder);
    }

    File getImageFile() {
        return imageFile;
    }
}
