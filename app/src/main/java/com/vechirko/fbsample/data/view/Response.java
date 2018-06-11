package com.vechirko.fbsample.data.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.vechirko.fbsample.data.view.Status.ERROR;
import static com.vechirko.fbsample.data.view.Status.LOADING;
import static com.vechirko.fbsample.data.view.Status.SUCCESS;

public class Response<T> {
    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private Response(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Response<T> success(@NonNull T data) {
        return new Response<>(SUCCESS, data, null);
    }

    public static <T> Response<T> error(String msg) {
        return new Response<>(ERROR, null, msg);
    }

    public static <T> Response<T> loading() {
        return new Response<>(LOADING, null, null);
    }
}