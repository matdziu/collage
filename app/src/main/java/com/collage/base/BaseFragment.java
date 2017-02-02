package com.collage.base;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.collage.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BaseFragment extends Fragment {

    protected File imageFile;
    public static final String IMAGE_FILE_PATH = "imageFilePath";
    public static final String IMAGE_FILE_NAME = "imageFileName";
    public static final int REQUEST_SEND_IMAGE = 1;
    public static final int RESULT_PICTURE_SENT = 1;

    protected void closeSoftKeyboard() {
        View focusedView = getActivity()
                .getWindow()
                .getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    protected View getDecorView() {
        return getActivity()
                .getWindow()
                .getDecorView();
    }

    protected void hideSystemUI() {
        getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    protected void showSystemUI() {
        getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected File createImageGallery() {
        File storageDirectory = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File galleryFolder = new File(storageDirectory, getResources().getString(R.string.app_name));
        if (!galleryFolder.exists()) {
            galleryFolder.mkdirs();
        }
        return galleryFolder;
    }

    protected File createImageFile(File galleryFolder) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "image_" + timeStamp + "_";
        return File.createTempFile(imageFileName, ".jpg", galleryFolder);
    }
}
