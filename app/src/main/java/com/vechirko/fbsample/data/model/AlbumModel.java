package com.vechirko.fbsample.data.model;

import com.vechirko.fbsample.data.repository.HasId;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class AlbumModel implements RealmModel, HasId<String> {

    @Ignore
    public static final String ID = "id";
    @Ignore
    public static final String USER_ID = "userId";

    @PrimaryKey
    private String id;
    @Index
    private String userId;
    private String title;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
