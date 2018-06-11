package com.vechirko.fbsample;

import com.vechirko.fbsample.data.repository.Repository;
import com.vechirko.fbsample.data.repository.RepositoryInterface;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.RealmModel;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public static RepositoryInterface<RealmModel> provideRepository() {
        return new Repository();
    }
}
