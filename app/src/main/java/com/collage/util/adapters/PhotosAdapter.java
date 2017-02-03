package com.collage.util.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.collage.R;
import com.collage.gallerydetail.GalleryDetailActivity;
import com.collage.util.models.Photo;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.ViewHolder> {

    private List<Photo> photoList;
    private int imageWidth;
    private Context context;
    public static final String EXTRAS_PHOTO_LIST = "photoList";

    public PhotosAdapter(List<Photo> photoList, int imageWidth, Context context) {
        this.photoList = photoList;
        this.imageWidth = imageWidth;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setImageViewBounds(holder.imageView, imageWidth);
        holder.progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(photoList.get(position).imageUrl)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleryDetailActivity.class);
                intent.putExtra(EXTRAS_PHOTO_LIST, Parcels.wrap(photoList));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setImageViewBounds(ImageView imageView, int imageWidth) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = imageWidth;
        params.height = imageWidth;
        imageView.setLayoutParams(params);
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_photo_image_view)
        ImageView imageView;

        @BindView(R.id.item_photo_progress_bar)
        ProgressBar progressBar;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
