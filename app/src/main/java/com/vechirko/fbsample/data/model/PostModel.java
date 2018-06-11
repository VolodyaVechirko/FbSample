package com.vechirko.fbsample.data.model;

import com.vechirko.fbsample.data.repository.HasId;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PostModel implements RealmModel, HasId<String> {

    @Ignore
    public static final String ID = "id";
    @Ignore
    public static final String USER_ID = "userId";

    @PrimaryKey
    private String id;

    private String title;

    private String body;

    private String userId;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, String> toRequestMap() {
        return new HashMap<String, String>() {{
            if (title != null) put("title", title);
            if (body != null) put("body", body);
            if (userId != null) put("userId", userId);
        }};
    }
}
