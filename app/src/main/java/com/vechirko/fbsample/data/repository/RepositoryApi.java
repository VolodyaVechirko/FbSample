package com.vechirko.fbsample.data.repository;

import com.vechirko.fbsample.data.ServiceGenerator;
import com.vechirko.fbsample.data.model.AlbumModel;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.test.FavouriteAlbum;
import com.vechirko.fbsample.data.test.GeneralAlbum;

import java.util.Collection;
import java.util.Collections;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmModel;

import static com.vechirko.fbsample.data.repository.ObservableUtils.cast;

public class RepositoryApi implements RepositoryInterface<RealmModel> {

    RequestAPI api;

    public RepositoryApi() {
        api = ServiceGenerator.createService(RequestAPI.class);
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> getAll(Class<T> clazz) {
        if (PostModel.class.equals(clazz)) {
            return cast(api.getPosts(null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        } else if (UserModel.class.equals(clazz)) {
            return cast(api.getUsers()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        } else if (GeneralAlbum.class.equals(clazz)) {
            return cast(api.getAllAlbums()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        } else if (FavouriteAlbum.class.equals(clazz)) {
            return cast(api.getFavouriteAlbums()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()));
        } else {
            return Observable.error(new IllegalArgumentException("Method 'api:getAll' not implemented for " + clazz));
        }
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> getAll(Request<T> request) {
        if (PostModel.class.equals(request.clazz)) {
            return request.has(PostModel.ID)
                    ? cast(api.getPost(request.getString(PostModel.ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(om -> om.map(Collections::singletonList))
            )
                    : request.has(PostModel.USER_ID)
                    ? cast(api.getPosts(request.getString(PostModel.USER_ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            )
                    : Observable.error(new IllegalArgumentException("post 'id' not provided!"));
        } else if (UserModel.class.equals(request.clazz)) {
            return request.has(UserModel.ID)
                    ? cast(api.getUser(request.getString(UserModel.ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(umo -> umo.map(Collections::singletonList))
            )
                    : Observable.error(new IllegalArgumentException("user 'id' not provided!"));
        } else if (AlbumModel.class.equals(request.clazz)) {
            return request.has(AlbumModel.ID)
                    ? cast(api.getAlbum(request.getString(AlbumModel.ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(umo -> umo.map(Collections::singletonList))
            )
                    : request.has(AlbumModel.USER_ID)
                    ? cast(api.getAlbums(request.getString(AlbumModel.USER_ID))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            )
                    : Observable.error(new IllegalArgumentException("album 'id' not provided!"));
        } else {
            return Observable.error(new IllegalArgumentException("Method 'api:getAll' not implemented for " + request.clazz));
        }
    }

    @Override
    public <T extends RealmModel> Request<T> get(Class<T> clazz) {
        return new Request<T>(this, clazz);
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> saveAll(Class<T> clazz, Collection<T> elements) {
        if (PostModel.class.equals(clazz)) {
            return cast(elements.isEmpty() ? Observable.just(elements) : Observable.fromIterable(elements)
                    .cast(PostModel.class)
                    .flatMap(p -> api.createPosts(p.toRequestMap()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(om -> om.map(Collections::singletonList))
            );
        } else {
            return Observable.error(new IllegalArgumentException("Method 'api:saveAll' not implemented for " + clazz));
        }
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> removeAll(Class<T> clazz, Collection<T> elements) {
        if (PostModel.class.equals(clazz)) {
            return cast(elements.isEmpty() ? Observable.just(elements) : Observable.fromIterable(elements)
                    .cast(PostModel.class)
                    .flatMap(n -> api.deletePost(n.getId()), (post, aVoid) -> post)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .to(om -> om.map(Collections::singletonList))
            );
        } else {
            return Observable.error(new IllegalArgumentException("Method 'api:removeAll' not implemented for " + clazz));
        }
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> removeAll(Class<T> clazz) {
        return Observable.error(new IllegalArgumentException("Method 'api:removeAll' not implemented for " + clazz));
    }
}
