package com.vechirko.fbsample.test;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.repository.Destroyable;
import com.vechirko.fbsample.data.repository.Repository;
import com.vechirko.fbsample.data.test.AlbumContract;
import com.vechirko.fbsample.data.test.FavouriteAlbum;
import com.vechirko.fbsample.data.test.GeneralAlbum;

import java.util.Collection;
import java.util.Collections;

import io.reactivex.Observable;
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

    public void getAll() {
        view.showLoading(true);
        disposable.add(
                repository.getAll(GeneralAlbum.class)
                        .cast(Collection.class)
                        .subscribe(
                                view::setData,
                                Errors.handle(view::showError),
                                () -> view.showLoading(false)
                        )
        );
    }

    public void getFavourite() {
        view.showLoading(true);
        disposable.add(
                repository.getAll(FavouriteAlbum.class)
                        .flatMap(Observable::fromIterable)
                        .cast(AlbumContract.class)
                        .to(om -> om.map(Collections::singletonList))
                        .subscribe(
                                view::setData,
                                Errors.handle(view::showError),
                                () -> view.showLoading(false)
                        )
        );
    }
}
