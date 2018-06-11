package com.vechirko.fbsample.ui.user;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class UserPresenter implements Destroyable {

    UserView view;
    Repository repository;
    CompositeDisposable disposable;

    public UserPresenter(UserView view) {
        this.view = view;
        repository = new Repository();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
        disposable.clear();
    }

    public void getUser(String userId) {
        view.showLoading(true);
        disposable.add(repository.get(UserModel.class)
                .where(UserModel.ID, userId)
                .findAll()
                .map(coll -> coll.iterator().next())
                .subscribe(
                        view::setData,
                        Errors.handle(view::showError),
                        () -> view.showLoading(false)
                )
        );
    }
}
