package com.vechirko.fbsample.data.repository;

import com.vechirko.fbsample.data.model.PostModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class ObservableUtils {

    @SuppressWarnings("unchecked")
    public static <T> Observable<List<T>> cast(Observable<? extends Collection<?>> obs) {
        return obs.map(ArrayList::new)
                .map(i -> (List<T>) i);
    }
}
