package com.vechirko.fbsample.post;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

public class PostPresenter implements Destroyable {

    PostView view;
    Repository repository;

    public PostPresenter(PostView view) {
        this.view = view;
        repository = new Repository();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
    }

    public void getPost() {
        view.showLoading(true);
        repository.get(PostModel.class)
                .where(PostModel.ID, view.getPostId())
                .findAll()
                .subscribe(coll -> {
                            if (coll.isEmpty()) {
                                view.showEmptyView(true);
                            } else {
                                view.setData(coll.iterator().next());
                            }
                        },
                        Errors.handle(view::showError),
                        () -> view.showLoading(false)
                );
    }

    public void removePost() {
        view.showLoading(true);
        repository.get(PostModel.class)
                .where(PostModel.ID, view.getPostId())
                .findAll()
                .filter(coll-> !coll.isEmpty())
                .flatMap(coll -> repository.removeAll(PostModel.class, coll))
                .ignoreElements()
                .subscribe(
                        () -> {
                            view.showLoading(false);
                            view.finish();
                        },
                        Errors.handle(view::showError)
                );
    }
}
