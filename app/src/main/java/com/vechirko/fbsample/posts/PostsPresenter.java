package com.vechirko.fbsample.posts;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

public class PostsPresenter implements Destroyable {

    PostsView view;
    Repository repository;

    public PostsPresenter(PostsView view) {
        this.view = view;
        repository = new Repository();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
    }

    public void getPosts() {
        view.showLoading(true);
        repository.getAll(PostModel.class)
                .subscribe(
                        view::setData,
                        Errors.handle(view::showError),
                        () -> view.showLoading(false));
    }
}
