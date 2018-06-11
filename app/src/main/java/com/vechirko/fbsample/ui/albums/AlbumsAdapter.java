package com.vechirko.fbsample.ui.albums;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vechirko.fbsample.R;
import com.vechirko.fbsample.data.model.AlbumModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumHolder> {

    List<AlbumModel> data = new ArrayList<>();

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album, parent, false));
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {
        AlbumModel album = data.get(position);
        holder.title.setText(album.getTitle());

        holder.itemView.setOnClickListener(v -> openAlbum(v.getContext(), album));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void openAlbum(Context c, AlbumModel album) {
//        c.startActivity(PostActivity.newIntent(c, album.getId()));
    }

    public void setData(Collection<AlbumModel> list) {
        data = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    static class AlbumHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView title;

        public AlbumHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.title);
        }
    }
}