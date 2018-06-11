package com.vechirko.fbsample.data.test;

import com.vechirko.fbsample.data.repository.HasId;

import io.realm.RealmModel;

public interface AlbumContract extends RealmModel, HasId<String> {

    String ID = "id";
    String USER_ID = "userId";

    String getId();

    void setId(String id);

    String getUserId();

    void setUserId(String userId);

    String getTitle();

    void setTitle(String title);
}
