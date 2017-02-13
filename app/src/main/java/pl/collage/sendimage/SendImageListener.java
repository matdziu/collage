package pl.collage.sendimage;

import pl.collage.base.BaseListener;
import pl.collage.util.models.Photo;
import pl.collage.util.models.User;

public interface SendImageListener extends BaseListener<User> {

    void onImageUploadStarted(User friendStarted);

    void onImageUploadFinished(Photo uploadedPhoto, User friendFinished);
}
