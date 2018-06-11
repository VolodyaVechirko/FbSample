package com.vechirko.fbsample.ui.posts;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class PostsPresenter implements Destroyable {

    PostsView view;
    Repository repository;
    CompositeDisposable disposable;

    public PostsPresenter(PostsView view) {
        this.view = view;
        repository = new Repository();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
        disposable.clear();
    }

    public void getPosts() {
        view.showLoading(true);
        disposable.add(
                repository.getAll(PostModel.class)
                        .subscribe(
                                view::setData,
                                Errors.handle(view::showError),
                                () -> view.showLoading(false)
                        )
        );
    }
}
