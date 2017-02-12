package pl.collage.gallerydetail;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import pl.collage.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static pl.collage.util.adapters.PhotosDetailAdapter.EXTRAS_CACHED_BITMAP;
import static pl.collage.util.adapters.PhotosDetailAdapter.EXTRAS_PHOTO_URL;

public class GalleryDetailFragment extends Fragment {

    @BindView(R.id.gallery_detail_image_view)
    ImageView imageView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String photoUrl = getArguments().getString(EXTRAS_PHOTO_URL);
        Bitmap bitmap = getArguments().getParcelable(EXTRAS_CACHED_BITMAP);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(photoUrl)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model,
                                                       Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                       boolean isFirstResource) {
                            imageView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onException(Exception e, String model,
                                                   Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }
}
