package com.vechirko.fbsample.ui.posts;

import com.vechirko.fbsample.data.model.PostModel;

import java.util.Collection;

public interface PostsView {

    String getUserId();

    void showLoading(boolean show);

    void showError(String message);

    void setData(Collection<PostModel> data);

    void showEmptyView(boolean show);
}
