package com.vechirko.fbsample.ui.posts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.ui.post.PostActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostHolder> {

    List<PostModel> data = new ArrayList<>();

    @Override
    public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(PostHolder holder, int position) {
        PostModel post = data.get(position);
        holder.title.setText(post.getTitle());
        holder.author.setText(post.getUserId());
        holder.body.setText(post.getBody());

        holder.itemView.setOnClickListener(v -> openPost(v.getContext(), post));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void openPost(Context c, PostModel post) {
        c.startActivity(PostActivity.newIntent(c, post.getId()));
    }

    public void setData(Collection<PostModel> list) {
        data = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    static class PostHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView author;
        TextView body;

        public PostHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            body = itemView.findViewById(R.id.body);
        }
    }
}
