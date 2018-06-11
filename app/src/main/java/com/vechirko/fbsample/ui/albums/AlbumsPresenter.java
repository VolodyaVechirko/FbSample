package com.vechirko.fbsample.ui.albums;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.AlbumModel;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;

import io.reactivex.disposables.CompositeDisposable;

public class AlbumsPresenter implements Destroyable {

    AlbumsView view;
    Repository repository;
    CompositeDisposable disposable;

    public AlbumsPresenter(AlbumsView view) {
        this.view = view;
        repository = new Repository();
        disposable = new CompositeDisposable();
    }

    @Override
    public void onDestroy() {
        repository.onDestroy();
        disposable.clear();
    }

    public void getAlbums(String userId) {
        view.showLoading(true);
        disposable.add(
                repository.get(AlbumModel.class)
                        .where(AlbumModel.USER_ID, userId)
                        .findAll()
                        .subscribe(
                                view::setData,
                                Errors.handle(view::showError),
                                () -> view.showLoading(false)
                        )
        );
    }
}
