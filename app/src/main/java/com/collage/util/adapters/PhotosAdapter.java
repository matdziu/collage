package com.collage.util.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
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

    static SparseArray<Bitmap> cachedPhotoArray = new SparseArray<>();

    private List<Photo> photoList;
    private Point size;
    private Context context;
    public static final String EXTRAS_PHOTO_LIST = "photoList";
    public static final String EXTRAS_CURRENT_PHOTO_POSITION = "currentPhotoPosition";

    public PhotosAdapter(List<Photo> photoList, Point size, Context context) {
        this.photoList = photoList;
        this.size = size;
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
        setImageViewBounds(holder.imageView, size);
        holder.progressBar.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(photoList.get(position).imageUrl)
                .asBitmap()
                .override(size.x, size.y)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onResourceReady(Bitmap resource, String model,
                                                   Target<Bitmap> target, boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        cachedPhotoArray.put(holder.getAdapterPosition(), resource);
                        return false;
                    }

                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target,
                                               boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GalleryDetailActivity.class);
                intent.putExtra(EXTRAS_PHOTO_LIST, Parcels.wrap(photoList));
                intent.putExtra(EXTRAS_CURRENT_PHOTO_POSITION, holder.getAdapterPosition());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private void setImageViewBounds(ImageView imageView, Point size) {
        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = size.x / 3;
        params.height = size.x / 3;
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
