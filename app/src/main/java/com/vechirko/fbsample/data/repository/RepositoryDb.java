package com.vechirko.fbsample.data.repository;

import com.vechirko.fbsample.data.model.UserModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class RepositoryDb implements RepositoryInterface<RealmModel>, Destroyable {

    Realm realm;

    public RepositoryDb() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        realm.close();
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> getAll(Class<T> clazz) {
        return realm.where(clazz).findAll()
                .asFlowable().take(1)
                .map(c -> realm.copyFromRealm(c))
                .toObservable();
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> getAll(Request<T> request) {
        RealmQuery<T> query = realm.where(request.clazz);
        for (Map.Entry<String, String> e : request.stringProps.entrySet())
            query.equalTo(e.getKey(), e.getValue());
        for (Map.Entry<String, Long> e : request.longProps.entrySet())
            query.equalTo(e.getKey(), e.getValue());
        if (request.oneOf != null)
            query.in(request.oneOf.first, request.oneOf.second);

        return query.findAll()
                .asFlowable().take(1)
                .map(c -> realm.copyFromRealm(c))
                .toObservable();
    }

    @Override
    public <T extends RealmModel> Request<T> get(Class<T> clazz) {
        return new Request<T>(this, clazz);
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> saveAll(Class<T> clazz, Collection<T> elements) {
        List<T> saved = new ArrayList<T>();
        return Completable.fromAction(() -> {
            realm.executeTransaction(realm -> {
                Observable.fromIterable(realm.copyFromRealm(realm.copyToRealmOrUpdate(elements)))
                        .forEach(saved::add);
            });
        }).andThen(Observable.just(saved));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> removeAll(Class<T> clazz, Collection<T> elements) {
        if (!HasId.class.isAssignableFrom(clazz)) {
            return Observable.error(new IllegalArgumentException(clazz.getSimpleName() + " does not implement HasId<> interface"));
        }

        return Completable.fromAction(() -> {
            realm.executeTransaction(realm -> {
                for (T e : elements) {
                    HasId<String> item = (HasId<String>) e;
                    realm.where(clazz).equalTo(item.getFieldName(), item.getId())
                            .findAll()
                            .deleteAllFromRealm();
                }
            });
        }).andThen(Observable.just(elements));
    }

    @Override
    public <T extends RealmModel> Observable<? extends Collection<T>> removeAll(Class<T> clazz) {
        List<T> removed = new ArrayList<T>();
        return Completable.fromAction(() -> {
            realm.executeTransaction(realm -> {
                Observable.fromIterable(realm.copyFromRealm(realm.where(clazz).findAll()))
                        .forEach(removed::add);
                realm.delete(clazz);
            });
        }).andThen(Observable.just(removed));
    }

    public Completable clear() {
        return Completable.fromAction(() -> {
            realm.executeTransaction(realm -> realm.deleteAll());
        });
    }

    public Flowable<List<UserModel>> getTasks() {
        try (Realm realm = Realm.getDefaultInstance()) {
            RealmQuery<UserModel> query = realm.where(UserModel.class);
            Flowable<RealmResults<UserModel>> flowable;
            if (realm.isAutoRefresh()) { // for looper threads. Use `switchMap()`!
                flowable = query
                        .findAllSortedAsync(UserModel.ID)
                        .asFlowable()
                        .filter(RealmResults::isLoaded);
            } else { // for background threads
                flowable = Flowable.just(query.findAllSorted(UserModel.ID));
            }
            // RealmResults<T> is a list, so we can return it as a List<T>
            // but the compiler needs coercing

            // noinspection unchecked
            return (Flowable) flowable;
        }
    }
}
