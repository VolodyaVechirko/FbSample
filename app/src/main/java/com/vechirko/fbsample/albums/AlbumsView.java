package com.vechirko.fbsample.albums;

import com.vechirko.fbsample.data.model.AlbumModel;

import java.util.Collection;

public interface AlbumsView {

    String getUserId();

    void showLoading(boolean show);

    void showError(String message);

    void setData(Collection<AlbumModel> data);

    void showEmptyView(boolean show);
}
