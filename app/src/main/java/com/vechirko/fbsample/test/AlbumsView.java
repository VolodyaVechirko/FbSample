package com.vechirko.fbsample.test;

import com.vechirko.fbsample.data.test.AlbumContract;

import java.util.Collection;

public interface AlbumsView {

    void showLoading(boolean show);

    void showError(String message);

    void setData(Collection<AlbumContract> data);

    void showEmptyView(boolean show);
}
