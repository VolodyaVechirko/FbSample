package com.vechirko.fbsample.data.repository;

import com.vechirko.fbsample.data.model.AlbumModel;
import com.vechirko.fbsample.data.model.CommentModel;
import com.vechirko.fbsample.data.model.PhotoModel;
import com.vechirko.fbsample.data.model.PostModel;
import com.vechirko.fbsample.data.model.UserModel;
import com.vechirko.fbsample.data.test.FavouriteAlbum;
import com.vechirko.fbsample.data.test.GeneralAlbum;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RequestAPI {

    @GET("users")
    Observable<List<UserModel>> getUsers();

    @GET("users/{userId}")
    Observable<UserModel> getUser(@Path("userId") String userId);

    @GET("albums")
    Observable<List<AlbumModel>> getAlbums(@Query("userId") String userId);

    @GET("albums/{albumId}")
    Observable<AlbumModel> getAlbum(@Path("albumId") String albumId);

    @GET("photos")
    Observable<List<PhotoModel>> getPhotos(@Query("albumId") String albumId);

    @GET("photos/{photoId}")
    Observable<PhotoModel> getPhoto(@Path("photoId") String photoId);

    @GET("comments")
    Observable<List<CommentModel>> getComments(@Query("postId") String postId);

    @GET("comments/{commentId}")
    Observable<CommentModel> getComment(@Path("commentId") String commentId);

    @GET("posts")
    Observable<List<PostModel>> getPosts(@Query("userId") String userId);

    @GET("posts/{postId}")
    Observable<PostModel> getPost(@Path("postId") String postId);

    @FormUrlEncoded
    @POST("posts")
    Observable<PostModel> createPosts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @PUT("posts/{postId}")
    Observable<PostModel> updatePosts(@Path("postId") String postId, @FieldMap Map<String, String> map);

    @DELETE("posts/{postId}")
    Observable<Void> deletePost(@Path("postId") String postId);


    // interface polymorphism test
    @GET("albums")
    Observable<List<GeneralAlbum>> getAllAlbums();

    @GET("albums?userId=9")
    Observable<List<FavouriteAlbum>> getFavouriteAlbums();
}
