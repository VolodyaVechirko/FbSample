package com.vechirko.fbsample.data.model;

import com.vechirko.fbsample.data.repository.HasId;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class CommentModel implements RealmModel, HasId<String> {

    @Ignore
    public static final String ID = "id";
    @Ignore
    public static final String POST_ID = "postId";

    @PrimaryKey
    private String id;
    @Index
    private String postId;
    private String name;
    private String email;
    private String body;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

//        {
//        "postId": 1,
//        "id": 1,
//        "name": "id labore ex et quam laborum",
//        "email": "Eliseo@gardner.biz",
//        "body": "laudantium enim quasi est quidem magnam voluptate ipsam eos\ntempora quo necessitatibus\ndolor quam autem quasi\nreiciendis et nam sapiente accusantium"
//        },
