package com.vechirko.fbsample.data.repository;

/**
 * Helper for Base repository interface
 * Used to identify Repository model to delete it from Realm db
 *
 * @param <T> base type for model id
 */

public interface HasId<T> {

    T getId();

    default String getFieldName() {
        return "id";
    }
}
