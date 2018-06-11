package com.vechirko.fbsample.ui.post_arch;

import com.vechirko.fbsample.data.model.PostModel;

interface PostView {

    void showLoading(boolean show);

    void showError(String message);

    String getPostId();

    void setData(PostModel data);

    void showEmptyView(boolean show);

    void finish();
}
