package com.vechirko.fbsample.data.view;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.vechirko.fbsample.data.view.Status.ERROR;
import static com.vechirko.fbsample.data.view.Status.LOADING;
import static com.vechirko.fbsample.data.view.Status.SUCCESS;

public class Action {

    @NonNull
    public final Status status;
    @Nullable
    public final String message;

    private Action(@NonNull Status status, @Nullable String message) {
        this.status = status;
        this.message = message;
    }

    public static Action success() {
        return new Action(SUCCESS, null);
    }

    public static Action error(String msg) {
        return new Action(ERROR, msg);
    }

    public static Action loading() {
        return new Action(LOADING, null);
    }
}
