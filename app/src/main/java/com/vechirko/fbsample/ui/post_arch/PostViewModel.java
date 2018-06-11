package com.vechirko.fbsample.ui.post_arch;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.vechirko.fbsample.data.Errors;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.repository.Repository;
import com.vechirko.fbsample.data.view.Action;
import com.vechirko.fbsample.data.view.Response;

import io.reactivex.disposables.CompositeDisposable;

@SuppressWarnings("WeakerAccess")
public class PostViewModel extends ViewModel {

    String postId;
    MutableLiveData<Response<PostModel>> postData;
    MutableLiveData<Action> removeData;

    Repository repository;
    CompositeDisposable disposable;

    public PostViewModel() {
        repository = new Repository();
        disposable = new CompositeDisposable();
        postData = new MutableLiveData<>();
        removeData = new MutableLiveData<>();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }

    public void init(String postId) {
        if (this.postId != null) {
            return;
        }

        this.postId = postId;
        loadPost();
    }

    public LiveData<Response<PostModel>> getPostData() {
        return postData;
    }

    public LiveData<Action> getRemoveData() {
        return removeData;
    }

    public void loadPost() {
        if (postId == null) {
            return;
        }
        disposable.add(
                repository.get(PostModel.class)
                        .where(PostModel.ID, postId)
                        .findAll()
                        .map(coll -> {
                            if (coll.isEmpty()) {
                                throw new IllegalArgumentException("Post not found");
                            } else {
                                return coll.iterator().next();
                            }
                        })
                        .doOnSubscribe(d -> postData.setValue(Response.loading()))
                        .subscribe(
                                data -> postData.setValue(Response.success(data)),
                                Errors.handle(msg -> postData.setValue(Response.error(msg)))
                        )
        );
    }

    public void removePost() {
        if (postId == null) {
            return;
        }
        disposable.add(
                repository.get(PostModel.class)
                        .where(PostModel.ID, postId)
                        .findAll()
                        .filter(coll -> !coll.isEmpty())
                        .flatMap(coll -> repository.removeAll(PostModel.class, coll))
                        .ignoreElements()
                        .doOnSubscribe(d -> removeData.setValue(Action.loading()))
                        .subscribe(
                                () -> removeData.setValue(Action.success()),
                                Errors.handle(msg -> removeData.setValue(Action.error(msg)))
                        )
        );
    }
}
