package com.vechirko.fbsample.data.repository;

import android.util.Pair;

import java.util.Collection;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.realm.RealmModel;

import static com.vechirko.fbsample.App.isNetworkAvailable;

public class Repository implements RepositoryInterface<RealmModel>, Destroyable {

    public RepositoryApi api;
    public RepositoryDb db;

    //region Lifecycle
    public Repository() {
        api = new RepositoryApi();
        db = new RepositoryDb();
    }

    @Override
    public void onDestroy() {
        db.onDestroy();
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> getAll(Class<T> clazz) {
        return Observable.merge(
                db.getAll(clazz), // get local items
                Observable.defer(() -> isNetworkAvailable()
                        ? api.getAll(clazz).flatMap(r -> db.saveAll(clazz, r))  // or just get response and save
                        : Observable.empty()
                )
        );
    }

    public <T extends RealmModel> Observable<Collection<T>> getAll(Class<T> clazz, boolean cleanFetch) {
        return Observable.merge(
                db.getAll(clazz), // get local items
                Observable.defer(() -> isNetworkAvailable() // if network present call api
                        ? cleanFetch
                        ? db.getAll(clazz)   // get all saved when clean fetch
                        .flatMap(r -> api.getAll(clazz)
                                        .map(coll -> coll), // get newest from api
                                (oldIs, newIs) -> {
                                    oldIs.removeAll(newIs); // subtract new items from saved items
                                    return new Pair<>(oldIs, newIs);    // return (diff, newItems)
                                })
                        .flatMap(pair -> pair.first.isEmpty()
                                        ? Observable.just(pair.first)
                                        : db.removeAll(clazz, pair.first)
                                .map(coll -> coll), // remove difference from db
                                (p, removed) -> p.second)   // return previous new items
                        .flatMap(newIs -> db.saveAll(clazz, newIs)) // and save results to db

                        : api.getAll(clazz).flatMap(r -> db.saveAll(clazz, r))  // or just get response and save

                        : Observable.empty()
                )
        );
    }

    @Override
    public <T extends RealmModel> Observable<Collection<T>> getAll(Request<T> request) {
        return Observable.merge(
                db.getAll(request),
                Observable.defer(() -> isNetworkAvailable()
                        ? api.getAll(request).flatMap(r -> db.saveAll(request.clazz, r))
                            .ignoreElements().andThen(db.getAll(request))
                        : Observable.empty()
                )
        );
    }

    @Override
    public <T extends RealmModel> Request<T> get(Class<T> clazz) {
        return new Request<T>(this, clazz);
    }

    @Override
    public <T extends RealmModel> Observable<Collection<T>> saveAll(Class<T> clazz, Collection<T> elements) {
        return api.saveAll(clazz, elements)
                .flatMap(response -> db.saveAll(clazz, response));
    }

    @Override
    public <T extends RealmModel> Observable<Collection<T>> removeAll(Class<T> clazz, Collection<T> elements) {
        return api.removeAll(clazz, elements)
                .flatMap(es -> db.removeAll(clazz, elements));
    }

    @Override
    public <T extends RealmModel> Observable<Collection<T>> removeAll(Class<T> clazz) {
        return api.removeAll(clazz)
                .flatMap(es -> db.removeAll(clazz));
    }
}
