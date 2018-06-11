package com.vechirko.fbsample.data.model;

import com.google.gson.annotations.SerializedName;
import com.vechirko.fbsample.data.repository.HasId;

import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class PhotoModel implements RealmModel, HasId<String> {

    @Ignore
    public static final String ID = "id";
    @Ignore
    public static final String ALBUM_ID = "albumId";

    @PrimaryKey
    private String id;
    @Index
    private String albumId;
    private String title;
    private String url;

    @SerializedName("thumbnailUrl")
    private String thumb;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}

//      {
//        "albumId": 1,
//        "id": 1,
//        "title": "accusamus beatae ad facilis cum similique qui sunt",
//        "url": "http://placehold.it/600/92c952",
//        "thumbnailUrl": "http://placehold.it/150/92c952"
//        },
