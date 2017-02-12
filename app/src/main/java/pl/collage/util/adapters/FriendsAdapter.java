package pl.collage.util.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.collage.R;
import pl.collage.friends.FriendsListener;
import pl.collage.util.models.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    private List<User> friendList;
    private FriendsListener friendsListener;
    private Context context;
    private User currentlySelectedFriend;

    public FriendsAdapter(List<User> friendList, FriendsListener friendsListener,
                          Context context) {
        this.friendList = friendList;
        this.friendsListener = friendsListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (currentlySelectedFriend != null && friendList.get(position).uid
                .equals(currentlySelectedFriend.uid)) {
            holder.layout.setBackgroundColor(ContextCompat.getColor(
                    context, R.color.colorAccent));
        } else {
            holder.layout.setBackgroundColor(ContextCompat.getColor(
                    context, android.R.color.white));
        }
        holder.textView.setText(friendList.get(position).fullName);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentlySelectedFriend = friendList.get(holder.getAdapterPosition());
                friendsListener.onFriendSelected(
                        friendList.get(holder.getAdapterPosition()));
                notifyDataSetChanged();

                PhotosAdapter.cachedPhotoArray.clear();
            }
        });
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public void setFriendList(List<User> friendList) {
        this.friendList = friendList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.friends_text_view)
        TextView textView;

        @BindView(R.id.layout_item_friend)
        ViewGroup layout;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
