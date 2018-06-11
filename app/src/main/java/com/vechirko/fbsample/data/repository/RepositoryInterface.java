package com.vechirko.fbsample.data.repository;

import android.util.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Base repository interface
 *
 * @param <S> base type for repository models
 */

public interface RepositoryInterface<S> {

    /**
     * Get all elements of some type
     *
     * @param <T>   elements type
     * @param clazz elements class
     * @return found elements observable
     */
    <T extends S> Observable<? extends Collection<T>> getAll(Class<T> clazz);

    /**
     * Get all elements specified by request params
     *
     * @param <T>     elements type
     * @param request parametrised request
     * @return found elements observable
     */
    <T extends S> Observable<? extends Collection<T>> getAll(Request<T> request);

    /**
     * Parametrized request getter
     *
     * @param clazz elements class
     * @param <T>   elements type
     * @return new request
     */
    <T extends S> Request<T> get(Class<T> clazz);

    /**
     * Save new items to repository, overwrites if already exist.
     *
     * @param clazz    elements class
     * @param <T>      elements type
     * @param elements element collection
     * @return saved items observable
     */
    <T extends S> Observable<? extends Collection<T>> saveAll(Class<T> clazz, Collection<T> elements);

    /**
     * Remove items collection from repository
     *
     * @param clazz    elements class
     * @param <T>      elements type
     * @param elements element collection to remove
     * @return elements observable
     */
    <T extends S> Observable<? extends Collection<T>> removeAll(Class<T> clazz, Collection<T> elements);

    /**
     * Remove all items type {@code T} from repository
     *
     * @param clazz elements class
     * @param <T>   elements type
     * @return removed items observable
     */
    <T extends S> Observable<? extends Collection<T>> removeAll(Class<T> clazz);

    /**
     * Parametrised request builder
     *
     * @param <T> elements type
     */
    class Request<T> {
        RepositoryInterface<? super T> repo;
        Map<String, String> stringProps = new HashMap<>();
        Map<String, Long> longProps = new HashMap<>();
        Pair<String, String[]> oneOf;
        Class<T> clazz;

        Request(RepositoryInterface<? super T> repo, Class<T> clazz) {
            this.clazz = clazz;
            this.repo = repo;
        }

        /**
         * Checks if property is set
         *
         * @param prop property name
         * @return true if property already set, false otherwise
         */
        protected boolean has(String prop) {
            return stringProps.get(prop) != null || longProps.get(prop) != null;
        }

        public String getString(String prop) {
            return stringProps.get(prop);
        }

        public Long getLong(String prop) {
            return longProps.get(prop);
        }

        /**
         * String property setter
         *
         * @param property
         * @param value
         * @return updated request
         */
        public Request<T> where(String property, String value) {
            stringProps.put(property, value);
            return this;
        }

        /**
         * Integer property setter
         *
         * @param property
         * @param value
         * @return updated request
         */
        public Request<T> where(String property, long value) {
            longProps.put(property, value);
            return this;
        }

        /**
         * OneOf property setter
         *
         * @param property
         * @param values
         * @return updated request
         */
        public Request<T> hasOneOf(String property, String... values) {
            oneOf = new Pair<>(property, values);
            return this;
        }

        /**
         * Request trigger
         *
         * @return results observable
         */
        public Observable<? extends Collection<T>> findAll() {
            return repo.getAll(this);
        }

        @Override
        public String toString() {
            return "Request{" +
                    "stringProps=" + stringProps +
                    ", longProps=" + longProps +
                    '}';
        }
    }
}

