package pl.collage.sendimage;

import android.net.Uri;

import pl.collage.base.BaseListener;
import pl.collage.util.models.User;

public interface SendImageListener extends BaseListener<User> {

    void onImageUploadStarted(User friendStarted);

    void onImageUploadFinished(Uri downloadUrl, User friendFinished);
}
