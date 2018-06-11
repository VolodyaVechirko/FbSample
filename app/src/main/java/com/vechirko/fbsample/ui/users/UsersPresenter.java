package com.vechirko.fbsample.ui.users;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class UsersPresenter implements Destroyable {

    UsersView view;
    Repository repository;
    CompositeDisposable disposable;

    public UsersPresenter(UsersView view) {
        this.view = view;
        repository = new Repository();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
        disposable.clear();
    }

    public void getUsers() {
        view.showLoading(true);
        disposable.add(
                repository.getAll(UserModel.class)
                        .subscribe(
                                view::setData,
                                Errors.handle(view::showError),
                                () -> view.showLoading(false)
                        )
        );
    }
}
